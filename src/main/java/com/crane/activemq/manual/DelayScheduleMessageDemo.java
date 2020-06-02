package com.crane.activemq.manual;

import org.apache.activemq.ScheduledMessage;
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
public class DelayScheduleMessageDemo {

    @Autowired
    private JmsTemplate jmsTemplateQueue;

    //@PostConstruct
    private void init() {
        jmsTemplateQueue.send("queue1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText("hello send queue1");
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,5000L);
                return textMessage;
            }
        });
        jmsTemplateQueue.send("queue1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5 * 1000L); // 延时
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 2 * 1000L); // 投递间隔
                textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 2); // 重复次数
                return textMessage;
            }
        });

        jmsTemplateQueue.send("queue1", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "0 * * * *");
                return textMessage;
            }
        });




    }

    public static void main(String[] args) {
        SpringApplication.run(DelayScheduleMessageDemo.class);

    }
}
