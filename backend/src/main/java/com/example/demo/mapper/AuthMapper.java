package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.AuthDto.UserLogin;
import com.example.demo.model.AuthModel;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "username", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productModels", ignore = true)
    AuthModel toEntity(AuthDto.RegisterRequest authDto);

    @Mapping(source = "name", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "id", target = "id")
    UserLogin toDto(AuthModel authModel);
}
