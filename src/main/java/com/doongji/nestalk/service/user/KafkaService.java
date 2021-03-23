package com.doongji.nestalk.service.user;

import com.doongji.nestalk.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@RequestMapping("/nestalk")
public class KafkaService {

    private static final String TOPIC = "nestalk";
    private final KafkaTemplate<String, Message> kafkaTemplate;

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        log.info(message.getMessage());
        kafkaTemplate.send(TOPIC, message);
    }

    @KafkaListener(topics = "nestalk", groupId = "kafka-module")
    public Message listen(Message message) {
        log.info(message.getMessage());
        return message;
    }

}
