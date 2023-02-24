package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.utils.FileDeepCompare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;

@Slf4j
@Service
public class FileCopyWalker {
    @Autowired
    private FileMetaIndexRepository repository;

    public void markRepeating(){
        log.info("markRepeating(). entry point");

        Iterable<FileMetaIndex> res = repository.findAll();
        Map<Long, FileMetaIndex> corrected = new HashMap<>();
        res.forEach(
            val -> {

                if(!corrected.containsKey(val.getId())){
                    log.info("Element under test: {}", val);
                    Iterable<FileMetaIndex> same = repository.findByMd5AndSize(val.getMd5(), val.getSize());
                    same.forEach(
                        val2 -> {
                            if(!Objects.equals(val.getId(), val2.getId())) {
                                if(deepCompareFileMeta(val, val2)) {
                                    log.info("    - same element: {}", val2);
                                    val2.setMainFile(val);
                                    corrected.put(val2.getId(), val2);
                                }
                            }
                        }
                    );
                } else {
                    log.info("Element under test: {} is copy of entity id={}", val, corrected.get(val.getId()).getMainFile().getId());
                }
            }
        );

        repository.saveAll(corrected.values());
    }

    private boolean deepCompareFileMeta(FileMetaIndex file1, FileMetaIndex file2){
        if(!Objects.equals(file1.getSize(), file2.getSize())) return false;
        return FileDeepCompare.isFilesEquals(Path.of(file1.getPath()), Path.of(file2.getPath()));
    }
}
