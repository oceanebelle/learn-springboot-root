package com.oceanebelle.learn.mongo.controller;


import com.oceanebelle.learn.mongo.service.UserService;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RestController;



// TODO: Add validation to the dto
@Log4j2
@RestController
public class UserAccountController {

//    private final Tracer tracer;
 //   private final UserService userService;
//    private final CurrentTraceContext currentTraceContext;




//    @GetMapping("/users")
//    List<User> getAllUsers(RequestId requestId) {
//        log.info(LogMessageFactory.startAccess("userApi").method().with(requestId));
//
//
//
//        var result = userService.getUsers().stream()
//                .map(u -> new User(u.getName(), u.getId()))
//                .map(u -> u.add(linkTo(methodOn(UserAccountController.class).getUser(u.getId())).withSelfRel()))
//                .collect(Collectors.toList());
//
//        log.info(endAccess("userApi").method().kv("size", result.size()));
//
//        return result;
//    }
//
//    @PostMapping("/users")
//    ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) {
//
//        return ResponseEntity.ok(UserDTO.builder().build());
//    }
//
//
//    @GetMapping("/users/{id}")
//    ResponseEntity<User> getUser(@PathVariable("id") String id) {
//        log.info(LogMessageFactory.startAccess("userApi").method().kv("id", id));
//        var result = userService.getUser(id)
//                .map(u -> new User(u.getName(), u.getId()))
//                .map(u -> u.add(linkTo(methodOn(UserAccountController.class).getAllUsers(null)).withRel("all")))
//                .map(u -> u.add(linkTo(methodOn(UserAccountController.class).getUser(u.getId())).withSelfRel()));
//
//
//        log.info(LogMessageFactory.endAccess("userApi").method().kv("id", id).kv("found", result.isPresent()));
//        if (result.isPresent()) {
//            return ResponseEntity.ok(result.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PatchMapping("/users/{id}")
//    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user, @PathVariable("id") String id) {
//        // TODO fetch the user
//        return ResponseEntity.ok(UserDTO.builder().build());
//    }
//
//    @DeleteMapping("/users/{id}")
//    void deleteUser(@PathVariable("id") String id) {
//        // TODO remove user
//    }

}
