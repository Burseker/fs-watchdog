package com.burseker.hiphub.fswatchdog.file_watcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

@Slf4j
public class FileWatcher {
    private final String workingPath;
    private final WatchService watchService;
    private final Path path;

    public FileWatcher(@NonNull String workingPath) throws IOException {
        this.workingPath =Objects.requireNonNull(workingPath, "workingPath");
        watchService = FileSystems.getDefault().newWatchService();
        path = Paths.get(this.workingPath);
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
