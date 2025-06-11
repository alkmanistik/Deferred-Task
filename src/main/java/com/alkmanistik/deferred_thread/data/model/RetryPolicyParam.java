package com.alkmanistik.deferred_thread.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryPolicyParam {

    private int retryCount;

}
