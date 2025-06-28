package com.example.logistics.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    /**
     * 定义切点 - 所有Service层方法
     */
    @Pointcut("execution(* com.example.logistics.service..*.*(..))")
    public void serviceLayer() {}

    /**
     * 定义切点 - 所有Controller层方法
     */
    @Pointcut("execution(* com.example.logistics.controller..*.*(..))")
    public void controllerLayer() {}

    /**
     * 定义切点 - 所有删除操作
     */
    @Pointcut("execution(* com.example.logistics.service..*.delete*(..))")
    public void deleteOperation() {}

    /**
     * 定义切点 - 所有更新操作
     */
    @Pointcut("execution(* com.example.logistics.service..*.update*(..))")
    public void updateOperation() {}

    /**
     * 记录方法执行时间
     */
    @Around("serviceLayer() || controllerLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("方法执行完成 - {}.{}，耗时：{}ms", className, methodName, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("方法执行异常 - {}.{}，耗时：{}ms，异常：{}", 
                    className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }

    /**
     * 记录删除操作
     */
    @Before("deleteOperation()")
    public void logBeforeDelete(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        try {
            log.warn("⚠️ 执行删除操作 - {}.{}，参数：{}", 
                    className, methodName, objectMapper.writeValueAsString(args));
        } catch (Exception e) {
            log.warn("⚠️ 执行删除操作 - {}.{}，参数：{}", 
                    className, methodName, Arrays.toString(args));
        }
    }

    /**
     * 记录更新操作
     */
    @Before("updateOperation()")
    public void logBeforeUpdate(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        try {
            log.info("执行更新操作 - {}.{}，参数：{}", 
                    className, methodName, objectMapper.writeValueAsString(args));
        } catch (Exception e) {
            log.info("执行更新操作 - {}.{}，参数：{}", 
                    className, methodName, Arrays.toString(args));
        }
    }

    /**
     * 记录HTTP请求
     */
    @Before("controllerLayer()")
    public void logHttpRequest(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            
            log.info("HTTP请求 - {}.{}，URL：{}，Method：{}，IP：{}", 
                    className, methodName, 
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    request.getRemoteAddr());
        }
    }
} 