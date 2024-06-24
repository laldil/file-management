package com.example.file_management.mapper;

import com.example.file_management.dto.UserDto;
import com.example.file_management.dto.UserRegisterRequest;
import com.example.file_management.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author aldi
 * @since 17.06.2024
 */

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserEntity mapToEntity(UserRegisterRequest request);

    UserDto mapToDto(UserEntity entity);
}
