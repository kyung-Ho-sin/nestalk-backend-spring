package com.doongji.nestalk.config;

import com.doongji.nestalk.entity.ReceiveMessage;
import com.doongji.nestalk.entity.SendMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

public class KfakaConfig {

//    @Bean
//    public KafkaMessageListenerContainer<String, SendMessage> replyContainer(
//            ConsumerFactory<String, SendMessage> cf) {
//        ContainerProperties containerProperties = new ContainerProperties("nestalk");
//        return new KafkaMessageListenerContainer<>(cf, containerProperties);
//    }

    @Bean
    public ReplyingKafkaTemplate<String, SendMessage, ReceiveMessage> kafkaTemplate( ProducerFactory<String, SendMessage> pf, ConcurrentKafkaListenerContainerFactory<String, SendMessage> replyContainer) {

        ConcurrentMessageListenerContainer<String, SendMessage> Container = replyContainer.createContainer("v1.kafka.reply");
        Container.getContainerProperties().setMissingTopicsFatal(false);

        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<String, SendMessage> replyTemplate(
            ProducerFactory<String, SendMessage> pf,
            ConcurrentKafkaListenerContainerFactory<String, SendMessage> factory
    ) {
        KafkaTemplate<String, SendMessage> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}
