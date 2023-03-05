package com.burseker.hiphub.fswatchdog.watchdog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileCopies {
    private final String name;
    private final Long size;
    private final String hash;
    private final List<FileMetaInfo> copies;
}
