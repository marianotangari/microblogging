package com.uala.microblogging.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_followers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserFollower extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "follower_user_id")
    private Long followerUserId;
}