package com.backend.tasks.repository;

import org.springframework.data.repository.CrudRepository;

import com.backend.tasks.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
