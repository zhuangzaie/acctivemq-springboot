package com.crane.activemq.test;

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
 * @description: produce
 * @author: crane
 * @create: 2020-05-27 15:23
 **/
@SpringBootApplication
public class Produce {
    @Autowired
    private JmsTemplate jmsTemplate;


    @PostConstruct
    public void init() throws JMSException {
/*        MessageCreator messageCreator = send();
        MessageCreator message1 = session -> {
            TextMessage textMessage = session.createTextMessage("Hello world groupB" );
            textMessage.setStringProperty("JMSXGroupID", "GroupB");
            textMessage.setIntProperty("JMSXGroupSeq", -1);

            return textMessage;
        };
        MessageCreator groupB = session -> {
            TextMessage textMessage = session.createTextMessage("Hello world groupB" );
            textMessage.setStringProperty("JMSXGroupID", "GroupB");
            return textMessage;
        };
        for (int i = 0; i <10 ; i++) {
            if (5 == i) {
                jmsTemplate.send("queue3", message1);
            } else {
                jmsTemplate.send("queue3", messageCreator);
            }
            jmsTemplate.send("queue3",groupB );

        }*/
        //jmsTemplate.setPubSubDomain(true);
/*        for (int i = 0; i < 20; i++) {
            jmsTemplate.convertAndSend("queue3","hello queue1");
        }
        jmsTemplate.convertAndSend("queue2","hello queue2");
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.send("queue3", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText("hello queue1 topic?");
                return message;
            }
        });*/
    }

    private MessageCreator send() {
        return session -> {
            TextMessage textMessage = session.createTextMessage("Hello world groupA" );
            textMessage.setStringProperty("JMSXGroupID", "GroupA");
            return textMessage;
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(Produce.class,args);
    }
}
