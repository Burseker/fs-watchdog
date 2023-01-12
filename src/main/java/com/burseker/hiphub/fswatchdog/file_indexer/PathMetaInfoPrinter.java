package com.burseker.hiphub.fswatchdog.file_indexer;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

import static com.burseker.hiphub.fswatchdog.utils.MD5Utils.checksum;

public class PathMetaInfoPrinter {

    @SneakyThrows
    static String printPathInfo(Path path){
        if( Files.isRegularFile(path) )
        {
            String checksum = checksum(path);
            return FileMetaInfo.builder()
                    .name(path.toString())
                    .size(Files.size(path))
                    .hash(checksum)
                    .build()
                    .toString();
        }

        return "unsupported path";
    }
}
