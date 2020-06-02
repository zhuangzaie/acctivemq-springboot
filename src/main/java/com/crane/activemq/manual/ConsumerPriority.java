package com.crane.activemq.manual;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @description: 优先级
 * @author: crane
 * @create: 2020-05-29 11:31
 **/
/**
 * 优先级消费
 * <p>
 * ActiveMQ支持消费者优先级
 * 优先级值的范围是：0-127。最高优先级是127。默认优先级是0
 */
@SpringBootApplication
public class ConsumerPriority {


    /**
     * 独占消费
     * 独占消费是在队列层面使用，当某个队列有多个消费者开启独占模式后，此队列将会被第一个消费者占用，只有第一个消费者才能从这个队列获取消息<br/>
     * 未开启时，是多个消费者平均消费
     */
    //onsumer.exclusive=true  当低优先级的消费者开启了独占后，此队列的其它消费者的优先级再高都不会收到消息
    @JmsListener(destination = "queue1?consumer.priority=20&consumer.exclusive=true")
    public void receiveTopicFirst(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("priority=20 收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("priority=20 收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }

    @JmsListener(destination = "queue1?consumer.priority=127")
    public void receiveTopic(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            System.out.println("priority=0 收到文本消息：" + ((TextMessage) message).getText());
        } else if (message instanceof ActiveMQMapMessage) {
            System.out.println("priority=0 收到map消息：" + ((ActiveMQMapMessage) message).getContentMap());
        } else {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerPriority.class);

    }
}
