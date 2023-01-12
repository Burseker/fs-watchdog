package com.burseker.hiphub.fswatchdog.file_indexer;

import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;

public class Path2MetaInfoMapper {

    public FileMetaInfo map(Path path){
        Assert.isTrue(Files.isRegularFile(path), "path is not regular file");
        return null;
    }
}
