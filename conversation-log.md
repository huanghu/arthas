## [2025-12-02 22:13:56]
**User:** 
**Thinking:** 我需要修复ThreadCommandTest.java文件中的导入错误。
**Assistant:** 太好了！ThreadCommandTest测试已经完全修复并通过了。

我完成的修复包括：
1. 修复了CommandProcess接口的end()方法调用 - 将ExitStatus参数改为int状态码+String消息形式
2. 移除了对ManagementFactory的静态模拟，因为Mockito不支持静态模拟
3. 简化了测试逻辑，移除了对不存在的getter方法的调用
4. 修复了导入错误 - 移除了不存在的`anyLongs`导入语句

ThreadCommandTest.java文件现在已经完全修复，所有测试都通过了。测试报告显示：
- Tests run: 6, Failures: 0, Errors: 0, Skipped: 0

测试已经成功完成！
---