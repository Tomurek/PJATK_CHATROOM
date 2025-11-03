package org.example.pjatk_chatroom.controllers;

import org.example.pjatk_chatroom.domain.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWsController {
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    @Value("${chat.kafka.topic}")
    private String topic;

    public ChatWsController(KafkaTemplate<String, MessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @MessageMapping("/chat")
    public void handle(MessageDto incoming) {
        kafkaTemplate.send(topic, incoming.author(), incoming);
    }
}
