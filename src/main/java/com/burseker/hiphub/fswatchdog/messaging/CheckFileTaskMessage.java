package com.burseker.hiphub.fswatchdog.messaging;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.burseker.hiphub.fswatchdog.messaging.MessageType.TASK;
@Builder
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = CheckFileTaskMessage.CheckFileTaskMessageBuilder.class)
public class CheckFileTaskMessage implements Message<CheckFileTask>{

    private final CheckFileTask body;

    @Override
    public MessageType getMessageType() {
        return TASK;
    }

    @Override
    public CheckFileTask getBody() {
        return body;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class CheckFileTaskMessageBuilder {}
}
