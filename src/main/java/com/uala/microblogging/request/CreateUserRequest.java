package com.uala.microblogging.request;
import jakarta.validation.constraints.NotBlank;

import com.uala.microblogging.entity.User;

import lombok.Builder;

@Builder
public record CreateUserRequest(@NotBlank String username){

    public User toUser() {
        return User.builder()
            .username(username)
            .build();
    }
}