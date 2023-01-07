package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.file_indexer.FileIndexer;
import com.burseker.hiphub.fswatchdog.file_watcher.FileWatcher;
import com.burseker.hiphub.fswatchdog.persistant.daos.NonUniqueFileRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.NonUniqueFile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.burseker.hiphub.fswatchdog.utils.PrinterUtils.listToString;

@Slf4j
@Service
public class FileService {

    private static final String WORKING_PATH_STRING = "D:\\tmp\\testPath";

    private FileWatcher fileWatcher;

    @Autowired
    private NonUniqueFileRepository repository;

    @SneakyThrows
    @PostConstruct
    void init(){
        Path workingPath = Path.of(WORKING_PATH_STRING);
        if(!Files.isDirectory(workingPath)) throw new IllegalArgumentException("WORKING_PATH_STRING=D:\\tmp\\testPath is not directory");

        log.info("all files in directory");
        log.info(listToString(new FileIndexer(workingPath).listFiles()));

        repository.save(
             NonUniqueFile.builder()
                .path("some test path")
                .size(12323L)
                .md5("2342341341324")
                .build()
        );
        repository.save(
             NonUniqueFile.builder()
                .path("some test path")
                .size(12323L)
                .md5("2342341341324")
                .build()
        );
        repository.save(
             NonUniqueFile.builder()
                .path("some test path")
                .size(12323L)
                .md5("2342341341324")
                .build()
        );

        log.info("initialization of FileWatcher service");
        fileWatcher=new FileWatcher(WORKING_PATH_STRING);
        //fileWatcher.start();
    }
}
