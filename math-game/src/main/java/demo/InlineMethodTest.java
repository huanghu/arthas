package demo;

/**
 * 测试类，用于验证Jad命令反编译时内联方法的处理
 */
public class InlineMethodTest {
    private int value;
    
    public InlineMethodTest(int value) {
        this.value = value;
    }
    
    /**
     * 简单的getter方法，可能会被JVM内联
     */
    public int getValue() {
        return value;
    }
    
    /**
     * 简单的setter方法，可能会被JVM内联
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    /**
     * 简单的计算方法，可能会被JVM内联
     */
    public int add(int other) {
        return value + other;
    }
    
    /**
     * 复杂的计算方法，不太可能会被JVM内联
     */
    public int complexCalculate(int a, int b, int c) {
        int result = 0;
        for (int i = 0; i < a; i++) {
            result += b * c;
        }
        return result;
    }
    
    public static void main(String[] args) {
        InlineMethodTest test = new InlineMethodTest(10);
        
        // 调用可能会被内联的方法
        int value = test.getValue();
        test.setValue(value + 5);
        int sum = test.add(20);
        
        // 调用不太可能会被内联的方法
        int complexResult = test.complexCalculate(100, 200, 300);
        
        System.out.println("value: " + value);
        System.out.println("sum: " + sum);
        System.out.println("complexResult: " + complexResult);
        
        // 无限循环，让程序持续运行
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
