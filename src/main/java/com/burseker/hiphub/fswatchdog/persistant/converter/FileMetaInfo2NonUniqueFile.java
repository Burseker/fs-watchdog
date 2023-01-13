package com.burseker.hiphub.fswatchdog.persistant.converter;


import com.burseker.hiphub.fswatchdog.file_indexer.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import org.springframework.util.Assert;

public class FileMetaInfo2NonUniqueFile {
    public static FileMetaIndex convert(final FileMetaInfo fileMetaInfo){
        Assert.notNull(fileMetaInfo, "fileMetaInfo is null");
        return FileMetaIndex.builder()
                .path(fileMetaInfo.getName())
                .size(fileMetaInfo.getSize())
                .md5(fileMetaInfo.getHash())
                .build();
    }
}
