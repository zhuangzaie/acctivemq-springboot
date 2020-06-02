package com.crane.activemq.selectors;

import com.crane.activemq.redelivery.RedeliveryPolicyConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Random;

/**
 * @description:
 * @author: crane
 * @create: 2020-05-29 17:13
 **/
@SpringBootApplication
public class SelectorsConsumer {


    /**
     * 连接工厂
     *
     * @param brokerUrl
     * @param userName
     * @param password
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.activemq.broker-url}") String brokerUrl, @Value("${spring.activemq.user}") String userName, @Value("${spring.activemq.password}") String password) {
        return new ActiveMQConnectionFactory(userName, password, brokerUrl);
    }
    /**
     * 队列模式的监听容器
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactoryQueue(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }


    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = "queue6" ,containerFactory = "jmsListenerContainerFactoryQueue" ,selector="age >= 18 and gender = '女'")
    private void receiveQueue(Message message, Session session) throws JMSException, InterruptedException {
        if (message instanceof TextMessage) {
            System.out.println("age >= 18 and gender = '女'：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("age >= 18 and gender = '女'：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }


    @JmsListener(destination = "queue6" ,containerFactory = "jmsListenerContainerFactoryQueue" ,selector="gender = '男'")
    private void receiveQueue1(Message message, Session session) throws JMSException, InterruptedException {
        if (message instanceof TextMessage) {
            System.out.println("gender = '男'：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("收到Map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }


    @PostConstruct
    private void init() {
        String[] gender = new String[]{"男", "女"};
        for (int i = 0; i <10 ; i++) {
            jmsTemplate.send("queue6", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    String text = "Hello ";
                    TextMessage message = session.createTextMessage(text);
                    message.setIntProperty("age", new Random().nextInt(30));
                    message.setStringProperty("gender", gender[ new Random().nextInt(2)]);
                    return message;
                }
            });
        }





    }


    public static void main(String[] args) {
        SpringApplication.run(SelectorsConsumer.class);
    }
}
