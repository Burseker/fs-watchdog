package com.burseker.hiphub.fswatchdog.file_indexer;

import com.burseker.hiphub.fswatchdog.persistant.converter.FileMetaInfo2NonUniqueFile;
import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.burseker.hiphub.fswatchdog.utils.PrinterUtils.listToString;
import static java.util.Objects.requireNonNull;

@Slf4j
public class FileIndexer {
    private final Path workingPath;
    private final FileMetaIndexRepository repository;


    public FileIndexer(@NonNull FileMetaIndexRepository repository,
                       @NonNull Path workingPath) {
        this.repository = requireNonNull(repository, "repository");
        this.workingPath = requireNonNull(workingPath, "workingPath");
        if(!Files.isDirectory(workingPath)) throw new IllegalArgumentException(String.format("workingPath=%s is not directory", workingPath));
    }

    public void index(){
        Set<Path> pathsSet = collectFilePaths();
        saveIfNotExist(pathsSet);
    }

    @SneakyThrows
    private Set<Path> collectFilePaths(){
        RegularFileVisitor fileVisitor = new RegularFileVisitor();
        Files.walkFileTree(workingPath, fileVisitor);
        return fileVisitor.getFileNamesSet();
    }

    private Iterable<FileMetaIndex> saveIfNotExist(Set<Path> paths){
        Set<Path> newPaths = new HashSet<>(paths);

        Iterable<FileMetaIndex> storedPaths = repository.findByPathStartingWith(workingPath.toFile().getAbsolutePath());
        storedPaths.forEach( v -> newPaths.remove(Path.of(v.getPath())) );

        Path2MetaInfoMapper mapper = new Path2MetaInfoMapper(false);
        List<FileMetaInfo> metaToStore = newPaths.stream().map(mapper::map).collect(Collectors.toList());
        log.trace(listToString(metaToStore.stream().map(FileMetaInfo::toString).collect(Collectors.toList())));

        return repository.saveAll(metaToStore.stream().map(FileMetaInfo2NonUniqueFile::convert).collect(Collectors.toList()));
    }
}
