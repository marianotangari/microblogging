ALTER TABLE posts ADD CONSTRAINT FK_POSTS FOREIGN KEY(created_by) REFERENCES users(id);
ALTER TABLE user_followers ADD CONSTRAINT FK_USER_ID FOREIGN KEY(user_id) REFERENCES users(id);
ALTER TABLE user_followers ADD CONSTRAINT FK_FOLLOWER_USER_ID FOREIGN KEY(follower_user_id) REFERENCES users(id);

-- Insert test users
INSERT INTO users VALUES(default, 'test_username_1');
INSERT INTO users VALUES(default, 'test_username_2');
INSERT INTO users VALUES(default, 'test_username_3');