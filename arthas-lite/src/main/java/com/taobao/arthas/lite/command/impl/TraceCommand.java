package com.taobao.arthas.lite.command.impl;

import com.taobao.arthas.lite.trace.MethodTracer;

import java.lang.instrument.Instrumentation;

/**
 * Trace command implementation
 * @author Arthas Team
 */
public class TraceCommand {
    private static Instrumentation instrumentation;
    
    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }
    
    public static void execute(String command) {
        if (instrumentation == null) {
            System.out.println("Instrumentation is not available. Please run with -javaagent option.");
            return;
        }
        
        // Parse command arguments
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            System.out.println("Usage: trace {class-pattern} {method-pattern}");
            return;
        }
        
        String classPattern = parts[1];
        String methodPattern = parts[2];
        
        System.out.println("Tracing method: " + classPattern + "." + methodPattern);
        
        // Install method tracer
        MethodTracer.install(instrumentation, classPattern, methodPattern);
    }
}
