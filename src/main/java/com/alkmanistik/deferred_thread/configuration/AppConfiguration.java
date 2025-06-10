package com.alkmanistik.deferred_thread.configuration;

import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    RetryPolicyParam initRetryParam(){
        return new RetryPolicyParam();
    }

    @Bean
    WorkerParams initWorkerParam(){
        return new WorkerParams();
    }

}
