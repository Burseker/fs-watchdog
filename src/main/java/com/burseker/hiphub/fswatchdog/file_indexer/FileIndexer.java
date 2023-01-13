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
public class FileIndexer {
    private final Path workingPath;

    public FileIndexer(@NonNull Path workingPath) {
        this.workingPath = Objects.requireNonNull(workingPath, "workingPath");
    }

    @SneakyThrows
    public List<FileMetaInfo> listFiles(){
        RegularFileVisitor fileVisitor = new RegularFileVisitor();
        Files.walkFileTree(workingPath, fileVisitor);

        return fileVisitor
                .getFileMetaList()
                .stream()
                .map(Path2MetaInfoMapper::map)
                .collect(Collectors.toList());
    }
}
