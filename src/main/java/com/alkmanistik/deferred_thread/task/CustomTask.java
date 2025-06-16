package com.alkmanistik.deferred_thread.task;

import com.alkmanistik.deferred_thread.data.anotation.TaskParams;

import java.util.Map;

@TaskParams(required = {"firstParam", "secondParam"})
public class CustomTask extends Task {

    public CustomTask(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void execute(Map<String, Object> params) throws InterruptedException {
        // Некоторая логика
        // Пример работы с параметрами
        System.out.println(params.get("firstParam") + " " + params.get("secondParam"));
    }

}
