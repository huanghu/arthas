import org.junit.Test;

public class InlineMethodTest {

    /**
     * 测试方法，包含一个可能会被inline的简单方法
     */
    @Test
    public void testInlineMethod() {
        int a = 5;
        int b = 10;
        
        // 调用可能会被inline的方法
        int sum = add(a, b);
        int product = multiply(a, b);
        
        System.out.println("Sum: " + sum);
        System.out.println("Product: " + product);
    }
    
    /**
     * 简单的加法方法，可能会被JVM inline
     */
    private int add(int a, int b) {
        return a + b;
    }
    
    /**
     * 简单的乘法方法，可能会被JVM inline
     */
    private int multiply(int a, int b) {
        return a * b;
    }
}
