package com.burseker.hiphub.fswatchdog.file_indexer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.file.FileVisitResult.CONTINUE;

@Slf4j
public class RegularFileVisitor extends SimpleFileVisitor<Path> {

    private List<Path> fileMetaList = new ArrayList<>();

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        if (attr.isSymbolicLink()) {
            log.trace("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
            log.trace("Regular file: %s ", file);
            fileMetaList.add(file);
        } else {
            log.trace("Other: %s ", file);
        }
        log.trace("(" + attr.size() + "bytes)");
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        log.trace("Directory: %s%n", dir);
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        log.error("visitFileFailed()", exc);
        return CONTINUE;
    }

    void clearFileList(){
        fileMetaList.clear();
    }

    public List<Path> getFileMetaList() {
        return Collections.unmodifiableList(fileMetaList);
    }

    public Set<Path> getFileNamesSet() {
        return fileMetaList.stream().collect(Collectors.toUnmodifiableSet());
    }
}
