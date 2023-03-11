package com.burseker.hiphub.fswatchdog.messaging.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = LongTaskMessage.LongTaskMessageBuilder.class)
public class LongTaskMessage implements Message{
    private final Long id;
    private final Long spendTime;
    private final String body;

    @JsonPOJOBuilder(withPrefix="")
    public static class LongTaskMessageBuilder {}
}
