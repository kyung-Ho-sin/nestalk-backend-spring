package com.doongji.nestalk.service.user;

import com.doongji.nestalk.entity.ReceiveMessage;
import com.doongji.nestalk.entity.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
@Slf4j
@RequestMapping("/nestalk")
public class KafkaService {

    private static final String TOPIC = "nestalk";
    private final ReplyingKafkaTemplate<String, SendMessage, ReceiveMessage> replyingkafkaTemplate;

    @PostMapping("/send")
    public Optional<ReceiveMessage> sendMessage(@RequestBody SendMessage message) throws ExecutionException, InterruptedException {
        return Optional.ofNullable(replyingkafkaTemplate.sendAndReceive(new ProducerRecord<>(TOPIC, message))
                .completable()
                .exceptionally(throwable -> {
                    log.warn("fail : {}", throwable.getMessage(), throwable);
                    return null;
                })
                .thenApply(ConsumerRecord::value)
                .get()
        );
    }

    @KafkaListener(topics = "nestalk", groupId = "nestalk-group")
    @SendTo
    public SendMessage listen(SendMessage message) {
        log.info(message.getResult());
        return message;
    }

}
