package com.burseker.hiphub.fswatchdog.watchdog.core.common;

import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;

import java.io.IOException;

public interface DeepMetaIndexCompare {
    boolean compare(FileMetaIndex fileIndex1, FileMetaIndex fileIndex2) throws IOException;
}
