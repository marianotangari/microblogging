package com.uala.microblogging.service;

import java.util.ArrayList;
import java.util.List;

import com.uala.microblogging.entity.UserFollower;
import com.uala.microblogging.repository.UserFollowerRepository;
import com.uala.microblogging.response.CreatedUserResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uala.microblogging.entity.User;
import com.uala.microblogging.repository.PostRepository;
import com.uala.microblogging.repository.UserRepository;
import com.uala.microblogging.response.CreatedPostResponseBody;
import com.uala.microblogging.response.ResponseBody;

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

        return ResponseEntity.ok(CreatedUserResponseBody.from(createdUser));
    }

    public ResponseEntity<?> getTimeline(final Long userId) {

        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new ResponseBody("User with id %s does not exist".formatted(userId)));
        }

        List<Long> postIds = redisService.getAllPostIds(userId)
            .stream()
            .map(Long::parseLong)
            .toList();

        if (postIds.isEmpty()) {
            return ResponseEntity.ok(findPostsInDatabase(userId));
        }

        List<CreatedPostResponseBody> postResponses = new ArrayList<>();

        postRepository.findAllById(postIds).forEach(post -> postResponses.add(CreatedPostResponseBody.from(post)));

        return ResponseEntity.ok(postResponses);
    }

    public ResponseEntity<?> createFollower(final UserFollower userFollower) {

        final Long userId = userFollower.getUserId();
        final Long followerUserId = userFollower.getFollowerUserId();

        if (validateUserIds(userId, followerUserId)) {
            return ResponseEntity.badRequest().body(new ResponseBody("Invalid user and follower ids"));
        }

        userFollowerRepository.save(userFollower);

        return ResponseEntity.ok(new ResponseBody("Follower successfully created"));
    }

    private boolean validateUserIds(final Long userId, final Long followerUserId) {
        return userId.longValue() == followerUserId.longValue() || !userRepository.existsById(userId) || !userRepository.existsById(followerUserId);
    }

    private List<CreatedPostResponseBody> findPostsInDatabase(final Long userId) {
        return postRepository.findAllPostsFromFollowedUsers(userId)
            .stream()
            .map(CreatedPostResponseBody::from)
            .toList();
    }
}