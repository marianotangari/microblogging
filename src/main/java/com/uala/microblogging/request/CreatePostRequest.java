package com.uala.microblogging.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.uala.microblogging.entity.Post;

import lombok.Builder;

@Builder
public record CreatePostRequest(@NotNull Long createdBy, @Size(min = 1, max = 280) String content) {

    public Post toPost() {
        return Post.builder()
            .createdBy(createdBy)
            .content(content)
            .createdAt(LocalDateTime.now())
            .build();
    }
}