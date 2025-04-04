package com.uala.microblogging.response;

import com.uala.microblogging.entity.User;

import lombok.Builder;

@Builder
public record CreateUserResponse(Long id, String username) {

    public static CreateUserResponse from(final User user) {
        return CreateUserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .build();
    }
}