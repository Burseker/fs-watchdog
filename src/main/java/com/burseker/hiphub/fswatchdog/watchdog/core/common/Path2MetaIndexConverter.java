package com.burseker.hiphub.fswatchdog.watchdog.core.common;

import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.utils.MD5Utils;
import com.burseker.hiphub.fswatchdog.utils.UnhandledExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex.NULL_HASH;

@Slf4j
public class Path2MetaIndexConverter {
    private final boolean calculateChecksum;
    public Path2MetaIndexConverter(boolean calculateChecksum) {
        this.calculateChecksum = calculateChecksum;
    }

    public FileMetaIndex map(Path path){
        Assert.isTrue(Files.isRegularFile(path), "path isn't regular file");
        log.trace("Maps file {} in FileMetaInfo", path);

        FileMetaIndex result = FileMetaIndex.builder()
            .path(path.toFile().getAbsolutePath())
            .md5(calculateChecksum ? MD5Utils.checksum(path) : NULL_HASH)
            .size(UnhandledExceptionWrapper.call(()->Files.size(path)))
            .build();

        log.trace("Return={}",result.toString());
        return result;
    }
}
