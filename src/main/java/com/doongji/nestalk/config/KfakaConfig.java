package com.doongji.nestalk.config;

import com.doongji.nestalk.entity.ReceiveMessage;
import com.doongji.nestalk.entity.SendMessage;
import org.apache.kafka.common.network.Receive;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KfakaConfig {

    @Bean
    public ReplyingKafkaTemplate<String, SendMessage, ReceiveMessage> KafkaTemplateConfig(
            ProducerFactory<String, SendMessage> pf,
            ConcurrentKafkaListenerContainerFactory<String, ReceiveMessage> replyContainer) {

        ConcurrentMessageListenerContainer<String, ReceiveMessage> Container = replyContainer.createContainer("nestalk");
        Container.getContainerProperties().setMissingTopicsFatal(false);

        return new ReplyingKafkaTemplate<>(pf, Container);
    }

}
