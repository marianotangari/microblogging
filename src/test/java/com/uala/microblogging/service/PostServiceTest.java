package com.uala.microblogging.service;

import com.uala.microblogging.entity.Post;
import com.uala.microblogging.rabbitmq.RabbitMQTimelineProducer;
import com.uala.microblogging.rabbitmq.TimelinePost;
import com.uala.microblogging.repository.PostRepository;
import com.uala.microblogging.repository.UserRepository;
import com.uala.microblogging.response.CreatedPostResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RabbitMQTimelineProducer<TimelinePost> rabbitMQTimelineProducer;

    private final Post postRequest = Post.builder()
            .createdBy(1L)
            .content("Dummy Post")
            .build();

    @Test
    void create_whenUserDoesNotExist_returnBadRequest() {

        when(userRepository.existsById(1L)).thenReturn(false);

        final ResponseEntity<?> response = postService.create(postRequest);

        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, times(1)).existsById(1L);
        verify(postRepository, never()).save(postRequest);
        verify(rabbitMQTimelineProducer, never()).sendMessage(any());
    }

    @Test
    void create_whenUserExists_returnOk() {

        final CreatedPostResponseBody postResponse = CreatedPostResponseBody.from(postRequest);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(postRepository.save(postRequest)).thenReturn(postRequest);

        final ResponseEntity<?> response = postService.create(postRequest);

        assertSame(HttpStatus.OK, response.getStatusCode());
        assertEquals(postResponse, response.getBody());
        verify(userRepository, times(1)).existsById(1L);
        verify(postRepository, times(1)).save(postRequest);
        verify(rabbitMQTimelineProducer, times(1)).sendMessage(any());
    }
}
