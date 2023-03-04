package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.file_indexer.FileIndexer;
import com.burseker.hiphub.fswatchdog.file_indexer.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.file_watcher.FileWatcher;
import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.view.FileWithCopies;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        new FileIndexer(repository, workingPath).index();
//        new FileIndexer(repository, Path.of("C:\\projects\\SOFT\\sandbox")).index();
//        new FileIndexer(repository, Path.of("C:\\projects\\SOFT\\personal")).index();

        fileCopyWalker.markCopyKeys();

        log.info("initialization of FileWatcher service");
        fileWatcher=new FileWatcher(this.workingPath);
        //fileWatcher.start();
    }

    public void walkForCopies(){
        fileCopyWalker.markRepeating();
    }

    public List<FileWithCopies> getAllFiles(){
        Iterable<FileMetaIndex> res = repository.findAll();
        List<FileWithCopies> result = new ArrayList<>();
        res.forEach(val-> result.add(FileWithCopies
                .builder()
                .name(val.getPath())
                .size(val.getSize())
                .hash(val.getMd5())
                .build()
        ));
        return result;
    }

    public Collection<FileWithCopies> getFilesWithCopies(){
        Iterable<FileMetaIndex> res = repository.findAllByCopyKeyNotNull();
        Map<String, FileWithCopies> result = new HashMap<>();
        res.forEach(val-> {
                String key = val.getMd5() + val.getSize() + "-" + val.getCopyKey();

                FileMetaInfo fileMetaInfo = FileMetaInfo
                        .builder()
                        .name(val.getPath())
                        .size(val.getSize())
                        .hash(val.getMd5())
                        .build();

                result.compute(key, (k, v) -> {
                        if (v == null)
                            return FileWithCopies.builder()
                                    .name(key)
                                    .hash(val.getMd5())
                                    .size(val.getSize())
                                    .copies(Stream.of(fileMetaInfo).collect(Collectors.toList()))
                                    .build();
                        v.getCopies().add(fileMetaInfo);
                        return v;
                    }
                );
            }
        );

        return result.values();
    }
}
