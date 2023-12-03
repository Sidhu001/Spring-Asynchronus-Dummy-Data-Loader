package com.generatedummydata.SpringDummyDataProject.config;

import com.generatedummydata.SpringDummyDataProject.service.MerchantServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootConfiguration
@EnableAsync
public class AsyncConfig {

    public static final Logger logger = LogManager.getLogger(MerchantServiceImpl.class);

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("userThread-");
        taskExecutor.setRejectedExecutionHandler((r, executor1) -> logger.warn("Task rejected, thread pool is full and queue is also full"));
        taskExecutor.initialize();
        return taskExecutor;
    }
}
