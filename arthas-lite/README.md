# Arthas-Lite

A lightweight version of Arthas for thread monitoring and method tracing.

## Features

- **Thread Command**: Display thread information and stack traces
- **Trace Command**: Trace method execution time using AOP/instrumentation

## Building

```bash
cd /Users/huanghu/Documents/program/java/source/arthas
mvn clean install -pl arthas-lite -am
```

## Running

### Start the Demo Application

```bash
java -javaagent:arthas-core/target/arthas-core-3.7.2.jar -cp arthas-lite/target/arthas-lite-3.7.2.jar com.taobao.arthas.lite.demo.DemoApplication
```

### Attach Arthas-Lite to a Running Process

```bash
java -jar arthas-lite/target/arthas-lite-3.7.2.jar
```

## Usage Examples

### Thread Command

```bash
# List all threads
thread

# Show specific thread stack trace
thread 1
```

### Trace Command

```bash
# Trace all methods in DemoService
trace com.taobao.arthas.lite.demo.DemoApplication.DemoService *

# Trace specific method
trace com.taobao.arthas.lite.demo.DemoApplication.DemoService processData

# Trace with pattern matching
trace com.taobao.arthas.lite.demo.* *
```

## Architecture

- **ArthasLite**: Main entry point
- **ShellServerImpl**: Lightweight shell server implementation
- **ThreadCommand**: Thread monitoring implementation
- **TraceCommand**: Method tracing with AOP/instrumentation
- **MethodTracer**: Execution time tracking
- **TraceTransformer**: Bytecode transformation for tracing

## Demo Output

When you run the trace command, you'll see output like:

```
TRACE com.taobao.arthas.lite.demo.DemoApplication.DemoService.processData() - 105ms
TRACE com.taobao.arthas.lite.demo.DemoApplication.DemoService.calculateSomething() - 52ms
TRACE com.taobao.arthas.lite.demo.DemoApplication.DemoService.doWork() - 103ms
```