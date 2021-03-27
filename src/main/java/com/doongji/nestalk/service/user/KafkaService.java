package com.doongji.nestalk.service.user;

import com.doongji.nestalk.entity.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Service
@Slf4j
@RequestMapping("/nestalk")
public class KafkaService {

    private static final String TOPIC = "nestalk";
    private final KafkaTemplate<String, SendMessage> kafkaTemplate;

    @PostMapping("/send")
    public void sendMessage(@RequestBody SendMessage message) {
        log.info(message.getResult());
        kafkaTemplate.send(TOPIC, message);
    }

    @KafkaListener(topics = "nestalk", groupId = "nestalk-group")
    public SendMessage listen(SendMessage message) {
        log.info(message.getResult());
        return message;
    }

}
