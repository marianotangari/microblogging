package com.uala.microblogging.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addPostIdToTimeline(final List<Long> userIds, final String postId) {
        userIds.forEach((userId -> redisTemplate.opsForList().leftPush(String.valueOf(userId), postId)));
    }

    public List<String> getAllPostIds(final Long userId) {
        return redisTemplate.opsForList().range(String.valueOf(userId), 0, -1);
    }
}