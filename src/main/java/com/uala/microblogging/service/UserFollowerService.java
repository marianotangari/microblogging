package com.uala.microblogging.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uala.microblogging.entity.UserFollower;
import com.uala.microblogging.repository.UserFollowerRepository;
import com.uala.microblogging.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserFollowerService {

    private final UserRepository         userRepository;
    private final UserFollowerRepository userFollowerRepository;

    public ResponseEntity<String> create(final UserFollower userFollower) {

        final Long userId = userFollower.getUserId();
        final Long followerUserId = userFollower.getFollowerUserId();

        if (validateUserIds(userId, followerUserId)) {
            return ResponseEntity.badRequest().body("Invalid user and follower ids");
        }

        userFollowerRepository.save(userFollower);

        return ResponseEntity.ok("Follower successfully created");
    }

    private boolean validateUserIds(final Long userId, final Long followerUserId) {
        return userId.longValue() == followerUserId.longValue() || !userRepository.existsById(userId) || !userRepository.existsById(followerUserId);
    }
}