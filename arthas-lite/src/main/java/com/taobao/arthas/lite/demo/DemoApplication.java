package com.taobao.arthas.lite.demo;

/**
 * Demo application for testing arthas-lite
 */
public class DemoApplication {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting arthas-lite demo application...");
        
        DemoService service = new DemoService();
        
        // Run some operations to trace
        while (true) {
            try {
                service.processData("test-data");
                service.calculateSomething(42);
                service.doWork();
                
                Thread.sleep(2000); // Wait 2 seconds
            } catch (Exception e) {
                System.err.println("Error in demo: " + e.getMessage());
            }
        }
    }
    
    /**
     * Demo service class with methods to trace
     */
    public static class DemoService {
        
        public String processData(String data) {
            System.out.println("Processing data: " + data);
            
            // Simulate some work
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            return "Processed: " + data;
        }
        
        public int calculateSomething(int value) {
            System.out.println("Calculating with value: " + value);
            
            // Simulate some calculation
            int result = value * 2 + 1;
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            return result;
        }
        
        public void doWork() {
            System.out.println("Doing some work...");
            
            // Simulate work
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            System.out.println("Work completed");
        }
    }
}