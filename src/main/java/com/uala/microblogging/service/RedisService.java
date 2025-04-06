package com.uala.microblogging.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addPostIdToUsersTimeline(final List<Long> userIds, final Long postId) {
        userIds.forEach((userId -> redisTemplate
            .opsForList()
            .leftPush(String.valueOf(userId), String.valueOf(postId))));
    }

    public void addPostIdsToUserTimeline(final Long userId, final List<Long> postIds) {

        final List<String> strPostIds = postIds.stream().map(String::valueOf).toList();

        redisTemplate.opsForList().leftPushAll(String.valueOf(userId), strPostIds);
    }

    public List<String> getAllPostIds(final Long userId) {
        return redisTemplate.opsForList().range(String.valueOf(userId), 0, -1);
    }
}