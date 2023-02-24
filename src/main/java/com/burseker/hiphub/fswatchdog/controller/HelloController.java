package com.burseker.hiphub.fswatchdog.controller;

import com.burseker.hiphub.fswatchdog.file_indexer.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.service.FileService;
import com.burseker.hiphub.fswatchdog.view.FileWithCopies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
