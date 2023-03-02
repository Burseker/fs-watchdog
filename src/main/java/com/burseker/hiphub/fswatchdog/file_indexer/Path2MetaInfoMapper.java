package com.burseker.hiphub.fswatchdog.file_indexer;

import com.burseker.hiphub.fswatchdog.utils.MD5Utils;
import com.burseker.hiphub.fswatchdog.utils.UnhandledExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex.NULL_HASH;

@Slf4j
public class Path2MetaInfoMapper {

    private final boolean calculateChecksum;

    public Path2MetaInfoMapper(boolean calculateChecksum) {
        this.calculateChecksum = calculateChecksum;
    }

    public FileMetaInfo map(Path path){
        Assert.isTrue(Files.isRegularFile(path), "path isn't regular file");
        log.trace("Maps file {} in FileMetaInfo", path);

        FileMetaInfo result = FileMetaInfo.builder()
                .name(path.toFile().getAbsolutePath())
                .hash(calculateChecksum ? MD5Utils.checksum(path) : NULL_HASH)
                .size(UnhandledExceptionWrapper.call(()->Files.size(path)))
                .creationTS(null)
                .build();

        log.debug("FileMetaInfo.map={}",result.toString());
        return result;
    }
}
