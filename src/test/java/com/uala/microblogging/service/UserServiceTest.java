package com.uala.microblogging.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uala.microblogging.entity.UserFollower;
import com.uala.microblogging.repository.UserFollowerRepository;
import com.uala.microblogging.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserFollowerRepository userFollowerRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void createFollower_whenUserIdOrFollowerIdIsDoNotExist_thenReturnBadRequest() {

        final UserFollower userFollower = UserFollower.builder().userId(1L).followerUserId(2L).build();

        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        final ResponseEntity<String> response = userService.createFollower(userFollower);

        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, Mockito.times(1)).existsById(Mockito.anyLong());
    }

    @Test
    public void createFollower_whenUserIdOrFollowerIdAreEqual_thenReturnBadRequest() {

        final UserFollower userFollower = UserFollower.builder().userId(1L).followerUserId(1L).build();

        final ResponseEntity<String> response = userService.createFollower(userFollower);

        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, never()).existsById(Mockito.anyLong());
    }

    @Test
    public void createFollower_whenUserIdOrFollowerIdExistAndNotEqual_thenReturnOk() {

        final UserFollower userFollower = UserFollower.builder().userId(1L).followerUserId(2L).build();

        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        final ResponseEntity<String> response = userService.createFollower(userFollower);

        assertEquals(200, response.getStatusCode().value());
        verify(userRepository, Mockito.times(2)).existsById(Mockito.anyLong());
    }
}