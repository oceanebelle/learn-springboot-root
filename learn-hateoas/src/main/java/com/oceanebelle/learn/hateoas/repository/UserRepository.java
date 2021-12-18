package com.oceanebelle.learn.hateoas.repository;

import com.oceanebelle.learn.hateoas.repository.entity.UserEntity;
import com.oceanebelle.learn.logging.LogMessage;
import com.oceanebelle.learn.logging.LogMessageVisitor;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long>, LogMessageVisitor {
    @Override
    default void visit(LogMessage message) {
        message.kv("entity", UserEntity.class.getSimpleName());
    }
}
