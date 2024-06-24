package com.example.file_management.mapper;

import com.example.file_management.dto.ClusterDto;
import com.example.file_management.dto.ClusterShortDto;
import com.example.file_management.dto.CreateClusterRequest;
import com.example.file_management.dto.FileDto;
import com.example.file_management.model.ClusterEntity;
import com.example.file_management.model.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aldi
 * @since 18.06.2024
 */

@Mapper
public interface ClusterMapper {
    ClusterMapper INSTANCE = Mappers.getMapper(ClusterMapper.class);

    ClusterEntity mapToEntity(CreateClusterRequest request);

    @Mapping(target = "owner", expression = "java(UserMapper.INSTANCE.mapToDto(entity.getOwner()))")
    @Mapping(target = "files", expression = "java(getFileList(entity.getFiles()))")
    ClusterDto mapToDto(ClusterEntity entity);

    @Mapping(target = "owner", expression = "java(UserMapper.INSTANCE.mapToDto(entity.getOwner()))")
    ClusterShortDto mapToShortDto(ClusterEntity entity);

    default List<FileDto> getFileList(List<FileEntity> list) {
        if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
        return list.stream().map(FileMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    default List<ClusterShortDto> getShortDtoList(List<ClusterEntity> list) {
        if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
        return list.stream().map(this::mapToShortDto).toList();
    }
}
