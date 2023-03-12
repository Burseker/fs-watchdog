package com.burseker.hiphub.fswatchdog.controller;

import com.burseker.hiphub.fswatchdog.messaging.CheckFileTask;
import com.burseker.hiphub.fswatchdog.messaging.CheckFileTaskMessage;
import com.burseker.hiphub.fswatchdog.messaging.MessagingSendService;
import com.burseker.hiphub.fswatchdog.service.FileService;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileCopies;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import java.util.Collection;

@Slf4j
@RestController
public class HelloController {

    private static long counter = 0L;

    @Autowired
    private MessagingSendService messagingSendService;

    @Autowired
    private FileService fileService;

    @GetMapping("get-version")
    String getVersion(){
        log.info("getVersion entry");
        log.debug("debug log string");
        return "hiphub version 0.0.1-SNAPSHOT";
    }

    @GetMapping("walk-for-copies")
    String walkForCopies(){
        log.info("walkForCopies(). entry");
        fileService.walkForCopies();
        return "copies searching done";
    }

    @GetMapping("get-all-files")
    Collection<FileMetaInfo> getAllFiles(){
        log.info("getAllFiles(). entry");
        return fileService.getAllFiles();
    }

    @GetMapping("get-files-with-copies")
    Collection<FileCopies> getFileWithCopies(){
        log.info("getFileWithCopies(). entry");
        return fileService.getFilesWithCopies();
    }

    @GetMapping("test-endpoint")
    String testEndpoint() throws UnsupportedEncodingException {
        log.info("testEndpoint(). entry");
        messagingSendService.send(CheckFileTaskMessage
            .builder()
            .body(
                new CheckFileTask()
                    .withDescription("some text for description, id={"+ (counter++) +"}")
                    .withPath("C:/"))
            .build());
        return "Какая то тестовая строка";
    }
}
