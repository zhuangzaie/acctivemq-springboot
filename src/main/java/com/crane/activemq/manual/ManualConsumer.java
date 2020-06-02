package com.crane.activemq.manual;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @description: 手动指定消费者
 * @author: crane
 * @create: 2020-05-28 09:33
 **/
@SpringBootApplication
public class ManualConsumer {

    @JmsListener(destination = "queue1",containerFactory = "jmsListenerContainerFactoryQueue")
    private void receiveQueue(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }

    @JmsListener(destination = "queue1", containerFactory = "jmsListenerContainerFactoryTopic")
    public void receiveTopic(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }




    public static void main(String[] args) {
        SpringApplication.run(ManualConsumer.class);
    }

}
