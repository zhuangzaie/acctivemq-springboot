package com.crane.activemq.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @description: mq 事务
 * @author: crane
 * @create: 2020-06-01 15:09
 **/
@SpringBootApplication
public class Producer {
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

    @Bean
    public PlatformTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }


    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return  new JmsTemplate(connectionFactory);
    }


    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                     DefaultJmsListenerContainerFactoryConfigurer defaultJmsListenerContainerFactoryConfigurer,
                                                     PlatformTransactionManager transactionManager) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setTransactionManager(transactionManager);
        factory.setReceiveTimeout(5000L);
        defaultJmsListenerContainerFactoryConfigurer.configure(factory, connectionFactory);
        return factory;
    }



    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Transactional
    @JmsListener(destination = "queue9", containerFactory = "jmsListenerContainerFactory")
    public void sendMsg(Message message) throws JMSException {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        // 模拟消费者异常
        if (((TextMessage) message).getText().endsWith("4")) {
            System.out.println(((TextMessage) message).getText());
            transactionManager.rollback(status);
        }
    }

    @Transactional
    @PostConstruct
    void init() {
        jmsTemplate.convertAndSend("queue9","ssssss");
        jmsTemplate.convertAndSend("queue9","ssssss4");
        jmsTemplate.convertAndSend("queue9","ssssss44423r5");
    }


    public static void main(String[] args) {
        SpringApplication.run(Producer.class);
    }


}


