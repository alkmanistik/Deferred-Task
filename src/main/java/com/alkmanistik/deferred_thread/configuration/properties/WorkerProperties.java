package com.alkmanistik.deferred_thread.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "task.worker")
@Data
public class WorkerProperties {
    private int threadNumber;
    private int tasksNumber;
}
