package com.uala.microblogging.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uala.microblogging.entity.UserFollower;

@Repository
public interface UserFollowerRepository extends CrudRepository<UserFollower, Long> {

    List<Long> findUserFollowersByUserId(Long userId);
}