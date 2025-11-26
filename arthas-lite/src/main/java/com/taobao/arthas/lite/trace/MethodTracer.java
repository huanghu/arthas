package com.taobao.arthas.lite.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Method tracer for tracking execution time
 */
public class MethodTracer {
    private static final Logger logger = LoggerFactory.getLogger(MethodTracer.class);
    
    private static final ConcurrentHashMap<String, MethodStats> methodStats = new ConcurrentHashMap<>();
    
    public static class MethodStats {
        private final AtomicLong callCount = new AtomicLong(0);
        private final AtomicLong totalTime = new AtomicLong(0);
        private volatile long maxTime = 0;
        private volatile long minTime = Long.MAX_VALUE;
        
        public void recordCall(long executionTime) {
            callCount.incrementAndGet();
            totalTime.addAndGet(executionTime);
            
            synchronized (this) {
                if (executionTime > maxTime) {
                    maxTime = executionTime;
                }
                if (executionTime < minTime) {
                    minTime = executionTime;
                }
            }
        }
        
        public long getCallCount() {
            return callCount.get();
        }
        
        public long getTotalTime() {
            return totalTime.get();
        }
        
        public long getAverageTime() {
            long count = callCount.get();
            return count > 0 ? totalTime.get() / count : 0;
        }
        
        public long getMaxTime() {
            return maxTime;
        }
        
        public long getMinTime() {
            return minTime == Long.MAX_VALUE ? 0 : minTime;
        }
        
        @Override
        public String toString() {
            return String.format("calls=%d, total=%dms, avg=%dms, min=%dms, max=%dms",
                getCallCount(), getTotalTime(), getAverageTime(), getMinTime(), getMaxTime());
        }
    }
    
    /**
     * Called by instrumented methods to trace execution
     */
    public static Object traceMethodCall(Object instance, String methodName, Object[] args, 
                                       String className, Method method, Object... methodArgs) throws Throwable {
        
        String methodKey = className + "." + methodName;
        long startTime = System.currentTimeMillis();
        
        try {
            // Execute the original method
            return method.invoke(instance, methodArgs);
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // Record statistics
            MethodStats stats = methodStats.computeIfAbsent(methodKey, k -> new MethodStats());
            stats.recordCall(executionTime);
            
            // Log the trace information
            logger.info("TRACE {}.{}() - {}ms", className, methodName, executionTime);
            
            // Print to console if System.out is available
            System.out.printf("TRACE %s.%s() - %dms%n", className, methodName, executionTime);
        }
    }
    
    /**
     * Get statistics for a method
     */
    public static MethodStats getMethodStats(String methodKey) {
        return methodStats.get(methodKey);
    }
    
    /**
     * Get all method statistics
     */
    public static ConcurrentHashMap<String, MethodStats> getAllMethodStats() {
        return new ConcurrentHashMap<>(methodStats);
    }
    
    /**
     * Clear all statistics
     */
    public static void clearStats() {
        methodStats.clear();
    }
}