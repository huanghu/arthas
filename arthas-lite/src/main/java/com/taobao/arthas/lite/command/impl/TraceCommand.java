package com.taobao.arthas.lite.command.impl;

import com.taobao.arthas.core.shell.command.AnnotatedCommand;
import com.taobao.arthas.core.shell.command.CommandProcess;
import com.taobao.arthas.core.shell.command.annotation.Argument;
import com.taobao.arthas.core.shell.command.annotation.Name;
import com.taobao.arthas.core.shell.command.annotation.Summary;
import com.taobao.arthas.lite.trace.MethodTracer;
import com.taobao.arthas.lite.trace.TraceTransformer;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.regex.Pattern;

/**
 * Trace command for method execution tracing
 */
@Name("trace")
@Summary("Trace method execution time")
public class TraceCommand extends AnnotatedCommand {
    
    private String classPattern;
    private String methodPattern;
    
    @Argument(argName = "class-pattern", index = 0, required = true)
    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }
    
    @Argument(argName = "method-pattern", index = 1, required = true)
    public void setMethodPattern(String methodPattern) {
        this.methodPattern = methodPattern;
    }
    
    @Override
    public void process(CommandProcess process) {
        try {
            // Get instrumentation from MBean server
            Instrumentation instrumentation = getInstrumentation();
            
            if (instrumentation == null) {
                process.write("Error: Instrumentation not available\\n");
                process.end();
                return;
            }
            
            // Compile patterns
            Pattern classRegex = Pattern.compile(classPattern.replace("*", ".*"));
            Pattern methodRegex = Pattern.compile(methodPattern.replace("*", ".*"));
            
            // Create and register transformer
            TraceTransformer transformer = new TraceTransformer(classRegex, methodRegex);
            instrumentation.addTransformer(transformer, true);
            
            // Retransform classes
            retransformClasses(instrumentation, classRegex);
            
            process.write("Tracing enabled for: " + classPattern + "." + methodPattern + "\\n");
            process.write("Method calls will be traced with execution time\\n");
            process.write("Press Ctrl+C to stop tracing\\n");
            
            // Keep the process running
            Thread.sleep(Long.MAX_VALUE);
            
        } catch (InterruptedException e) {
            process.write("Tracing stopped\\n");
        } catch (Exception e) {
            process.write("Error: " + e.getMessage() + "\\n");
        } finally {
            process.end();
        }
    }
    
    private Instrumentation getInstrumentation() {
        try {
            // Try to get instrumentation from ArthasBootstrap
            return com.taobao.arthas.core.server.ArthasBootstrap.getInstance().getInstrumentation();
        } catch (Exception e) {
            process.write("Warning: Could not get instrumentation from ArthasBootstrap\\n");
            return null;
        }
    }
    
    private void retransformClasses(Instrumentation instrumentation, Pattern classPattern) {
        Class<?>[] loadedClasses = instrumentation.getAllLoadedClasses();
        for (Class<?> clazz : loadedClasses) {
            if (classPattern.matcher(clazz.getName()).matches()) {
                try {
                    if (instrumentation.isModifiableClass(clazz)) {
                        instrumentation.retransformClasses(clazz);
                    }
                } catch (Exception e) {
                    // Ignore retransformation errors for individual classes
                }
            }
        }
    }
}