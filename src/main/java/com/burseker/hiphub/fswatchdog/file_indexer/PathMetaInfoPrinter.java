package com.burseker.hiphub.fswatchdog.file_indexer;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class PathMetaInfoPrinter {

    @SneakyThrows
    static String printPathInfo(Path path){
        if( Files.isRegularFile(path) )
        {
            byte[] data = Files.readAllBytes(path);
            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
            String checksum = new BigInteger(1, hash).toString(16);

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
