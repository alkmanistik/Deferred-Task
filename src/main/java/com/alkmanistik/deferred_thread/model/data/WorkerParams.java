package com.alkmanistik.deferred_thread.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerParams {

    private String category;
    private int threadNumber;
    private int tasksNumber;

}
