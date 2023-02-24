package com.burseker.hiphub.fswatchdog.file_indexer;

import com.burseker.hiphub.fswatchdog.utils.MD5Utils;
import com.burseker.hiphub.fswatchdog.utils.UnhandledExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class Path2MetaInfoMapper {

    public static FileMetaInfo map(Path path){
        Assert.isTrue(Files.isRegularFile(path), "path isn't regular file");
        log.info("Maps file {} in FileMetaInfo", path);

        FileMetaInfo result = FileMetaInfo.builder()
                .name(path.toFile().getAbsolutePath())
                .hash(MD5Utils.checksum(path))
                .size(UnhandledExceptionWrapper.call(()->Files.size(path)))
                .creationTS(null)
                .build();

        log.info(result.toString());
        return result;
    }
}
