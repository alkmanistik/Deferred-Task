package com.alkmanistik.deferred_thread.task;

import java.util.Map;

public class EmailProcessingTask  extends Task {
    public EmailProcessingTask(String email, String message) {
        super(Map.of(
                "email", email,
                "message", message
        ));
    }

    public EmailProcessingTask(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void execute(Map<String, Object> params) throws InterruptedException {
        Thread.sleep(1000);
        int randomValue = (int)(Math.random()*10);
        if(randomValue==0){
            throw new RuntimeException("Email could not be sent: " + params.get("email"));
        }
        System.out.println("Processing email: " + params.get("email"));
    }
}