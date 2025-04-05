package com.uala.microblogging.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uala.microblogging.entity.User;
import com.uala.microblogging.request.CreateUserRequest;
import com.uala.microblogging.request.CreateFollowerUserRequest;
import com.uala.microblogging.response.CreatePostResponse;
import com.uala.microblogging.response.CreateUserResponse;
import com.uala.microblogging.service.UserFollowerService;
import com.uala.microblogging.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    final UserService         userService;
    final UserFollowerService userFollowerService;

    @PostMapping
    public CreateUserResponse create(final @RequestBody @Valid CreateUserRequest userRequest) {
        final User createdUser = userService.create(userRequest.toUser());

        return CreateUserResponse.from(createdUser);
    }

    @PostMapping(value = "/follow")
    public ResponseEntity<String> followUser(final @RequestBody @Valid CreateFollowerUserRequest createFollowerUserRequest) {

        userFollowerService.create(createFollowerUserRequest.toUserFollower());

        return ResponseEntity.ok("Follower successfully created");
    }

    @GetMapping("/{userId}/timeline")
    public List<CreatePostResponse> getTimeline(final @PathVariable("userId") Long userId) {
        return userService.getTimeline(userId);
    }
}