package com.alkmanistik.deferred_thread.configuration;

import com.alkmanistik.deferred_thread.configuration.properties.RetryProperties;
import com.alkmanistik.deferred_thread.configuration.properties.WorkerProperties;
import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({WorkerProperties.class, RetryProperties.class})
public class AppConfiguration {

    @Bean
    public RetryPolicyParam retryPolicyParam(RetryProperties retryProperties) {
        return new RetryPolicyParam(retryProperties.getRetryCount());
    }

    @Bean
    public WorkerParams workerParams(WorkerProperties workerProperties) {
        return new WorkerParams(
                workerProperties.getThreadNumber(),
                workerProperties.getTasksNumber()
        );
    }
}
