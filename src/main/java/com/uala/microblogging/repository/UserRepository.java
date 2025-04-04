package com.uala.microblogging.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uala.microblogging.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {}