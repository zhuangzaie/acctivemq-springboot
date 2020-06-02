package com.crane.activemq.durableSubscriber;

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

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @description:
 * @author: crane
 * @create: 2020-05-29 10:53
 **/
@SpringBootApplication
public class DurableSubscriber {
    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.activemq.broker-url-durableSubscriber}") String brokerUrl, @Value("${spring.activemq.user}") String userName, @Value("${spring.activemq.password}") String password) {
        return new ActiveMQConnectionFactory(userName, password, brokerUrl);
    }

    /**
     * topic 监听
     * @return
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactoryTopic(@Value("${spring.activemq.broker-url-durableSubscriber}") String url , ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) connectionFactory;
        activeMQConnectionFactory.setBrokerURL(url);
        bean.setConnectionFactory(activeMQConnectionFactory);
        bean.setSubscriptionDurable(true);
        return bean;
    }

    @Bean
    JmsTemplate jmsDurableSubscriber(@Value("${spring.activemq.broker-url-durableSubscriber}") String url , ConnectionFactory connectionFactory) {
        ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) connectionFactory;
        activeMQConnectionFactory.setBrokerURL(url);
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }





    @Autowired
    private JmsTemplate jmsDurableSubscriber;



    @PostConstruct
    private void init() {
        jmsDurableSubscriber.convertAndSend("queue1","hello queue1");
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
        SpringApplication.run(DurableSubscriber.class);

    }
}
