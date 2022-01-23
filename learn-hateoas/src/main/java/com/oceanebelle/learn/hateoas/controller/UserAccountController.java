package com.oceanebelle.learn.hateoas.controller;

import com.oceanebelle.learn.hateoas.controller.config.RequestId;
import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.controller.hateoas.model.User;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.oceanebelle.learn.logging.LogMessageFactory.endAccess;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: Add validation to the dto
@Log4j2
@RestController
public class UserAccountController {

//    private final Tracer tracer;
    private final UserService userService;
//    private final CurrentTraceContext currentTraceContext;

    public UserAccountController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    List<User> getAllUsers(RequestId requestId) {
        log.info(LogMessageFactory.startAccess("userApi").method().with(requestId));

//        try {
//            // Test if the trace is propagated
//            var future = CompletableFuture.runAsync(currentTraceContext.wrap(() -> log.info("Task ran in separate thread")));
//            future.get(1000, TimeUnit.MICROSECONDS);
//        } catch (Exception ex) {
//            log.error(failAccess("userApi").method(), ex);
//        }

        var result = userService.getUsers().stream()
                .map(u -> new User(u.getName(), u.getId()))
                .map(u -> u.add(linkTo(methodOn(UserAccountController.class).getUser(u.getId())).withSelfRel()))
                .collect(Collectors.toList());

        log.info(endAccess("userApi").method().kv("size", result.size()));

        return result;
    }

    @PostMapping("/users")
    ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) {

        return ResponseEntity.ok(UserDTO.builder().build());
    }


    @GetMapping("/users/{id}")
    ResponseEntity<User> getUser(@PathVariable("id") String id) {
        log.info(LogMessageFactory.startAccess("userApi").method().kv("id", id));
        var result = userService.getUser(id)
                .map(u -> new User(u.getName(), u.getId()))
                .map(u -> u.add(linkTo(methodOn(UserAccountController.class).getAllUsers(null)).withRel("all")))
                .map(u -> u.add(linkTo(methodOn(UserAccountController.class).getUser(u.getId())).withSelfRel()));


        log.info(LogMessageFactory.endAccess("userApi").method().kv("id", id).kv("found", result.isPresent()));
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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
