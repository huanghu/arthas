package com.taobao.arthas.lite.demo;

/**
 * Demo application to test arthas-lite functionality
 * @author Arthas Team
 */
public class DemoApplication {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Demo application started!");
        
        // Create a thread to simulate work
        Thread workerThread = new Thread(() -> {
            while (true) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "WorkerThread");
        workerThread.start();
        
        // Keep the application running
        while (true) {
            Thread.sleep(1000);
        }
    }
    
    public static void doWork() {
        // Simulate some work
        try {
            Thread.sleep(500);
            String result = processData("test data");
            System.out.println("Processed result: " + result);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }
    
    public static String processData(String data) {
        // Simulate data processing
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }
}
