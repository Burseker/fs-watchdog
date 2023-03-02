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

//        log.info("all files in directory");
//        List<FileMetaInfo> fileMetaInfoList = new FileIndexerV0(workingPath).listFiles();
//        listToString(fileMetaInfoList.stream().map(FileMetaInfo::toString).collect(Collectors.toList())));
//
//        Iterable<FileMetaIndex> res = repository.saveAll(fileMetaInfoList.stream().map(FileMetaInfo2NonUniqueFile::convert).collect(Collectors.toList()));
//
//        List<FileMetaIndex> some = new ArrayList<>();
//        res.forEach(v->{
//                v.setMainFile(v);
//                log.info(v.toString());
//            }
//        );
//        repository.saveAll(res);
        new FileIndexer(repository, workingPath).index();
        //new FileIndexer(repository, Path.of("C:\\projects\\SOFT\\sandbox")).index();
        //new FileIndexer(repository, Path.of("C:\\projects\\SOFT\\personal")).index();

        //TODO Если метод вызывается из этой точки, то алгоритм markRepeating не распознаёт файлы, которые уже
        //     являются копиями
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
        Iterable<FileMetaIndex> res = repository.findAll();
        Map<Long, FileWithCopies> result = new HashMap<>();
        res.forEach(val->{
            if(!Objects.equals(val.getId(), val.getMainFile().getId())){
                FileWithCopies file = result.computeIfAbsent(val.getMainFile().getId(), k -> FileWithCopies
                        .builder()
                        .name(val.getMainFile().getPath())
                        .size(val.getMainFile().getSize())
                        .hash(val.getMainFile().getMd5())
                        .copies(new ArrayList<>())
                        .build()
                );

                file.getCopies().add(
                    FileMetaInfo
                        .builder()
                        .name(val.getPath())
                        .size(val.getSize())
                        .hash(val.getMd5())
                        .build()
                );
            }
        });
        return result.values();
    }
}
