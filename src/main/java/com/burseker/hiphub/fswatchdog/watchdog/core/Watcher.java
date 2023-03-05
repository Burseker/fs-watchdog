package com.burseker.hiphub.fswatchdog.watchdog.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

@Slf4j
public class Watcher {
    private final WatchService watchService;
    private final Path path;

    public Watcher(@NonNull String workingPath) throws IOException {
        path = Paths.get(Objects.requireNonNull(workingPath, "workingPath"));
        watchService = FileSystems.getDefault().newWatchService();
    }

    public void start() throws IOException, InterruptedException {
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                log.info(
                        "Event kind:" + event.kind()
                                + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }
    }

}
