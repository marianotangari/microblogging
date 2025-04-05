package com.uala.microblogging.service;

import java.util.ArrayList;
import java.util.List;

import com.uala.microblogging.entity.UserFollower;
import com.uala.microblogging.repository.UserFollowerRepository;
import com.uala.microblogging.response.CreateUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uala.microblogging.entity.User;
import com.uala.microblogging.repository.PostRepository;
import com.uala.microblogging.repository.UserRepository;
import com.uala.microblogging.response.CreatePostResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final RedisService   redisService;

    public ResponseEntity<?> create(final User user) {

        final User createdUser = userRepository.save(user);

        return ResponseEntity.ok(CreateUserResponse.from(createdUser));
    }

    public ResponseEntity<?> getTimeline(final Long userId) {

        List<Long> postIds = redisService.getAllPostIds(userId)
            .stream()
            .map(Long::parseLong)
            .toList();

        if (postIds.isEmpty()) {
            return ResponseEntity.ok(findPostsInDatabase(userId));
        }

        List<CreatePostResponse> postResponses = new ArrayList<>();

        postRepository.findAllById(postIds).forEach(post -> postResponses.add(CreatePostResponse.from(post)));

        return ResponseEntity.ok(postResponses);
    }

    public ResponseEntity<String> createFollower(final UserFollower userFollower) {

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

    private List<CreatePostResponse> findPostsInDatabase(final Long userId) {
        return postRepository.findAllPostsFromFollowedUsers(userId)
            .stream()
            .map(CreatePostResponse::from)
            .toList();
    }
}