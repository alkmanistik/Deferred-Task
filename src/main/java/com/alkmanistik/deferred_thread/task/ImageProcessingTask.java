package com.alkmanistik.deferred_thread.task;

import java.util.Map;

public class ImageProcessingTask extends Task {
    public ImageProcessingTask(String imagePath, int width, int height) {
        super(Map.of(
                "imagePath", imagePath,
                "width", width,
                "height", height
        ));
    }

    public ImageProcessingTask(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void execute(Map<String, Object> params) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Processing image: " + params.get("imagePath"));
    }
}