package com.example.file_management.service;

import com.example.file_management.dto.ClusterDto;
import com.example.file_management.dto.ClusterShortDto;
import com.example.file_management.dto.CreateClusterRequest;
import com.example.file_management.exception.PermissionDeniedException;
import com.example.file_management.mapper.ClusterMapper;
import com.example.file_management.model.BaseEntity;
import com.example.file_management.model.ClusterEntity;
import com.example.file_management.model.FileEntity;
import com.example.file_management.model.enums.ClusterAccessType;
import com.example.file_management.repository.ClusterRepository;
import com.example.file_management.repository.ClusterUsersRepository;
import com.example.file_management.repository.UserRepository;
import com.example.file_management.utils.ByteConverterUtils;
import com.example.file_management.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author aldi
 * @since 18.06.2024
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ClusterService {
    private final ClusterRepository clusterRepository;
    private final UserRepository userRepository;
    private final ClusterUsersRepository clusterUsersRepository;

    private final FileService fileService;

    public ClusterDto create(CreateClusterRequest request) {
        var cluster = ClusterMapper.INSTANCE.mapToEntity(request);
        var user = userRepository.findById(SecurityUtils.getCurrentId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        cluster.setCapacity(BigDecimal.ZERO);
        cluster.setMaxCapacity(BigDecimal.valueOf(100));
        cluster.setOwner(user);
        cluster.setBucketName(UUID.randomUUID().toString() + new Date().getTime());

        var savedCluster = clusterRepository.save(cluster);
        return ClusterMapper.INSTANCE.mapToDto(savedCluster);
    }

    public Pair<List<ClusterShortDto>, Integer> getClusterList(Integer page, Integer size, Boolean isOwner) {
        var user = userRepository.findById(SecurityUtils.getCurrentId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<ClusterEntity> clusterPage;
        if (isOwner) {
            clusterPage = clusterRepository.findByOwner(user, PageRequest.of(page, size));
        } else {
            var userClusters = clusterUsersRepository.findByUserId(user.getId());
            var idList = userClusters.stream().map(uc -> uc.getCluster().getId()).toList();
            clusterPage = clusterRepository.findByIdIn(idList, PageRequest.of(page, size));
        }

        var clusterList = clusterPage.stream().map(ClusterMapper.INSTANCE::mapToShortDto).toList();
        return Pair.of(clusterList, (int) clusterPage.getTotalElements());
    }

    @Transactional
    public ClusterDto uploadFiles(List<MultipartFile> fileList, Long clusterId) {
        var cluster = clusterRepository.findById(clusterId).orElseThrow(() -> new EntityNotFoundException("Cluster not found"));

        double fileListCapacity = ByteConverterUtils.bytesToMegabytes(fileList.stream().mapToLong(MultipartFile::getSize).sum());
        boolean hasEnoughCapacity = BigDecimal.valueOf(fileListCapacity).compareTo(cluster.getFreeCapacity()) < 0;
        if (!hasEnoughCapacity) {
            throw new BadCredentialsException("Not enough memory in the cluster (%.2f). Your files %.2f"
                    .formatted(cluster.getFreeCapacity(), fileListCapacity));
        }

        var uploadedFiles = fileService.uploadList(fileList, cluster.getBucketName());
        cluster.getFiles().addAll(uploadedFiles);
        cluster.setCapacity(cluster.getCapacity().add(BigDecimal.valueOf(fileListCapacity)));
        var savedCluster = clusterRepository.save(cluster);

        return ClusterMapper.INSTANCE.mapToDto(savedCluster);
    }

    public ClusterDto get(Long id) {
        var cluster = clusterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cluster not found"));

        if (cluster.getAccessType().equals(ClusterAccessType.PRIVATE)) {
            if (!cluster.getOwner().getId().equals(SecurityUtils.getCurrentId())) {
                log.warn("Unauthorized access attempt: User {} tried to access private cluster {} owned by {}",
                        SecurityUtils.getCurrentId(), cluster.getId(), cluster.getOwner().getId());
                throw new BadCredentialsException("Cluster not found");
            }
        }

        return ClusterMapper.INSTANCE.mapToDto(cluster);
    }

    public Boolean delete(Long id) {
        var cluster = clusterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cluster not found"));

        if (!cluster.getOwner().getId().equals(SecurityUtils.getCurrentId())) {
            throw new PermissionDeniedException("Cluster doesn't belong to the user");
        }

        clusterRepository.delete(cluster);
        return true;
    }

    @Transactional
    public Boolean deleteFiles(Long clusterId, List<Long> fileIds) {
        var cluster = clusterRepository.findById(clusterId).orElseThrow(() -> new EntityNotFoundException("Cluster not found"));

        Map<Long, FileEntity> fileMap = cluster.getFiles().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
        var newCapacity = cluster.getCapacity();

        for (Long fileId : fileIds) {
            var file = fileMap.get(fileId);
            if (file == null) {
                log.warn("File with ID {} not found in cluster with ID {}", fileId, clusterId);
                continue;
            }

            try {
                fileService.delete(fileId, cluster.getBucketName());
                newCapacity = newCapacity.subtract(file.getSize());
                cluster.getFiles().remove(file);
            } catch (Exception e) {
                log.error("Failed to delete file with ID {}: {}", fileId, e.getMessage());
            }
        }

        cluster.setCapacity(newCapacity);
        clusterRepository.save(cluster);

        return true;
    }
}
