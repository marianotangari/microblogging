package com.uala.microblogging.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RedisService   redisService;

    @Transactional
    public User create(final User user) {
        return userRepository.save(user);
    }

    public List<CreatePostResponse> getTimeline(final Long userId) {

        List<Long> postIds = redisService.getAllPostIds(userId)
            .stream()
            .map(Long::parseLong)
            .toList();

        if (postIds.isEmpty()) {
            return findPostsInDatabase(userId);
        }

        List<CreatePostResponse> postResponses = new ArrayList<>();

        postRepository.findAllById(postIds).forEach(post -> postResponses.add(CreatePostResponse.from(post)));

        return postResponses;
    }

    private List<CreatePostResponse> findPostsInDatabase(final Long userId) {
        return postRepository.findAllPostsFromFollowedUsers(userId)
            .stream()
            .map(CreatePostResponse::from)
            .toList();
    }
}