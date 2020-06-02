package com.crane.activemq.manual;

/**
 * @description:
 * @author: crane
 * @create: 2020-05-29 11:50
 **/

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 消费分组--生产者
 *
 * 通过message.setStringProperty("JMSXGroupID","GroupA");可以创建一个分组。
 * 同一个组的消息，只会发送到同一个消费者，直到Consumer被关闭
 *
 */
@SpringBootApplication
public class MessageGroupProducer {

    @Autowired
    private JmsTemplate jmsTemplateQueue ;


    @JmsListener(destination = "queue3")
    private void receiveMessage(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("a 收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }

    @JmsListener(destination = "queue3")
    private void receiveMessage1(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("b 收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }


    @JmsListener(destination = "queue3")
    private void receiveMessage2(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("c 收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }

    @PostConstruct
    private void init() {

    }


    public static void main(String[] args) {
        SpringApplication.run(MessageGroupProducer.class);
    }
}
