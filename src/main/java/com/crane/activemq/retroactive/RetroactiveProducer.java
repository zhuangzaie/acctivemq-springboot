package com.crane.activemq.retroactive;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import java.util.Date;

/**
 * @description: 消息追朔
 * @author: crane
 * @create: 2020-06-01 17:37
 **/
@SpringBootApplication
public class RetroactiveProducer {

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
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
        connectionFactory.setUseRetroactiveConsumer(true);
        return connectionFactory;

    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new  JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return  new JmsTemplate(connectionFactory);
    }




    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "queue10")
    private void receive(String message) {
        System.out.println(message);
    }

    @PostConstruct
    private void init() throws InterruptedException {
        jmsTemplate.convertAndSend("queue10",new Date() + "hello");
        System.out.println(new Date() + " hello");
        Thread.sleep(1000L);
    }

    public static void main(String[] args) {
        SpringApplication.run(RetroactiveProducer.class);
    }
}
