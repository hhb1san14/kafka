package com.hhb.kafka.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: huanghongbo
 * @date: 2020-08-13 11:35
 **/
@Component
public class KafkaConsumerController {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerController.class);

    /**
     * 消费者消费消息
     *
     * @param record
     */
    @KafkaListener(topics = "topic-spring-01")
    public void onMessage(ConsumerRecord<Integer, String> record) {
        logger.info("消费者：分区：" + record.partition() +
                "，主题：" + record.topic() +
                ",提交偏移量:" + record.offset() +
                ",key :  " + record.key() +
                ",value: " + record.value());
    }


}
