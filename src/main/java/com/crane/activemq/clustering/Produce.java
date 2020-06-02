package com.crane.activemq.clustering;

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
 * @create: 2020-05-28 17:23
 **/
@SpringBootApplication
public class Produce {

    /**
     * 集群连接模板
     * @param brokerUrl
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    @Bean
    public JmsTemplate jmsTemplateClustering(@Value("${spring.activemq.broker-url-failover}") String brokerUrl, @Value("${spring.activemq.user}") String userName,
                                      @Value("${spring.activemq.password}") String password) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName,password,brokerUrl);
        return new JmsTemplate(connectionFactory);
    }

    @Autowired
    private JmsTemplate jmsTemplateClustering ;

    @PostConstruct
    private void init() {
        jmsTemplateClustering.convertAndSend("queueClustering","hello");

    }


    public static void main(String[] args) {
        SpringApplication.run(Produce.class);
    }
}
