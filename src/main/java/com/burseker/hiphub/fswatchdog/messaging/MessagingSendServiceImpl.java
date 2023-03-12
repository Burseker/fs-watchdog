package com.burseker.hiphub.fswatchdog.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessagingSendServiceImpl implements MessagingSendService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void send(Message message) {
        log.info("Sending message={} to queue {}", message, message.getMessageType().toString());
        jmsTemplate.convertAndSend(message.getMessageType().toString(), message);
    }
}
