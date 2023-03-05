package com.burseker.hiphub.fswatchdog.watchdog.view;

import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.watchdog.dto.FileMetaInfo;
import org.springframework.util.Assert;

public class FileMetaIndex2FileMetaInfo {
    public static FileMetaInfo convert(final FileMetaIndex fileMetaIndex){
        Assert.notNull(fileMetaIndex, "fileMetaIndex is null");
        return FileMetaInfo.builder()
            .name(fileMetaIndex.getPath())
            .size(fileMetaIndex.getSize())
            .hash(fileMetaIndex.getMd5())
            .build();
    }
}
