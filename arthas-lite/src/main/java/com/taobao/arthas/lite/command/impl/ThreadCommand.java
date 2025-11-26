package com.taobao.arthas.lite.command.impl;

import com.taobao.arthas.core.shell.command.AnnotatedCommand;
import com.taobao.arthas.core.shell.command.CommandProcess;
import com.taobao.arthas.core.shell.command.annotation.Argument;
import com.taobao.arthas.core.shell.command.annotation.Name;
import com.taobao.arthas.core.shell.command.annotation.Summary;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

/**
 * Thread command for printing thread stacks
 */
@Name("thread")
@Summary("Display thread information")
public class ThreadCommand extends AnnotatedCommand {
    
    private String id;
    
    @Argument(argName = "id", required = false)
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public void process(CommandProcess process) {
        try {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            
            if (id == null || id.isEmpty()) {
                // Show all threads
                showAllThreads(process, threadMXBean);
            } else {
                // Show specific thread
                showThreadById(process, threadMXBean, id);
            }
            
        } catch (Exception e) {
            process.write("Error: " + e.getMessage() + "\\n");
        } finally {
            process.end();
        }
    }
    
    private void showAllThreads(CommandProcess process, ThreadMXBean threadMXBean) {
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        
        process.write("Threads: " + allThreads.size() + "\\n");
        process.write("===============================\\n");
        
        for (Map.Entry<Thread, StackTraceElement[]> entry : allThreads.entrySet()) {
            Thread thread = entry.getKey();
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(thread.getId());
            
            if (threadInfo != null) {
                process.write(String.format("%d %s %s %s\\n", 
                    thread.getId(), 
                    thread.getName(), 
                    thread.getState(), 
                    threadInfo.isSuspended() ? "[suspended]" : ""));
            }
        }
    }
    
    private void showThreadById(CommandProcess process, ThreadMXBean threadMXBean, String id) {
        try {
            long threadId = Long.parseLong(id);
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId, Integer.MAX_VALUE);
            
            if (threadInfo == null) {
                process.write("Thread not found: " + id + "\\n");
                return;
            }
            
            process.write("Thread " + threadId + " (" + threadInfo.getThreadName() + "): " + threadInfo.getThreadState() + "\\n");
            process.write("===============================\\n");
            
            StackTraceElement[] stackTrace = threadInfo.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                StackTraceElement element = stackTrace[i];
                process.write(String.format("%d. %s.%s(%s:%d)\\n", 
                    i, 
                    element.getClassName(), 
                    element.getMethodName(), 
                    element.getFileName(), 
                    element.getLineNumber()));
            }
            
        } catch (NumberFormatException e) {
            process.write("Invalid thread ID: " + id + "\\n");
        }
    }
}