package com.alibaba.arthas;

/**
 * 测试 jad 命令是否能够完整展示所有方法内容，包括可能被 JVM 内联的方法
 */
public class InlineTest {
    private int value = 0;

    /**
     * 简单的 getter 方法，可能被 JVM 内联
     */
    public int getValue() {
        return value;
    }

    /**
     * 简单的 setter 方法，可能被 JVM 内联
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 简单的加法方法，可能被 JVM 内联
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * 简单的乘法方法，可能被 JVM 内联
     */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * 复杂的方法，包含多个步骤，不太可能被 JVM 内联
     */
    public int complexCalculation(int a, int b, int c) {
        int result = add(a, b);
        result = multiply(result, c);
        result = add(result, getValue());
        setValue(result);
        return result;
    }

    /**
     * 静态方法，可能被 JVM 内联
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) throws InterruptedException {
        InlineTest test = new InlineTest();
        test.setValue(10);

        while (true) {
            int a = (int) (Math.random() * 100);
            int b = (int) (Math.random() * 100);
            int c = (int) (Math.random() * 100);

            int result = test.complexCalculation(a, b, c);
            System.out.println("Result: " + result);
            System.out.println("Is positive: " + isPositive(result));

            Thread.sleep(1000);
        }
    }
}
