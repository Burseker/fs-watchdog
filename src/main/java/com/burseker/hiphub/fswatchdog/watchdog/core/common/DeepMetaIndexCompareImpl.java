package com.burseker.hiphub.fswatchdog.watchdog.core.common;

import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.utils.FileDeepCompare;

import java.nio.file.Path;
import java.util.Objects;


public class DeepMetaIndexCompareImpl implements DeepMetaIndexCompare{
    @Override
    public boolean compare(FileMetaIndex fileIndex1, FileMetaIndex fileIndex2) {
        if(Objects.equals(fileIndex1.getId(), fileIndex2.getId())) return true;
        if(!Objects.equals(fileIndex1.getSize(), fileIndex2.getSize())) return false;
        return FileDeepCompare.isFilesEquals(Path.of(fileIndex1.getPath()), Path.of(fileIndex2.getPath()));
    }
}
