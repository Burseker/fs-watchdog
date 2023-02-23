package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileCopyWalker {
    @Autowired
    private FileMetaIndexRepository repository;

    public void markRepeating(){
        log.info("markRepeating(). entry point");

        Iterable<FileMetaIndex> res = repository.findAll();
        res.forEach(
            val -> {
                log.info("Element under test: {}", val.toString());
                Iterable<FileMetaIndex> same = repository.findByMd5AndSizeAndIdGreaterThan(val.getMd5(), val.getSize(), val.getId());
                same.forEach(
                        val2 -> log.info("    - same element: {}", val2.toString() )
                );
            }
        );
    }
}
