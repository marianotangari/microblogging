package com.uala.microblogging.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.uala.microblogging.rabbitmq.RabbitMQConfiguration.ROUTING_KEY;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQTimelineProducer<T> {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(T message) {

        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, ROUTING_KEY, message);

        log.info("Message sent to RabbitMQ Queue {}", RabbitMQConfiguration.QUEUE_NAME);
    }
}