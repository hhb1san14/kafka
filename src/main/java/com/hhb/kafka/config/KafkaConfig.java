package com.hhb.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huanghongbo
 * @date: 2020-08-13 13:52
 **/
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic1() {
        //第一个参数为topicName，第二个为：该topic有几个分区，第三个表示：有几个副本
        return new NewTopic("nptc-01", 5, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        //第一个参数为topicName，第二个为：该topic有几个分区，第三个表示：有几个副本
        return new NewTopic("nptc-02", 3, (short) 1);
    }


    /**
     * 重写KafkaAdmin的配置
     *
     * @return
     */
//    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", "hhb:9092");
        KafkaAdmin kafkaAdmin = new KafkaAdmin(config);
        return kafkaAdmin;
    }

    /**
     * 覆盖原有的KafkaTemplate的设置
     *
     * @param producerFactory
     * @return
     */
//    @Bean
//    @Autowired
    public KafkaTemplate<Integer, String> kafkaTemplate(ProducerFactory<Integer, String> producerFactory) {
        //浦发原有的配置
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 200);
        KafkaTemplate<Integer, String> kafkaTemplate = new KafkaTemplate<Integer, String>(producerFactory, config);
        return kafkaTemplate;
    }


}
