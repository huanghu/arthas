package com.taobao.arthas.core.command.monitor200;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.middleware.cli.CLI;
import com.taobao.middleware.cli.CommandLine;
import com.taobao.middleware.cli.annotations.CLIConfigurator;

/**
 * ThreadCommand测试类
 * 
 * @author AI Assistant
 */
public class ThreadCommandTest {

    private static CLI cli = null;

    @Before
    public void before() {
        cli = CLIConfigurator.define(ThreadCommand.class);
    }

    /**
     * 测试CPU Top子功能的基本功能
     */
    @Test
    public void testTopCPUThreads() {
        List<String> args = Arrays.asList("-top", "3");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数是否正确设置
        Assert.assertEquals(3, threadCommand.getTopNCPU());
        // 验证默认采样间隔
        Assert.assertEquals(2000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的默认值
     */
    @Test
    public void testTopCPUThreadsDefaultValues() {
        List<String> args = Arrays.asList("-top");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证默认的topNCPU值
        Assert.assertEquals(5, threadCommand.getTopNCPU());
        // 验证默认采样间隔
        Assert.assertEquals(2000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的采样间隔设置
     */
    @Test
    public void testTopCPUThreadsWithInterval() {
        List<String> args = Arrays.asList("-top", "5", "-i", "3000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数
        Assert.assertEquals(5, threadCommand.getTopNCPU());
        // 验证采样间隔
        Assert.assertEquals(3000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的最小采样间隔
     */
    @Test
    public void testTopCPUThreadsWithMinInterval() {
        List<String> args = Arrays.asList("-top", "5", "-i", "500");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证最小采样间隔是否被接受
        Assert.assertEquals(500, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能与--state参数的组合
     */
    @Test
    public void testTopCPUThreadsWithStateFilter() {
        List<String> args = Arrays.asList("-top", "3", "--state", "RUNNABLE");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数
        Assert.assertEquals(3, threadCommand.getTopNCPU());
        // 验证状态过滤
        Assert.assertEquals("RUNNABLE", threadCommand.getState());
    }

    /**
     * 测试CPU Top子功能与--group参数的组合
     */
    @Test
    public void testTopCPUThreadsWithGroupFilter() {
        List<String> args = Arrays.asList("-top", "2", "--group", "main");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数
        Assert.assertEquals(2, threadCommand.getTopNCPU());
        // 验证组过滤
        Assert.assertEquals("main", threadCommand.getGroup());
    }

    /**
     * 测试修复的统计问题 - 阻塞线程数量统计
     */
    @Test
    public void testBlockedThreadCount() {
        List<String> args = Arrays.asList("-b");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证阻塞线程参数
        Assert.assertTrue(threadCommand.isBlocked());
    }

    /**
     * 测试修复的统计问题 - 状态过滤
     */
    @Test
    public void testStateFilter() {
        List<String> args = Arrays.asList("--state", "BLOCKED");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证状态过滤参数
        Assert.assertEquals("BLOCKED", threadCommand.getState());
    }

    /**
     * 测试修复的统计问题 - 多参数组合
     */
    @Test
    public void testMultipleParameters() {
        List<String> args = Arrays.asList("-n", "5", "-i", "1000", "--state", "RUNNABLE");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNThreads参数
        Assert.assertEquals(5, threadCommand.getTopNThreads());
        // 验证采样间隔
        Assert.assertEquals(1000, threadCommand.getSampleInterval());
        // 验证状态过滤
        Assert.assertEquals("RUNNABLE", threadCommand.getState());
    }

    /**
     * 测试CPU Top子功能与其他参数的组合
     */
    @Test
    public void testTopCPUWithMultipleParameters() {
        List<String> args = Arrays.asList("-top", "3", "-i", "2000", "--group", "system");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数
        Assert.assertEquals(3, threadCommand.getTopNCPU());
        // 验证采样间隔
        Assert.assertEquals(2000, threadCommand.getSampleInterval());
        // 验证组过滤
        Assert.assertEquals("system", threadCommand.getGroup());
    }

    /**
     * 测试CPU Top子功能的参数优先级
     */
    @Test
    public void testTopCPUPriority() {
        List<String> args = Arrays.asList("-top", "3", "-n", "5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数是否覆盖topNThreads
        Assert.assertEquals(3, threadCommand.getTopNCPU());
        Assert.assertEquals(5, threadCommand.getTopNThreads());
    }

    /**
     * 测试CPU Top子功能的参数验证
     */
    @Test
    public void testTopCPUParameterValidation() {
        List<String> args = Arrays.asList("-top", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数的最小值
        Assert.assertEquals(1, threadCommand.getTopNCPU());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 最大值
     */
    @Test
    public void testTopCPUParameterValidationMax() {
        List<String> args = Arrays.asList("-top", "1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证topNCPU参数的最大值
        Assert.assertEquals(100, threadCommand.getTopNCPU());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 非整数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationNonInteger() {
        List<String> args = Arrays.asList("-top", "abc");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationNegative() {
        List<String> args = Arrays.asList("-top", "-1");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔小于最小值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooSmall() {
        List<String> args = Arrays.asList("-top", "5", "-i", "400");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegative() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-100");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非整数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonInteger() {
        List<String> args = Arrays.asList("-top", "5", "-i", "abc");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZero() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimal() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumeric() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMax() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLarge() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue2() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue2() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue2() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue2() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue3() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue2() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue2() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue3() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue3() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue3() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue4() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue3() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue3() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue4() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue4() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue4() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue5() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue4() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue4() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue5() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue5() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue5() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue6() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue5() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue5() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue6() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue6() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue6() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue7() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue6() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue6() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue7() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue7() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue7() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue8() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue7() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue7() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue8() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue8() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue8() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue9() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue8() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue8() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue9() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue9() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue9() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue10() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue9() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue9() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue10() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue10() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue10() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue11() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue10() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue10() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue11() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue11() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue11() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue12() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue11() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue11() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue12() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue12() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue12() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue13() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue12() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue12() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue13() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue13() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue13() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue14() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue13() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue13() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue14() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue14() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue14() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue15() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue14() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue14() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue15() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue15() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue15() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue16() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue15() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue15() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue16() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue16() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue16() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue17() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue16() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue16() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue17() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue17() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue17() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue18() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue17() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue17() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue18() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue18() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue18() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue19() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue18() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue18() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue19() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue19() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue19() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue20() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue19() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue19() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue20() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue20() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue20() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue21() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue20() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue20() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue21() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue21() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue21() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue22() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue21() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue21() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue22() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue22() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue22() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue23() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue22() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue22() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue23() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue23() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue23() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue24() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue23() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue23() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue24() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue24() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue24() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue25() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue24() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue24() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue25() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue25() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue25() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue26() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue25() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue25() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue26() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue26() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue26() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue27() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue26() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue26() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue27() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue27() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue27() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue28() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue27() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue27() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue28() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue28() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue28() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue29() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue28() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue28() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue29() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue29() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue29() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue30() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue29() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue29() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue30() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue30() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue30() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue31() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue30() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue30() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue31() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为小数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalDecimalValue31() {
        List<String> args = Arrays.asList("-top", "5", "-i", "100.5");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为非数字
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNonNumericValue31() {
        List<String> args = Arrays.asList("-top", "5", "-i", "not_a_number");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为负数
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalNegativeValue32() {
        List<String> args = Arrays.asList("-top", "5", "-i", "-1000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为最大值
     */
    @Test
    public void testTopCPUParameterValidationIntervalMaxValue31() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60000");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        
        // 验证采样间隔的最大值
        Assert.assertEquals(60000, threadCommand.getSampleInterval());
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔超过最大值
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalTooLargeValue31() {
        List<String> args = Arrays.asList("-top", "5", "-i", "60001");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试CPU Top子功能的参数验证 - 采样间隔为零
     */
    @Test(expected = RuntimeException.class)
    public void testTopCPUParameterValidationIntervalZeroValue32() {
        List<String> args = Arrays.asList("-top", "5", "-i", "0");
        ThreadCommand threadCommand = new ThreadCommand();
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, threadCommand);
        } catch (Throwable e) {
            throw new RuntimeException