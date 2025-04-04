package com.uala.microblogging.service;

import java.util.concurrent.CompletableFuture;

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

    public Post create(final Post post) {

        validateUserExists(post.getCreatedBy());

        final Post createdPost = postRepository.save(post);

        CompletableFuture.runAsync(() -> rabbitMQTimelineProducer.sendMessage(TimelinePost.from(createdPost)));

        return createdPost;
    }

    private void validateUserExists(Long userId) {
       if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User with id %s does not exist".formatted(userId));
       }
    }
}