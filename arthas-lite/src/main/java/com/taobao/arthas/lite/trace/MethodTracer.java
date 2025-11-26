package com.taobao.arthas.lite.trace;

import java.lang.instrument.Instrumentation;

/**
 * Method tracer for tracking method execution time
 * @author Arthas Team
 */
public class MethodTracer {
    public static void install(Instrumentation instrumentation, String classPattern, String methodPattern) {
        instrumentation.addTransformer(new TraceTransformer(classPattern, methodPattern), true);
    }
}
