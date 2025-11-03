package org.example.pjatk_chatroom.service;

import lombok.extern.slf4j.Slf4j;
import org.example.pjatk_chatroom.domain.MessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatKafkaListener {

    private final SimpMessagingTemplate ws;
    private final MessageService messageService;

    public ChatKafkaListener(SimpMessagingTemplate ws, MessageService messageService) {
        this.ws = ws;
        this.messageService = messageService;
    }

    @KafkaListener(
            topics = "${chat.kafka.topic}",
            groupId = "${chat.kafka.group-id}",
            containerFactory = "chatKafkaListenerContainerFactory"
    )
    public void onMessage(MessageDto msg) {
        log.info("Received message. Processing...");
        messageService.addMessageWithNormalization(msg);
        log.info("Message processed.");
        ws.convertAndSend("/topic/greetings", msg);
        log.info("Message sent to websocket.");
    }
}
