package com.crane.activemq.manual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @description: 手动指定连接方式
 * @author: crane
 * @create: 2020-05-28 09:27
 **/
@SpringBootApplication
public class ManualProducer {

    @Autowired
    private JmsTemplate jmsTemplatePublish;

    @Autowired
    private JmsTemplate jmsTemplateQueue;

    //@PostConstruct
    private void init() {
        jmsTemplateQueue.convertAndSend("queue1","hello queue1");
        jmsTemplateQueue.send("queue1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText("hello send queue1");
                return textMessage;
            }
        });

        jmsTemplatePublish.convertAndSend("queue1","hello jmsTemplatePublish queue1");
        jmsTemplatePublish.send("queue1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText("hello send jmsTemplatePublish queue1");
                return textMessage;
            }
        });


    }

    public static void main(String[] args) {
        SpringApplication.run(ManualProducer.class);

    }
}
