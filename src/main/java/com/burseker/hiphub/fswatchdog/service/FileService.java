package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.file_indexer.FileIndexer;
import com.burseker.hiphub.fswatchdog.file_indexer.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.file_watcher.FileWatcher;
import com.burseker.hiphub.fswatchdog.persistant.converter.FileMetaInfo2NonUniqueFile;
import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.burseker.hiphub.fswatchdog.utils.PrinterUtils.listToString;

@Slf4j
@Service
public class FileService {

    @Value("${watch.files.path}")
    private String workingPath;

    private FileWatcher fileWatcher;

    @Autowired
    private FileMetaIndexRepository repository;

    @Autowired
    private FileCopyWalker fileCopyWalker;

    @SneakyThrows
    @PostConstruct
    void init(){
        Path workingPath = Path.of(this.workingPath);
        if(!Files.isDirectory(workingPath)) throw new IllegalArgumentException(String.format("workingPath=%s is not directory", workingPath));

        log.info("all files in directory");
        List<FileMetaInfo> fileMetaInfoList = new FileIndexer(workingPath).listFiles();
        log.info(listToString(fileMetaInfoList.stream().map(FileMetaInfo::toString).collect(Collectors.toList())));

        Iterable<FileMetaIndex> res = repository.saveAll(fileMetaInfoList.stream().map(FileMetaInfo2NonUniqueFile::convert).collect(Collectors.toList()));

        List<FileMetaIndex> some = new ArrayList<>();
        res.forEach(v->{
                v.setMainFile(v);
                log.info(v.toString());
            }
        );
        repository.saveAll(res);

        log.info("initialization of FileWatcher service");
        fileWatcher=new FileWatcher(this.workingPath);
        //fileWatcher.start();
    }

    public void walkForCopies(){
        fileCopyWalker.markRepeating();
    }
}
