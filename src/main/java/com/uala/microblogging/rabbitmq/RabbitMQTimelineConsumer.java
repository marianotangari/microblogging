package com.uala.microblogging.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.uala.microblogging.service.FanoutService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQTimelineConsumer {

    private final FanoutService fanoutService;

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_NAME)
    public void listen(TimelinePost message) {

        log.info("Message received: {}", message);

        fanoutService.feedTimeline(message);
    }
}