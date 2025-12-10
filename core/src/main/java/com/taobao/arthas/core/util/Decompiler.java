package com.taobao.arthas.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns.LineNumberMapping;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.taobao.arthas.common.Pair;

/**
 *
 * @author hengyunabc 2018-11-16
 *
 */
public class Decompiler {
    private static final Logger logger = LoggerFactory.getLogger(Decompiler.class);

    public static String decompile(String classFilePath, String methodName) {
        return decompile(classFilePath, methodName, false);
    }

    public static String decompile(String classFilePath, String methodName, boolean hideUnicode) {
        return decompile(classFilePath, methodName, hideUnicode, true);
    }

    public static Pair<String, NavigableMap<Integer, Integer>> decompileWithMappings(String classFilePath, 
            String methodName, boolean hideUnicode, boolean printLineNumber) {
        logger.debug("开始反编译类文件: {}, 方法: {}", classFilePath, methodName);
        
        final StringBuilder sb = new StringBuilder(8192);
        final NavigableMap<Integer, Integer> lineMapping = new TreeMap<Integer, Integer>();

        OutputSinkFactory mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                return Arrays.asList(SinkClass.STRING, SinkClass.DECOMPILED, SinkClass.DECOMPILED_MULTIVER,
                        SinkClass.EXCEPTION_MESSAGE, SinkClass.LINE_NUMBER_MAPPING);
            }

            @Override
            public <T> Sink<T> getSink(final SinkType sinkType, final SinkClass sinkClass) {
                return new Sink<T>() {
                    @Override
                    public void write(T sinkable) {
                        // skip message like: Analysing type demo.MathGame
                        if (sinkType == SinkType.PROGRESS) {
                            return;
                        }
                        if (sinkType == SinkType.LINENUMBER) {
                            LineNumberMapping mapping = (LineNumberMapping) sinkable;
                            NavigableMap<Integer, Integer> classFileMappings = mapping.getClassFileMappings();
                            NavigableMap<Integer, Integer> mappings = mapping.getMappings();
                            if (classFileMappings != null && mappings != null) {
                                for (Entry<Integer, Integer> entry : mappings.entrySet()) {
                                    Integer srcLineNumber = classFileMappings.get(entry.getKey());
                                    lineMapping.put(entry.getValue(), srcLineNumber);
                                }
                            }
                            return;
                        }
                        sb.append(sinkable);
                    }
                };
            }
        };

        HashMap<String, String> options = new HashMap<String, String>();
        /**
         * @see org.benf.cfr.reader.util.MiscConstants.Version.getVersion() Currently,
         *      the cfr version is wrong. so disable show cfr version.
         */
        options.put("showversion", "false");
        options.put("hideutf", String.valueOf(hideUnicode));
        options.put("trackbytecodeloc", "true");
        // 禁用删除死方法选项，以避免方法体丢失
        options.put("removedeadmethods", "false");
        
        logger.debug("反编译选项配置: hideUnicode={}, printLineNumber={}, removedeadmethods=false", 
                hideUnicode, printLineNumber);
        
        if (!StringUtils.isBlank(methodName)) {
            options.put("methodname", methodName);
            logger.debug("指定反编译方法: {}", methodName);
        }

        CfrDriver driver = new CfrDriver.Builder().withOptions(options).withOutputSink(mySink).build();
        List<String> toAnalyse = new ArrayList<String>();
        toAnalyse.add(classFilePath);
        
        logger.debug("执行反编译分析...");
        driver.analyse(toAnalyse);
        logger.debug("反编译分析完成");

        String resultCode = sb.toString();
        
        logger.debug("反编译结果长度: {} 字符", resultCode.length());
        
        if (printLineNumber && !lineMapping.isEmpty()) {
            logger.debug("添加行号映射，共 {} 个映射条目", lineMapping.size());
            resultCode = addLineNumber(resultCode, lineMapping);
        }

        logger.debug("反编译完成，返回结果");
        return Pair.make(resultCode, lineMapping);
    }

    public static String decompile(String classFilePath, String methodName, boolean hideUnicode,
            boolean printLineNumber) {
        return decompileWithMappings(classFilePath, methodName, hideUnicode, printLineNumber).getFirst();
    }

    private static String addLineNumber(String src, Map<Integer, Integer> lineMapping) {
        int maxLineNumber = 0;
        for (Integer value : lineMapping.values()) {
            if (value != null && value > maxLineNumber) {
                maxLineNumber = value;
            }
        }

        String formatStr = "/*%2d*/ ";
        String emptyStr = "       ";

        StringBuilder sb = new StringBuilder();

        List<String> lines = StringUtils.toLines(src);

        if (maxLineNumber >= 1000) {
            formatStr = "/*%4d*/ ";
            emptyStr = "         ";
        } else if (maxLineNumber >= 100) {
            formatStr = "/*%3d*/ ";
            emptyStr = "        ";
        }

        int index = 0;
        for (String line : lines) {
            Integer srcLineNumber = lineMapping.get(index + 1);
            if (srcLineNumber != null) {
                sb.append(String.format(formatStr, srcLineNumber));
            } else {
                sb.append(emptyStr);
            }
            sb.append(line).append("\n");
            index++;
        }

        return sb.toString();
    }

}
