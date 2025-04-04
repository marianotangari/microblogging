package com.uala.microblogging.repository;

import java.util.List;

import org.springframework.data.jpa.repository.NativeQuery;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uala.microblogging.entity.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @NativeQuery("""
        SELECT p.* FROM user_followers uf\s
        JOIN posts p ON p.created_by = uf.user_id\s
        WHERE uf.follower_user_id = :userId
        """)
    List<Post> findAllPostsFromFollowedUsers(final @Param("userId") Long userId);
}
