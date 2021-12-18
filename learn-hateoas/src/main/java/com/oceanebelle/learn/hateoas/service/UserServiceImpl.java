package com.oceanebelle.learn.hateoas.service;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.mapper.UserMapper;
import com.oceanebelle.learn.hateoas.repository.UserRepository;
import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Log4j2
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getUsers() {
        log.info(LogMessageFactory.startAction("READ_DB").method("getUsers").with(userRepository));
        var result = StreamSupport.stream(userRepository.findAll()
                .spliterator(), false)
                .map(UserMapper::mapToDTO)
                .collect(Collectors.toList());
        log.info(LogMessageFactory.endAction("READ_DB").method("getUsers").kv("size", result.size()).with(userRepository));
        return result;
    }
}
