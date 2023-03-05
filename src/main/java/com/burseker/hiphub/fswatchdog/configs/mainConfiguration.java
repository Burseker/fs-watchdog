package com.burseker.hiphub.fswatchdog.configs;

import com.burseker.hiphub.fswatchdog.watchdog.core.common.DeepMetaIndexCompare;
import com.burseker.hiphub.fswatchdog.watchdog.core.common.DeepMetaIndexCompareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mainConfiguration {

    @Bean
    public DeepMetaIndexCompare deepMetaIndexCompare(){
        return new DeepMetaIndexCompareImpl();
    }
}
