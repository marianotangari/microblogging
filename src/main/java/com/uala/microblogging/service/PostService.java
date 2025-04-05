package com.uala.microblogging.service;

import java.util.concurrent.CompletableFuture;

import com.uala.microblogging.response.CreatePostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uala.microblogging.entity.Post;
import com.uala.microblogging.rabbitmq.RabbitMQTimelineProducer;
import com.uala.microblogging.rabbitmq.TimelinePost;
import com.uala.microblogging.repository.PostRepository;
import com.uala.microblogging.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService{

    private final PostRepository                         postRepository;
    private final UserRepository                         userRepository;
    private final RabbitMQTimelineProducer<TimelinePost> rabbitMQTimelineProducer;

    public ResponseEntity<?> create(final Post post) {

        final Long userId = post.getCreatedBy();

        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("User with id %s does not exist".formatted(userId));
        }

        final Post createdPost = postRepository.save(post);

        CompletableFuture.runAsync(() -> rabbitMQTimelineProducer.sendMessage(TimelinePost.from(createdPost)));

        return ResponseEntity.ok().body(CreatePostResponse.from(createdPost));
    }
}