package com.burseker.hiphub.fswatchdog.watchdog.view;

import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileCopies;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileMetaInfo;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Monitor {
    private final FileMetaIndexRepository repository;

    public Monitor(@NonNull FileMetaIndexRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public Collection<FileMetaInfo> getAllFiles(){
        return StreamSupport
            .stream(repository.findAll().spliterator(), false)
            .map(FileMetaIndex2FileMetaInfo::convert).toList();
    }

    public Collection<FileCopies> getFilesWithCopies(){
        Iterable<FileMetaIndex> res = repository.findAllByCopyKeyNotNull();
        Map<String, FileCopies> result = new HashMap<>();
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
                            return FileCopies.builder()
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
