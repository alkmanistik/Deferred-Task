package com.alkmanistik.deferred_thread.task;

import com.alkmanistik.deferred_thread.data.anotation.TaskParams;

import java.util.Map;

@TaskParams(required = {"imagePath", "width", "height"})
public class ImageProcessingTask extends Task {
    public ImageProcessingTask(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void execute(Map<String, Object> params) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Image path: " + params.get("imagePath") + " Size: " + params.get("width") + " width " + params.get("height") + " height");
    }
}