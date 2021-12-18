package com.oceanebelle.learn.hateoas.service;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers();
}
