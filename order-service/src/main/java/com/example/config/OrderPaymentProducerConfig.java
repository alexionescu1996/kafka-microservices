package com.example.config;

import com.example.model.OrderCreatedEvent;
import com.example.model.OrderUpdateEvent;
import com.example.partitioner.OrderLevelPartitioner;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderPaymentProducerConfig {

    private static final Logger log = LoggerFactory.getLogger(OrderProducerConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${topic.orders-update.name}")
    private String ordersUpdateTopic;


    @Bean
    public NewTopic ordersUpdateTopic() {
        log.info("creating orders-update topic :: {}", ordersUpdateTopic);
        return TopicBuilder.name(ordersUpdateTopic)
                .partitions(3)
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderUpdateEvent> orderUpdateProducerFactory() {

        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                OrderLevelPartitioner.class.getName());

        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderUpdateEvent> orderUpdateKafkaTemplate() {
        return new KafkaTemplate<>(orderUpdateProducerFactory());
    }
}
