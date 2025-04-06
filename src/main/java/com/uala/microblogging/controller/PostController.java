package com.uala.microblogging.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uala.microblogging.request.CreatePostRequest;
import com.uala.microblogging.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    final PostService  postService;

    @PostMapping
    public ResponseEntity<?> create(final @RequestBody @Valid CreatePostRequest postRequest) {
        return postService.create(postRequest);
    }
}