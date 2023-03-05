package com.burseker.hiphub.fswatchdog.watchdog.core;

import com.burseker.hiphub.fswatchdog.watchdog.core.common.Path2MetaIndexConverter;
import com.burseker.hiphub.fswatchdog.watchdog.core.common.RegularFileVisitor;
import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.burseker.hiphub.fswatchdog.utils.PrinterUtils.listToString;
import static java.util.Objects.requireNonNull;

@Slf4j
public class Indexer {
    private final FileMetaIndexRepository repository;
    private final Path workingPath;

    public Indexer(@NonNull FileMetaIndexRepository repository,
                   @NonNull String pathToIndex) {
        this.repository = requireNonNull(repository, "repository");
        this.workingPath = Path.of(requireNonNull(pathToIndex, "pathToIndex"));
        if(!Files.isDirectory(workingPath)) throw new IllegalArgumentException(String.format("workingPath=%s is not directory", workingPath));
    }

    public void index(){
        Set<Path> pathsSet = collectFilePaths();
        List<FileMetaIndex> ptintableList = new ArrayList<>();
        saveIfNotExist(pathsSet).forEach(ptintableList::add);
        log.info(listToString(ptintableList.stream().map(FileMetaIndex::toString).collect(Collectors.toList())));
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

        Path2MetaIndexConverter mapper = new Path2MetaIndexConverter(true);
        List<FileMetaIndex> metaToStore = newPaths.stream().map(mapper::map).toList();
        log.trace(listToString(metaToStore));

        return repository.saveAll(metaToStore);
    }
}
