package com.oceanebelle.learn.hateoas.service;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getUsers();

    Optional<UserDTO> getUser(String id);
}
