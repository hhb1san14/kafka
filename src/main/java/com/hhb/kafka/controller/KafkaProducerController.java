package com.hhb.kafka.controller;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: huanghongbo
 * @date: 2020-08-13 10:15
 **/
@RestController
public class KafkaProducerController {

    private final static Logger logger = LoggerFactory.getLogger(KafkaProducerController.class);

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;


    /**
     * 同步的方式发送数据
     *
     * @param message
     * @return
     */
    @GetMapping("/sync/send")
    public String syncSend(@RequestParam("message") String message) {
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send("topic-spring-01", 0, 0, message);
        //同步发送数据
        SendResult<Integer, String> sendResult = null;
        try {
            sendResult = future.get();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();
            logger.info("同步发送：分区：" + recordMetadata.partition() + "，主题：" + recordMetadata.topic() + ",提交偏移量:" + recordMetadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "success";
    }


    /**
     * 异步的方式发送数据
     *
     * @param message
     * @return
     */
    @GetMapping("/async/send")
    public String asyncSend(@RequestParam("message") String message) {
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send("topic-spring-01", 0, 1, message);
        // 设置一个回调函数
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            /**
             * 如果发送失败了，执行的方法
             *
             * @param throwable
             */
            @Override
            public void onFailure(Throwable throwable) {
                logger.info("发送消息失败了" + throwable.getMessage());
            }

            /**
             * 如果发送成功了，执行的方法
             *
             * @param integerStringSendResult
             */
            @Override
            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                RecordMetadata consumerRecord = integerStringSendResult.getRecordMetadata();
                logger.info("异步发送：分区：" + consumerRecord.partition() + "，主题：" + consumerRecord.topic() + ",提交偏移量:" + consumerRecord.offset());
            }
        });
        return "success";
    }

}
