package com.alkmanistik.deferred_thread.task;

import com.alkmanistik.deferred_thread.model.anotation.TaskParams;

import java.util.Map;

@TaskParams(required = {"email", "message"})
public class EmailProcessingTask  extends Task {

    public EmailProcessingTask(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void execute(Map<String, Object> params) throws InterruptedException {
        Thread.sleep(2000);
        int randomValue = (int)(Math.random()*10);
        if(randomValue==0){
            throw new RuntimeException("Email could not be sent: " + params.get("email"));
        }
        System.out.println("Processing email:" + params.get("email") + " with message:" + params.get("message"));
    }
}