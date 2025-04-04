package com.uala.microblogging.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uala.microblogging.entity.UserFollower;
import com.uala.microblogging.repository.UserFollowerRepository;
import com.uala.microblogging.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserFollowerService {

    private final UserRepository         userRepository;
    private final UserFollowerRepository userFollowerRepository;

    @Transactional
    public void create(final UserFollower userFollower) {

        validateUsersExist(userFollower.getUserId(), userFollower.getFollowerUserId());

        userFollowerRepository.save(userFollower);
    }

    private void validateUsersExist(final Long userId, final Long followerUserId) {
        if (!userRepository.existsById(userId) || !userRepository.existsById(followerUserId)) {
            throw new IllegalArgumentException("User with id %s not found");
        }
    }
}