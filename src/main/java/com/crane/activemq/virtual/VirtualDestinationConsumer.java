package com.crane.activemq.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;

/**
 * @description: 虚拟目标
 * @author: crane
 * @create: 2020-06-02 10:34
 **/
@SpringBootApplication
public class VirtualDestinationConsumer {
    @Autowired
    private JmsTemplate jmsTemplatePublish;
    int a = 0 ;
    int b = 0 ;
    int c = 0 ;
    int d = 0 ;

    @JmsListener(destination = "Consumer.A.VirtualTopic.A" ,containerFactory = "jmsListenerContainerFactoryQueue")
    private void receive(String message) {
        a++;
        System.out.println(a + " " + b + " " + c + " " + d);
    }

    @JmsListener(destination = "Consumer.A.VirtualTopic.A" ,containerFactory = "jmsListenerContainerFactoryQueue")
    private void receive1(String message) {
        b++;
        System.out.println(a + " " + b + " " + c + " " + d);
    }

    @JmsListener(destination = "Consumer.B.VirtualTopic.A",containerFactory = "jmsListenerContainerFactoryQueue")
    private void receive3(String message) {
        c++;
        System.out.println(a + " " + b + " " + c + " " + d);

    }

    @JmsListener(destination = "Consumer.B.VirtualTopic.A",containerFactory = "jmsListenerContainerFactoryQueue")
    private void receive4(String message) {
        d++;
        System.out.println(a + " " + b + " " + c + " " + d);
    }




    @PostConstruct
    private void init() throws InterruptedException {
        for(int i=0;i<400;i++){
            jmsTemplatePublish.convertAndSend("VirtualTopic.A","hello");
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(VirtualDestinationConsumer.class);
    }
}
