package com.burseker.hiphub.fswatchdog.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProducerService implements MessagingService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void send(Message message) {
        log.info("Sending message={} to queue {}", message, "TEST_QUEUE");
        jmsTemplate.convertAndSend("TEST_QUEUE", message);
    }
}
