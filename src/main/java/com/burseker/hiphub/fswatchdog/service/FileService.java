package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.watchdog.core.Watcher;
import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileCopies;
import com.burseker.hiphub.fswatchdog.watchdog.core.CopyWalker;
import com.burseker.hiphub.fswatchdog.watchdog.core.Indexer;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.watchdog.view.Monitor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
public class FileService {

    @Value("${watch.files.path}")
    private String workingPath;

    private Watcher watcher;

    @Autowired
    private FileMetaIndexRepository repository;

    @Autowired
    private CopyWalker copyWalker;

    private Monitor monitor;

    @SneakyThrows
    @PostConstruct
    void init(){

        new Indexer(repository, workingPath).index();
//        new Indexer(repository, "C:\\projects\\SOFT\\sandbox").index();
//        new Indexer(repository, "C:\\projects\\SOFT\\personal").index();

        copyWalker.markCopyKeys();

        monitor = new Monitor(repository);
        log.info("initialization of FileWatcher service");
        watcher =new Watcher(this.workingPath);
        //fileWatcher.start();
    }

    public void walkForCopies(){
        copyWalker.markCopyKeys();
    }

    public Collection<FileMetaInfo> getAllFiles(){
        return monitor.getAllFiles();
    }

    public Collection<FileCopies> getFilesWithCopies(){
        return monitor.getFilesWithCopies();
    }
}
