package com.burseker.hiphub.fswatchdog.controller;

import com.burseker.hiphub.fswatchdog.service.FileService;
import com.burseker.hiphub.fswatchdog.view.FileWithCopies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

        // Setting the file encoding explicitly
        // to a new value
        //System.setProperty("file.encoding", "UTF-8");

        //рабочий метод для IDEA, не очень хороший способ, надо еще копать
        //https://youtrack.jetbrains.com/issue/IDEA-276155/Unable-to-change-gradle-build-output-encoding

        log.info("Кодировка по умолчанию Charset.defaultCharset(): {}", Charset.defaultCharset());
        log.info("Кодировка по умолчанию System.getProperty(\"file.encoding\"){}", System.getProperty("file.encoding"));
        log.info("Кодировка по умолчанию getCharacterEncoding(){}", getCharacterEncoding());
        //https://www.geeksforgeeks.org/how-to-get-and-set-default-character-encoding-or-charset-in-java/
        return testString;
    }

    public static String getCharacterEncoding()
    {

        // Creating and initializing byte array
        // with some random character say it be N

        // Here N = w
        byte[] byte_array = { 'w' };

        // Creating an object of inputStream
        InputStream instream
                = new ByteArrayInputStream(byte_array);

        // Now, opening new file input stream reader
        InputStreamReader streamreader
                = new InputStreamReader(instream);

        String defaultCharset = streamreader.getEncoding();

        // Returning the default character encoded
        // Here it is for N = 'w'
        return defaultCharset;
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
