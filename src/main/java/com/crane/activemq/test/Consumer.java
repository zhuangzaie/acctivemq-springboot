package com.crane.activemq.test;

import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * @description: Consumer
 * @author: crane
 * @create: 2020-05-27 15:37
 **/
@SpringBootApplication
public class Consumer {


    @JmsListener(destination = "queue1")
    public void receive(String message) {
        System.out.println("queue1 receive message :" + message);
    }

    @JmsListener(destination = "queue2")
    public void receives(String message) {
        System.out.println("queue2 receive message :" + message);
    }

    @JmsListener(destination = "queue2")
    public void receivesTopic(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到Map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }




    public static void main(String[] args) {

        SpringApplication.run(Consumer.class);
    }


}
