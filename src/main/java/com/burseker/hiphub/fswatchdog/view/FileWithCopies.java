package com.burseker.hiphub.fswatchdog.view;

import com.burseker.hiphub.fswatchdog.file_indexer.FileMetaInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileWithCopies {
    private final String name;
    private final Long size;
    private final String hash;
    private final List<FileMetaInfo> copies;
}
