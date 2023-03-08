package com.burseker.hiphub.fswatchdog.configs;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MessagingConfig {

    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public MessageConverter messageConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public BrokerService broker() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.setPersistent(false);
        brokerService.setDestinationPolicy(policyMap());
        return brokerService;
    }

    @Bean
    public PolicyMap policyMap(){
        PolicyMap destinationPoliciy = new PolicyMap();
        List<PolicyEntry> entries = new ArrayList<PolicyEntry>();
        PolicyEntry queueEntry = new PolicyEntry();
        queueEntry.setQueue(">");
        queueEntry.setStrictOrderDispatch(false);
        entries.add(queueEntry);
        destinationPoliciy.setPolicyEntries(entries);
        return destinationPoliciy;
    }
}
