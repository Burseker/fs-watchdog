package com.burseker.hiphub.fswatchdog.messaging;

public interface Message {
    Long getId();
    Long getSpendTime();
    String getBody();
}
