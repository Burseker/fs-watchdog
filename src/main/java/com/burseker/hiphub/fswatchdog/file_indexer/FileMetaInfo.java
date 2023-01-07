package com.burseker.hiphub.fswatchdog.file_indexer;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class FileMetaInfo {
    private final String name;
    private final Long size;
    private final String hash;
    private final Instant creationTS;
}
