package com.pavel.jogger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    /**
     * Creates a specific task executor for handling Badge-related operations.
     * <p>
     * When a method is annotated with @Async("badgeExecutor"), it will be executed
     * using a thread from this specific pool. This isolates badge processing from
     * other application parts.
     * </p>
     *
     * @return The configured Executor specifically for badge tasks.
     */
    @Bean(name = "badgeExecutor")
    public Executor badgeExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("BadgeThread-");
        executor.initialize();
        return executor;
    }
}
