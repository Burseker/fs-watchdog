package com.burseker.hiphub.fswatchdog.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

import static java.lang.Thread.sleep;

@Slf4j
@Component
public class MessagingReceiveService {

    @JmsListener(destination = "TASK")
    public void receiveTaskMessage(Message message) throws JMSException {
        log.info("Receive text message: " + message );

        try {
            log.info("go to sleep for {} seconds...", 5 );
            sleep(5*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Message{} waking from sleep", message );
    }
}
