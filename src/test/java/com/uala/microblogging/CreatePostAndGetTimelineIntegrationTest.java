package com.uala.microblogging;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import com.uala.microblogging.request.CreateFollowerUserRequest;
import com.uala.microblogging.request.CreatePostRequest;
import com.uala.microblogging.request.CreateUserRequest;
import com.uala.microblogging.response.CreateUserResponse;

import lombok.SneakyThrows;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CreatePostAndGetTimelineIntegrationTest {

    @Container
    private final static RedisContainer    redisContainer  = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));
    @Container
    private final static RabbitMQContainer rabbitContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq"));

    @Autowired
    private       MockMvc      mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setup() {
        redisContainer.start();
        rabbitContainer.start();
    }

    @AfterAll
    static void tearDown() {
        redisContainer.stop();
        rabbitContainer.stop();
    }

    @SneakyThrows
    @Test
    void createPostAndGetTimelineTest() {

        final CreateUserRequest createUserRequest = CreateUserRequest.builder().username("TestUser").build();
        final CreateUserResponse createUserResponse = CreateUserResponse.builder().id(4L).username("TestUser").build();

        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserRequest))
            )
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(createUserResponse)));

        final CreateFollowerUserRequest createFollowerUserRequest = CreateFollowerUserRequest.builder().userId(4L).followerId(1L).build();

        mockMvc.perform(
                post("/users/follow")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFollowerUserRequest))
            )
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().string("Follower successfully created"));

        final CreatePostRequest createPostRequest = CreatePostRequest.builder().createdBy(4L).content("My Post").build();

        mockMvc.perform(
                post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequest))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("My Post"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty());

        mockMvc.perform(get("/users/1/timeline"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("My Post"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdBy").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").isNotEmpty());
    }
}