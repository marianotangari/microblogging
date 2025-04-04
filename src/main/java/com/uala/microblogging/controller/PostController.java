package com.uala.microblogging.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uala.microblogging.entity.Post;
import com.uala.microblogging.request.CreatePostRequest;
import com.uala.microblogging.response.CreatePostResponse;
import com.uala.microblogging.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    final PostService  postService;

    @PostMapping
    public CreatePostResponse create(final @RequestBody @Valid CreatePostRequest postRequest) {

        final Post createdPost = postService.create(postRequest.toPost());

        return CreatePostResponse.from(createdPost);
    }
}