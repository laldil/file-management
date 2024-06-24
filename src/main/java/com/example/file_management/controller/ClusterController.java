package com.example.file_management.controller;

import com.example.file_management.controller.api.ApiDataResponse;
import com.example.file_management.controller.api.ApiListResponse;
import com.example.file_management.dto.ClusterDto;
import com.example.file_management.dto.ClusterShortDto;
import com.example.file_management.dto.CreateClusterRequest;
import com.example.file_management.service.ClusterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author aldi
 * @since 18.06.2024
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cluster")
public class ClusterController {
    private final ClusterService clusterService;

    @PostMapping
    public ResponseEntity<ApiDataResponse<ClusterDto>> create(@Valid @RequestBody CreateClusterRequest request) {
        return ResponseEntity.ok().body(ApiDataResponse.create(clusterService.create(request)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiListResponse<ClusterShortDto>> getList(@RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer size,
                                                                    @RequestParam(required = false, defaultValue = "false") Boolean isOwner) {
        var data = clusterService.getClusterList(page, size, isOwner);
        return ResponseEntity.ok().body(ApiListResponse.create(data.getFirst(), data.getSecond()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<ClusterDto>> getCluster(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ApiDataResponse.create(clusterService.get(id)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @PostMapping("/{clusterId}/files")
    public ResponseEntity<ApiDataResponse<ClusterDto>> uploadFiles(@PathVariable Long clusterId, @RequestParam List<MultipartFile> files) {
        try {
            return ResponseEntity.ok().body(ApiDataResponse.create(clusterService.uploadFiles(files, clusterId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @DeleteMapping("/{clusterId}/files")
    public ResponseEntity<ApiDataResponse<Boolean>> deleteFiles(@PathVariable Long clusterId, @RequestParam List<Long> fileIds) {
        try {
            return ResponseEntity.ok().body(ApiDataResponse.create(clusterService.deleteFiles(clusterId, fileIds)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Boolean>> deleteCluster(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ApiDataResponse.create(clusterService.delete(id)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(ApiDataResponse.failed(e.getMessage()));
        }
    }
}
