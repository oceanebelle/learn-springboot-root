package com.oceanebelle.learn.hateoas.service;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.mapper.UserMapper;
import com.oceanebelle.learn.hateoas.repository.UserRepository;
import com.oceanebelle.learn.kafka.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Log4j2
public class UserServiceImpl implements UserService{
    private static final String READ_DB = "READ_DB";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getUsers() {
        log.info(LogMessageFactory.startAction(READ_DB).method().with(userRepository));
        var result = StreamSupport.stream(userRepository.findAll()
                .spliterator(), false)
                .map(UserMapper::mapToDTO)
                .collect(Collectors.toList());
        log.info(LogMessageFactory.completeAction(READ_DB).method().kv("size", result.size()).with(userRepository));
        return result;
    }

    @Override
    public Optional<UserDTO> getUser(String id) {
        log.info(LogMessageFactory.startAction(READ_DB).method().with(userRepository));
        return userRepository.findById(Long.valueOf(id)).map(UserMapper::mapToDTO);
    }
}
