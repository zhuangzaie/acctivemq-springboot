package com.crane.activemq.transport;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: crane
 * @create: 2020-05-28 16:44
 **/
@SpringBootApplication
public class SSL {


    /**
     * SSL 连接模板
     * @param brokerUrl
     * @param userName
     * @param password
     * @param trustStorePassword
     * @param trustStore
     * @return
     * @throws Exception
     */
    @Bean
    public JmsTemplate jmsTemplateSSL(@Value("${spring.activemq.broker-url-ssl}") String brokerUrl, @Value("${spring.activemq.user}") String userName,
                                        @Value("${spring.activemq.password}") String password,@Value("${javax.net.ssl.trustStorePassword}") String trustStorePassword,
                                        @Value("${javax.net.ssl.trustStore}") String trustStore) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory(brokerUrl);
        ((ActiveMQSslConnectionFactory) connectionFactory).setTrustStore(trustStore);
        ((ActiveMQSslConnectionFactory) connectionFactory).setTrustStorePassword(trustStorePassword);
        return new JmsTemplate(connectionFactory);
    }



    @Autowired
    JmsTemplate jmsTemplateSSL;

    @PostConstruct
    private void init() {
        jmsTemplateSSL.convertAndSend("queueSSL","hello SSL");
    }

    public static void main(String[] args) {
        SpringApplication.run(SSL.class);
    }
}
