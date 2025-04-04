package com.uala.microblogging.request;

import jakarta.validation.constraints.NotNull;

import com.uala.microblogging.entity.UserFollower;

import lombok.Builder;

@Builder
public record CreateFollowerUserRequest(@NotNull Long userId, @NotNull Long followerId) {

    public UserFollower toUserFollower() {
        return UserFollower.builder()
            .userId(userId)
            .followerUserId(followerId)
            .build();
    }
}
