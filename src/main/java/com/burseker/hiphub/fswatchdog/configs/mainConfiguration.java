package com.burseker.hiphub.fswatchdog.configs;

import com.burseker.hiphub.fswatchdog.service.DeepMetaIndexCompare;
import com.burseker.hiphub.fswatchdog.service.DeepMetaIndexCompareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mainConfiguration {

    @Bean
    public DeepMetaIndexCompare deepMetaIndexCompare(){
        return new DeepMetaIndexCompareImpl();
    }
}
