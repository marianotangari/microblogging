package com.uala.microblogging.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uala.microblogging.entity.Post;
import com.uala.microblogging.repository.PostRepository;
import com.uala.microblogging.repository.UserFollowerRepository;
import com.uala.microblogging.repository.UserRepository;
import com.uala.microblogging.request.CreateFollowerUserRequest;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserFollowerRepository userFollowerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private RedisService redisService;

    @Test
    public void createFollower_whenUserIdOrFollowerIdIsDoNotExist_thenReturnBadRequest() {

        final CreateFollowerUserRequest followerUserRequest = CreateFollowerUserRequest.builder().userId(1L).followerId(2L).build();

        when(userRepository.existsById(anyLong())).thenReturn(false);

        final ResponseEntity<?> response = userService.createFollower(followerUserRequest);

        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userFollowerRepository, never()).save(followerUserRequest.toUserFollower());
    }

    @Test
    public void createFollower_whenUserIdOrFollowerIdAreEqual_thenReturnBadRequest() {

        final CreateFollowerUserRequest followerUserRequest = CreateFollowerUserRequest.builder().userId(1L).followerId(1L).build();

        final ResponseEntity<?> response = userService.createFollower(followerUserRequest);

        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, never()).existsById(anyLong());
        verify(userFollowerRepository, never()).save(followerUserRequest.toUserFollower());
    }

    @Test
    public void createFollower_whenUserIdOrFollowerIdExistAndNotEqual_thenReturnOk() {

        final CreateFollowerUserRequest followerUserRequest = CreateFollowerUserRequest.builder().userId(1L).followerId(2L).build();

        when(userRepository.existsById(anyLong())).thenReturn(true);

        final ResponseEntity<?> response = userService.createFollower(followerUserRequest);

        assertEquals(200, response.getStatusCode().value());
        verify(userRepository, times(2)).existsById(anyLong());
        verify(userFollowerRepository, times(1)).save(any());
    }

    @Test
    public void getTimeline_whenUserDoesNotExist_thenReturnBadRequest() {

        when(userRepository.existsById(anyLong())).thenReturn(false);

        final ResponseEntity<?> response = userService.getTimeline(1L);

        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, times(1)).existsById(1L);
        verify(redisService, never()).getAllPostIds(1L);
        verify(postRepository, never()).findAllById(List.of(1L));
        verify(postRepository, never()).findAllPostsFromFollowedUsers(1L);
    }

    @Test
    public void getTimeline_whenPostsNotInRedis_thenReturnPostsFromDatabase() {

        final Post post = Post.builder().build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(redisService.getAllPostIds(1L)).thenReturn(List.of());
        when(postRepository.findAllPostsFromFollowedUsers(1L)).thenReturn(List.of(post));

        final ResponseEntity<?> response = userService.getTimeline(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(userRepository, times(1)).existsById(1L);
        verify(redisService, times(1)).getAllPostIds(1L);
        verify(postRepository, never()).findAllById(List.of(1L));
        verify(postRepository, times(1)).findAllPostsFromFollowedUsers(1L);
    }

    @Test
    public void getTimeline_whenPostsFoundInRedis_thenReturnPosts() {

        final Post post = Post.builder().build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(redisService.getAllPostIds(1L)).thenReturn(List.of("1"));
        when(postRepository.findAllById(List.of(1L))).thenReturn(List.of(post));

        final ResponseEntity<?> response = userService.getTimeline(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(userRepository, times(1)).existsById(1L);
        verify(redisService, times(1)).getAllPostIds(1L);
        verify(postRepository, times(1)).findAllById(List.of(1L));
        verify(postRepository, never()).findAllPostsFromFollowedUsers(1L);
    }
}