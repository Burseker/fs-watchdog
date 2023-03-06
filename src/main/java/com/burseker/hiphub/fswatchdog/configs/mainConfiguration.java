package com.burseker.hiphub.fswatchdog.configs;

import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.watchdog.core.CopyWalker;
import com.burseker.hiphub.fswatchdog.watchdog.core.Watcher;
import com.burseker.hiphub.fswatchdog.watchdog.core.common.DeepMetaIndexCompare;
import com.burseker.hiphub.fswatchdog.watchdog.core.common.DeepMetaIndexCompareImpl;
import com.burseker.hiphub.fswatchdog.watchdog.view.Monitor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mainConfiguration {
    @Value("${watch.files.path}")
    private String workingPath;

    @Autowired
    private FileMetaIndexRepository repository;

    @Bean
    public DeepMetaIndexCompare deepMetaIndexCompare(){
        return new DeepMetaIndexCompareImpl();
    }

    @Bean
    public CopyWalker copyWalker(){
        return new CopyWalker(repository, deepMetaIndexCompare());
    }

    @SneakyThrows
    @Bean
    public Watcher watcher(){
        return new Watcher(workingPath);
    }

    @Bean
    public Monitor monitor(){
        return new Monitor(repository);
    }
}
