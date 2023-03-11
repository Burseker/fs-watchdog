package com.burseker.hiphub.fswatchdog.messaging.test;

public interface Message {
    Long getId();
    Long getSpendTime();
    String getBody();
}
