package com.burseker.hiphub.fswatchdog.messaging;

public interface Message<T>{
    MessageType getMessageType();
    T getBody();
}
