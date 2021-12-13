package com.oceanebelle.learn.hateoas.controller;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

// TODO: Add validation to the dto


@Log4j2
@RestController
public class UserAccountApi {

    @GetMapping("/users")
    List<UserDTO> getAllUsers() {
        // TODO: list all users
        return Collections.emptyList();
    }

    @PostMapping("/users")
    UserDTO addUser(@RequestBody UserDTO user) {
        // TODO: process the user
        return UserDTO.builder().build();
    }


    @GetMapping("/users/{id}")
    UserDTO getUser(@PathVariable("id") String id) {
        // TODO fetch the user
        return UserDTO.builder().build();
    }

    @PatchMapping("/users/{id}")
    UserDTO updateUser(@RequestBody UserDTO user, @PathVariable("id") String id) {
        // TODO fetch the user
        return UserDTO.builder().build();
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable("id") String id) {
        // TODO remove user
    }

}