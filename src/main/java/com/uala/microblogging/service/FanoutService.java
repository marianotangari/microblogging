package com.uala.microblogging.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uala.microblogging.entity.UserFollower;
import com.uala.microblogging.rabbitmq.TimelinePost;
import com.uala.microblogging.repository.UserFollowerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FanoutService {

    private final RedisService           redisService;
    private final UserFollowerRepository userFollowerRepository;

    public void feedTimeline(final TimelinePost timelinePost) {

        final List<Long> userIds = userFollowerRepository.findUserFollowersByUserId(timelinePost.userId())
                .stream()
                .map(UserFollower::getFollowerUserId)
                .toList();

        redisService.addPostIdToTimeline(userIds, String.valueOf(timelinePost.postId()));
    }
}
