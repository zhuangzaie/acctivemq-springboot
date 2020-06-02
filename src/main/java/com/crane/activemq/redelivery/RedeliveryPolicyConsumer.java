package com.crane.activemq.redelivery;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
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
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.CountDownLatch;

/**
 * @description: 消息重发
 * @author: crane
 * @create: 2020-05-29 15:34
 **/
@SpringBootApplication
public class RedeliveryPolicyConsumer {


    /**
     * 连接工厂
     *
     * @param brokerUrl
     * @param userName
     * @param password
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactoryRedeliveryPolicyConsumer(@Value("${spring.activemq.broker-url}") String brokerUrl, @Value("${spring.activemq.user}") String userName, @Value("${spring.activemq.password}") String password) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
        // 创建队列重发策略
        RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
        queuePolicy.setInitialRedeliveryDelay(0); // 初始重发延迟时间，单位：毫秒
        queuePolicy.setRedeliveryDelay(5000); // 第一次以后的延迟时间
        queuePolicy.setUseExponentialBackOff(false);// 是否以指数递增的方式增加超时时间
        queuePolicy.setMaximumRedeliveries(3); // 最大重发次数，从0开始计数，为-1则不使用最大次数
        connectionFactory.setRedeliveryPolicy(queuePolicy);
        return connectionFactory;
    }
    /**
     * topic 监听
     * @param connectionFactoryRedeliveryPolicyConsumer
     * @return
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerRedeliveryPolicyConsumer(ConnectionFactory connectionFactoryRedeliveryPolicyConsumer) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactoryRedeliveryPolicyConsumer);
        bean.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return bean;
    }

    /**
     * 发布订阅模板
     *
     * @param connectionFactoryRedeliveryPolicyConsumer
     * @return
     */
    @Bean
    public JmsTemplate jmsTemplateRedeliveryPolicyConsumer(ConnectionFactory connectionFactoryRedeliveryPolicyConsumer) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactoryRedeliveryPolicyConsumer);
        return jmsTemplate;
    }

    @JmsListener(destination = "queue5",containerFactory = "jmsListenerRedeliveryPolicyConsumer")
    private void receiveQueue(Message message, Session session) throws JMSException, InterruptedException {
        try {
            // 模拟消费者异常
            if (((TextMessage) message).getText().endsWith("4")) {
                throw new RuntimeException("消息重发");
            }

            if (message instanceof TextMessage) {
                System.out.println("收到文本消息：" + ((TextMessage) message).getText());
            } else {
                System.out.println(message);
            }
            // 8、确认收到消息
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {

            try {
                // 消息重发
                session.recover();
                System.out.println(e.getMessage());
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Autowired
    JmsTemplate jmsTemplateRedeliveryPolicyConsumer;

    @PostConstruct
    private void init() {
        for (int i = 0; i <10 ; i++) {
            jmsTemplateRedeliveryPolicyConsumer.convertAndSend("queue5", "hello queue " + i);
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(RedeliveryPolicyConsumer.class);
    }



}
