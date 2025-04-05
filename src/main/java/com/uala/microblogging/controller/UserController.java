package com.uala.microblogging.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uala.microblogging.request.CreateUserRequest;
import com.uala.microblogging.request.CreateFollowerUserRequest;
import com.uala.microblogging.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    final UserService         userService;

    @PostMapping
    public ResponseEntity<?> create(final @RequestBody @Valid CreateUserRequest userRequest) {
        return userService.create(userRequest.toUser());
    }

    @PostMapping(value = "/follow")
    public ResponseEntity<?> followUser(final @RequestBody @Valid CreateFollowerUserRequest createFollowerUserRequest) {
        return userService.createFollower(createFollowerUserRequest.toUserFollower());
    }

    @GetMapping("/{userId}/timeline")
    public ResponseEntity<?> getTimeline(final @PathVariable("userId") Long userId) {
        return userService.getTimeline(userId);
    }
}