package com.example.file_management.mapper;

import com.example.file_management.dto.FileDto;
import com.example.file_management.model.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author aldi
 * @since 18.06.2024
 */

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(target = "owner", expression = "java(UserMapper.INSTANCE.mapToDto(entity.getOwner()))")
    FileDto mapToDto(FileEntity entity);
}
