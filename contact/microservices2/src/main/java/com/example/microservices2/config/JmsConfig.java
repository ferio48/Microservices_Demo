package com.example.microservices2.config;

import jakarta.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;


@Configuration
@EnableJms
public class JmsConfig {

//    String brokerUrl = "tcp://localhost:61616";

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory =
                new DefaultJmsListenerContainerFactory();

        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setConcurrency("5-10");
        return jmsListenerContainerFactory;
    }

//    @Bean
//    public ActiveMQConnectionFactory connectionFactory() throws JMSException {
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//        factory.setBrokerURL(brokerUrl);
//        return factory;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() throws JMSException {
//        JmsTemplate jmsTemplate = new JmsTemplate((ConnectionFactory) connectionFactory());
//        // You can customize JmsTemplate properties here if needed
//        return jmsTemplate;
//    }
}
