package com.pawban.communicator_frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Configuration
public class CoreConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor() {
//        return new ScheduledAnnotationBeanPostProcessor();
//    }

}
