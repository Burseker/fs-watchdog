package com.burseker.hiphub.fswatchdog.controller;

import com.burseker.hiphub.fswatchdog.file_indexer.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.service.FileService;
import com.burseker.hiphub.fswatchdog.view.FileWithCopies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    private FileService fileService;

    @GetMapping("get-version")
    String getVersion(){
        log.info("getVersion entry");
        log.debug("debug log string");
        return "hiphub version 0.0.1-SNAPSHOT";
    }

    @GetMapping("test-endpoint")
    String testEndpoint() throws UnsupportedEncodingException {
        log.info("testEndpoint(). entry");

        String testString = "Какие то символы на русском";
        log.info(testString);
        log.info(new BigInteger(1, testString.getBytes()).toString(16));
        String utf8EncodedString = new String(testString.getBytes(StandardCharsets.UTF_8));
        log.info(utf8EncodedString);
        log.info(new BigInteger(1, utf8EncodedString.getBytes()).toString(16));

        log.info("Кодировка по умолчанию {}", Charset.defaultCharset());
        //https://www.geeksforgeeks.org/how-to-get-and-set-default-character-encoding-or-charset-in-java/
        return testString;
    }

    @GetMapping("walk-for-copies")
    String walForCopies(){
        log.info("walForCopies(). entry");

        fileService.walkForCopies();

        return "someText";
    }

    @GetMapping("get-all-files")
    List<FileWithCopies> getAllFiles(){
        log.info("getAllFiles(). entry");
        return fileService.getAllFiles();
    }

    @GetMapping("get-files-with-copies")
    Collection<FileWithCopies> getFileWithCopies(){
        log.info("getAllFiles(). entry");
        return fileService.getFilesWithCopies();
    }
}
