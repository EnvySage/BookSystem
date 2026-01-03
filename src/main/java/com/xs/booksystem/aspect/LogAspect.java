package com.xs.booksystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    // 日志级别常量
    private static final int LEVEL_BASIC = 1;     // 基本级别 - 只记录开始和结束
    private static final int LEVEL_STANDARD = 2;  // 标准级别 - 记录开始、重要操作和结束
    private static final int LEVEL_DETAILED = 3;  // 详细级别 - 记录所有操作细节

    // 当前日志级别（可以根据需要调整）
    private static final int CURRENT_LOG_LEVEL = LEVEL_STANDARD;

    /**
     * 定义切点，拦截Service包下所有类的所有方法
     */
    @Pointcut("execution(* com.xs.booksystem.service..*.*(..))")
    public void serviceLog() {}

    /**
     * 定义切点，拦截Controller包下所有类的所有方法
     */
    @Pointcut("execution(* com.xs.booksystem.controller..*.*(..))")
    public void controllerLog() {}

    /**
     * 环绕通知，记录方法执行时间和服务调用日志
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around("serviceLog()")
    public Object serviceLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = getSimpleClassName(joinPoint.getTarget().getClass().getSimpleName());
        String methodName = signature.getName();
        String simpleMethodName = simplifyMethodName(methodName);
        String operationType = getOperationType(methodName);
        long startTime = System.currentTimeMillis();
        if (CURRENT_LOG_LEVEL >= LEVEL_STANDARD) {
            log.info("START  {}.{}", className, simpleMethodName);
        }
        Object result;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            if (CURRENT_LOG_LEVEL >= LEVEL_BASIC) {
                log.info("{} {} - {}ms", operationType, simpleMethodName, (endTime - startTime));
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("ERROR  {}.{} - {}ms - EXCEPTION: {}", className, simpleMethodName, (endTime - startTime), e.getMessage());
            throw e;
        }
        return result;
    }

    /**
     * 控制器层日志记录
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around("controllerLog()")
    public Object controllerLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = getSimpleClassName(joinPoint.getTarget().getClass().getSimpleName());
        String methodName = signature.getName();

        // 简化方法名
        String simpleMethodName = simplifyMethodName(methodName);

        long startTime = System.currentTimeMillis();

        // 根据日志级别决定是否记录开始
        if (CURRENT_LOG_LEVEL >= LEVEL_STANDARD) {
            log.info("START  {}.{}", className, simpleMethodName);
        }

        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 记录响应信息
            long endTime = System.currentTimeMillis();

            // 根据日志级别决定记录详细程度
            if (CURRENT_LOG_LEVEL >= LEVEL_BASIC) {
                // 如果返回的是Result类型，获取code
                String resultCode = "SUCCESS";
                if (result != null && result.getClass().getSimpleName().equals("Result")) {
                    try {
                        Object code = result.getClass().getMethod("getCode").invoke(result);
                        if (code != null && !code.equals(1)) {
                            resultCode = "FAILED";
                        }
                    } catch (Exception ignored) {
                        resultCode = "UNKNOWN";
                    }
                }
                log.info("END    {}.{} - {}ms - {}", className, simpleMethodName, (endTime - startTime), resultCode);
                log.info("===============================================");
            }
        } catch (Exception e) {
            // 记录异常信息
            long endTime = System.currentTimeMillis();
            log.error("END    {}.{} - {}ms - EXCEPTION: {}", className, simpleMethodName, (endTime - startTime), e.getMessage());
            throw e;
        }

        return result;
    }

    /**
     * 获取简化的类名（去掉Controller、Service、Impl等后缀）
     */
    private String getSimpleClassName(String fullClassName) {
        String simpleName = fullClassName;
        return simpleName;
    }

    /**
     * 简化方法名
     */
    private String simplifyMethodName(String methodName) {
        return methodName;
    }

    /**
     * 根据方法名识别操作类型
     */
    private String getOperationType(String methodName) {
        if (methodName.startsWith("get") || methodName.startsWith("select") || methodName.startsWith("find") ||
                methodName.startsWith("query") || methodName.startsWith("search") || methodName.startsWith("list")) {
            return "DB_QUERY";
        } else if (methodName.startsWith("create") || methodName.startsWith("insert") || methodName.startsWith("add")) {
            return "DB_INSERT";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify") || methodName.startsWith("change")) {
            return "DB_UPDATE";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "DB_DELETE";
        } else {
            return "SERVICE";
        }
    }
}
