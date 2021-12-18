package com.oceanebelle.learn.hateoas.mapper;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.repository.entity.UserEntity;

public class UserMapper {
    public static UserDTO mapToDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .build();
    }
}
