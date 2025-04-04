package com.uala.microblogging.response;

import java.time.LocalDateTime;

import com.uala.microblogging.entity.Post;

import lombok.Builder;

@Builder
public record CreatePostResponse(Long id, Long createdBy, LocalDateTime createdAt, String content) {

    public static CreatePostResponse from(final Post post) {
        return CreatePostResponse.builder()
            .id(post.getId())
            .createdBy(post.getCreatedBy())
            .createdAt(post.getCreatedAt())
            .content(post.getContent())
            .build();
    }
}