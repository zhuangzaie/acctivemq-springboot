package com.crane.activemq.manual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;

/**
 * 复合目标
 *
 * 如果需要将一条消息发送到多个不同的目标，可以在创建目标时，通过","来分隔多个目标。
 * 例如：queue1,queue2,queue3
 *
 * 如果需要匹配目标类型，可以通过增加topic://或queue://前缀来实现。例如在队列上发送消息，同时也发送到主题上：
 * queue1,topic://topic1
 */
/**
 * @description: 复合目标
 * @author: crane
 * @create: 2020-06-02 09:55
 **/

@SpringBootApplication
public class CompositeDestProducer {

    @Autowired
    private JmsTemplate jmsTemplateQueue;

    @JmsListener(destination = "queue11")
    private void receive(String message) {
        System.out.println("queue1 :" +message );
    }

    @JmsListener(destination = "queue12")
    private void receive1(String message) {
        System.out.println("queue2 :" +message );
    }


    @PostConstruct
    private void  init() {
        jmsTemplateQueue.convertAndSend("queue11,queue12","hello activemq");
    }



    public static void main(String[] args) {
        SpringApplication.run(CompositeDestProducer.class);
    }
}
