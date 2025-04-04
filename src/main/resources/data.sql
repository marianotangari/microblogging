-- Create users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50)
);

-- Create posts table
CREATE TABLE posts (
    id BIGINT PRIMARY KEY,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP,
    content VARCHAR(280)
);

CREATE TABLE user_followers (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    follower_user_id BIGINT NOT NULL
)