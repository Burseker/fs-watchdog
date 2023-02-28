package com.burseker.hiphub.fswatchdog.file_indexer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class FileIndexerV0 {
    private final Path workingPath;

    public FileIndexerV0(@NonNull Path workingPath) {
        this.workingPath = Objects.requireNonNull(workingPath, "workingPath");
    }

    @SneakyThrows
    public List<FileMetaInfo> listFiles(){
        RegularFileVisitor fileVisitor = new RegularFileVisitor();
        Files.walkFileTree(workingPath, fileVisitor);
        Path2MetaInfoMapper mapper = new Path2MetaInfoMapper(true);
        return fileVisitor
                .getFileMetaList()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
}
