package com.uala.microblogging.service;

import java.util.concurrent.CompletableFuture;

import com.uala.microblogging.request.CreatePostRequest;
import com.uala.microblogging.response.CreatedPostResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uala.microblogging.entity.Post;
import com.uala.microblogging.rabbitmq.RabbitMQTimelineProducer;
import com.uala.microblogging.rabbitmq.TimelinePost;
import com.uala.microblogging.repository.PostRepository;
import com.uala.microblogging.repository.UserRepository;
import com.uala.microblogging.response.ResponseBody;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService{

    private final PostRepository                         postRepository;
    private final UserRepository                         userRepository;
    private final RabbitMQTimelineProducer<TimelinePost> rabbitMQTimelineProducer;

    public ResponseEntity<?> create(final CreatePostRequest postRequest) {

        final Long userId = postRequest.createdBy();

        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new ResponseBody("User with id %s does not exist".formatted(userId)));
        }

        final Post createdPost = postRepository.save(postRequest.toPost());

        CompletableFuture.runAsync(() -> rabbitMQTimelineProducer.sendMessage(TimelinePost.from(createdPost)));

        return ResponseEntity.ok().body(CreatedPostResponseBody.from(createdPost));
    }
}