package com.uala.microblogging.response;

import com.uala.microblogging.entity.User;

import lombok.Builder;

@Builder
public record CreatedUserResponseBody(Long id, String username) {

    public static CreatedUserResponseBody from(final User user) {
        return CreatedUserResponseBody.builder()
            .id(user.getId())
            .username(user.getUsername())
            .build();
    }
}