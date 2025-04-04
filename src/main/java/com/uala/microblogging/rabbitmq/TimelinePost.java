package com.uala.microblogging.rabbitmq;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.uala.microblogging.entity.Post;

import lombok.Builder;

@Builder
public record TimelinePost (Long userId, Long postId, LocalDateTime timestamp) implements Serializable {

    public static TimelinePost from(final Post post) {
        return TimelinePost.builder()
            .postId(post.getId())
            .userId(post.getCreatedBy())
            .timestamp(post.getCreatedAt())
            .build();
    }
}