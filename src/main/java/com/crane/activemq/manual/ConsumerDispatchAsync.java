package com.crane.activemq.manual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;

/**
 * @description: 异步
 * @author: crane
 * @create: 2020-05-29 11:20
 **/
/**
 * 消费者异步调度
 * 从ActiveMQ v4开始，消费者异步调度的配置更加灵活，可以在连接URI、Connection和ConnectionFactory上进行配置，而在以前的版本中，只能在broker服务器上配置。<br/>
 * 可以在broker的配置中，通过disableAsyncDispatch属性禁用transportConnector上的异步调度，禁用这个传输连接后，在客户端将无法开启。
 * <transportConnector name="openwire" uri="tcp://0.0.0.0:61616" disableAsyncDispatch="true"/>
 *
 * 通过这种灵活的配置，可以实现为较慢的消费者提供异步消息传递，而为较快的消费者提供同步消息传递。<br/>
 * 使用同步消息的缺点是：如果向较慢的消费者发送消息时，可能造成生产者阻塞。
 */
@SpringBootApplication
public class ConsumerDispatchAsync {
    @Autowired
    private JmsTemplate jmsTemplateQueue;

    @PostConstruct
    private void init() {
        jmsTemplateQueue.convertAndSend("queue1?consumer.dispatchAsync=false","hello queue1");
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerDispatchAsync.class);

    }
}
