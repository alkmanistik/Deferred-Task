package com.alkmanistik.deferred_thread.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerParams {

    private int threadNumber;
    private int tasksNumber;

}
