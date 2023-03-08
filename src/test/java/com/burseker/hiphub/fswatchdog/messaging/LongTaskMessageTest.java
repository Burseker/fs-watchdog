package com.burseker.hiphub.fswatchdog.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class LongTaskMessageTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void longTaskMessageMappingTest() throws JsonProcessingException {
        LongTaskMessage longTaskMessage = LongTaskMessage
            .builder()
            .id(10L)
            .body("some body text")
            .spendTime(10L)
            .build();

        log.info("Object under test {}", longTaskMessage);
        String longTaskMessageString = mapper.writeValueAsString(longTaskMessage);
        log.info("Serialized data {}", longTaskMessageString);
        assertEquals(longTaskMessage, mapper.readValue(longTaskMessageString, LongTaskMessage.class));
    }
}
