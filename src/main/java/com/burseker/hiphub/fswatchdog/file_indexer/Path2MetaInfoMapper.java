package com.burseker.hiphub.fswatchdog.file_indexer;

import com.burseker.hiphub.fswatchdog.utils.MD5Utils;
import com.burseker.hiphub.fswatchdog.utils.UnhandledExceptionWrapper;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;

public class Path2MetaInfoMapper {

    public static FileMetaInfo map(Path path){
        Assert.isTrue(Files.isRegularFile(path), "path isn't regular file");
        return FileMetaInfo.builder()
                .name(path.toFile().getAbsolutePath().toString())
                .hash(MD5Utils.checksum(path))
                .size(UnhandledExceptionWrapper.call(()->Files.size(path)))
                .creationTS(null)
                .build();
    }
}
