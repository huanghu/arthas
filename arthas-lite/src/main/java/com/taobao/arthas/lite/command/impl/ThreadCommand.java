package com.taobao.arthas.lite.command.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Thread command implementation
 * @author Arthas Team
 */
public class ThreadCommand {
    public static void execute(String command) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // Get all thread IDs
        long[] threadIds = threadMXBean.getAllThreadIds();
        
        if (threadIds.length == 0) {
            System.out.println("No threads found.");
            return;
        }
        
        // Get detailed thread information
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds, Integer.MAX_VALUE);
        
        System.out.println("Thread dump:");
        System.out.println("Total threads: " + threadIds.length);
        System.out.println("\n" + repeat("=", 100));
        
        for (ThreadInfo threadInfo : threadInfos) {
            if (threadInfo == null) {
                continue;
            }
            
            System.out.println("\nThread ID: " + threadInfo.getThreadId());
            System.out.println("Thread Name: " + threadInfo.getThreadName());
            System.out.println("Thread State: " + threadInfo.getThreadState());
            System.out.println("Stack Trace:");
            
            for (StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
                System.out.println("  " + stackTraceElement.toString());
            }
            
            System.out.println("\n" + repeat("-", 100));
        }
    }
    
    /**
     * Repeat a string multiple times (compatible with Java 8)
     * @param str the string to repeat
     * @param times the number of times to repeat
     * @return the repeated string
     */
    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
