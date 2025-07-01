package com.alkmanistik.deferred_thread.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryPolicyParam {

    private double retryTime = Math.exp(1);
    private int retryCount;
    private Duration maxDelay = Duration.ofHours(10);

}
