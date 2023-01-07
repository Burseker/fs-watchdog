package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.file_indexer.FileIndexer;
import com.burseker.hiphub.fswatchdog.file_watcher.FileWatcher;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static com.burseker.hiphub.fswatchdog.utils.PrinterUtils.listToString;

@Slf4j
@Service
public class FileService {

    private static final String WORKING_PATH_STRING = "D:\\tmp\\testPath";

    private FileWatcher fileWatcher;

    @SneakyThrows
    @PostConstruct
    void init(){
        Path workingPath = Path.of(WORKING_PATH_STRING);
        if(!Files.isDirectory(workingPath)) throw new IllegalArgumentException("WORKING_PATH_STRING=D:\\tmp\\testPath is not directory");

        log.info("all files in directory");
        log.info(listToString(new FileIndexer(workingPath).listFiles()));

        log.info("initialization of FileWatcher service");
        fileWatcher=new FileWatcher(WORKING_PATH_STRING);
        fileWatcher.start();
    }
}
