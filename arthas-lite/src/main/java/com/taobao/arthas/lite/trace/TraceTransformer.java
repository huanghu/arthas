package com.taobao.arthas.lite.trace;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class transformer for tracing method execution time
 * @author Arthas Team
 */
public class TraceTransformer implements ClassFileTransformer {
    private static final String TIMER_VAR_NAME = "$arthasLiteTimer";
    
    private final Pattern classPattern;
    private final Pattern methodPattern;
    
    public TraceTransformer(String classPattern, String methodPattern) {
        this.classPattern = Pattern.compile(classPattern.replace("*", ".*"));
        this.methodPattern = Pattern.compile(methodPattern.replace("*", ".*"));
    }
    
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, 
                           ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // Convert className from "java/lang/String" to "java.lang.String"
        String javaClassName = className.replace("/", ".");
        
        // Check if class matches the pattern
        Matcher classMatcher = classPattern.matcher(javaClassName);
        if (!classMatcher.matches()) {
            return classfileBuffer;
        }
        
        // Modify the class bytecode
        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = new TraceClassVisitor(classWriter);
            classReader.accept(classVisitor, 0);
            return classWriter.toByteArray();
        } catch (Exception e) {
            System.err.println("Error transforming class " + javaClassName + ": " + e.getMessage());
            e.printStackTrace();
            return classfileBuffer;
        }
    }
    
    /**
     * Class visitor to modify methods
     */
    private class TraceClassVisitor extends ClassVisitor {
        public TraceClassVisitor(ClassVisitor cv) {
            super(Opcodes.ASM9, cv);
        }
        
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            // Check if method matches the pattern
            Matcher methodMatcher = methodPattern.matcher(name);
            if (methodMatcher.matches()) {
                System.out.println("Tracing method: " + name + descriptor);
                // Return a method visitor that adds timing code
                return new TraceMethodVisitor(cv.visitMethod(access, name, descriptor, signature, exceptions));
            }
            return cv.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
    
    /**
     * Method visitor to add timing code
     */
    private class TraceMethodVisitor extends MethodVisitor {
        public TraceMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM9, mv);
        }
        
        @Override
        public void visitCode() {
            // Add code at the beginning of the method: long $arthasLiteTimer = System.nanoTime();
            mv.visitCode();
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            mv.visitVarInsn(Opcodes.LSTORE, 1); // Store the timer value in local variable 1
        }
        
        @Override
        public void visitInsn(int opcode) {
            // Add code before return instructions
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                // Calculate the duration
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                mv.visitVarInsn(Opcodes.LLOAD, 1); // Load the start time
                mv.visitInsn(Opcodes.LSUB); // end - start
                
                // Print the duration
                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                mv.visitLdcInsn("Method execution time: ");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitInsn(Opcodes.L2D); // Convert nanoseconds to double
                mv.visitLdcInsn(1000000.0); // Convert nanoseconds to milliseconds
                mv.visitInsn(Opcodes.DDIV); // duration in milliseconds
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(D)Ljava/lang/StringBuilder;", false);
                mv.visitLdcInsn(" ms");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            mv.visitInsn(opcode);
        }
    }
}
