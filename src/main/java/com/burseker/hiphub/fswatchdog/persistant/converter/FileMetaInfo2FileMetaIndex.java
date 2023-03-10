package com.burseker.hiphub.fswatchdog.persistant.converter;


import com.burseker.hiphub.fswatchdog.watchdog.dto.FileMetaInfo;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import org.springframework.util.Assert;

public class FileMetaInfo2FileMetaIndex {
    public static FileMetaIndex convert(final FileMetaInfo fileMetaInfo){
        Assert.notNull(fileMetaInfo, "fileMetaInfo is null");
        return FileMetaIndex.builder()
                .path(fileMetaInfo.getName())
                .size(fileMetaInfo.getSize())
                .md5(fileMetaInfo.getHash())
                .build();
    }
}
