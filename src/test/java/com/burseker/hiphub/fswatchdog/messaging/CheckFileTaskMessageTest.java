package com.burseker.hiphub.fswatchdog.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CheckFileTaskMessageTest {
    ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void checkFileTaskMessageSerDesTest() throws JsonProcessingException {
        CheckFileTask task = new CheckFileTask()
            .withDescription("some description text")
            .withPath("C:/");
        //CheckFileTask task = CheckFileTask.builder().filePath(Path.of("C:/")).build();
        CheckFileTaskMessage message = CheckFileTaskMessage.builder()
            //.messageType(MessageType.TASK)
            .body(task)
            .build();


        log.info("Object under test {}", task);
        String longTaskMessageString = mapper.writeValueAsString(task);
        log.info("Serialized data {}", longTaskMessageString);
        assertEquals(task, mapper.readValue(longTaskMessageString, CheckFileTask.class));

        log.info("=====================");
        log.info("Object under test {}", message);
        longTaskMessageString = mapper.writeValueAsString(message);
        log.info("Serialized data {}", longTaskMessageString);
        assertEquals(message, mapper.readValue(longTaskMessageString, CheckFileTaskMessage.class));
    }
}
