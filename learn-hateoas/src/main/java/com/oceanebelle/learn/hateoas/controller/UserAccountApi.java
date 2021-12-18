package com.oceanebelle.learn.hateoas.controller;

import brave.Tracer;
import brave.propagation.CurrentTraceContext;
import com.oceanebelle.learn.hateoas.controller.config.RequestId;
import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.service.UserService;
import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO: Add validation to the dto
@Log4j2
@RestController
public class UserAccountApi {

    private final Tracer tracer;
    private final UserService userService;
    private final CurrentTraceContext currentTraceContext;

    public UserAccountApi(UserService userService, Tracer tracer, CurrentTraceContext currentTraceContext) {
        this.userService = userService;
        this.tracer = tracer;
        this.currentTraceContext = currentTraceContext;
    }


    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUsers(RequestId requestId) throws ExecutionException, InterruptedException, TimeoutException {
        log.info(LogMessageFactory.startAccess("userApi").with(requestId));

        // Test if the trace is propagated
        var future = CompletableFuture.runAsync(currentTraceContext.wrap(() -> log.info("Task ran in separate thread")));
        future.get(1000, TimeUnit.MICROSECONDS);

        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/users")
    ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) {
        // TODO: process the user
        return ResponseEntity.ok(UserDTO.builder().build());
    }


    @GetMapping("/users/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable("id") String id) {
        // TODO fetch the user
        return ResponseEntity.ok(UserDTO.builder().build());
    }

    @PatchMapping("/users/{id}")
    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user, @PathVariable("id") String id) {
        // TODO fetch the user
        return ResponseEntity.ok(UserDTO.builder().build());
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable("id") String id) {
        // TODO remove user
    }

}
