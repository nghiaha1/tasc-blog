package com.tasc.blogging.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Before("execution(* com.tasc.blogging.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering " + joinPoint.getSignature().getName());
    }

    @After("execution(* com.tasc.blogging.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Exiting " + joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "execution(* com.tasc.blogging.*.*(..))",
            returning= "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Result of " + joinPoint.getSignature().getName() + " is: " + result);
    }

    @AfterThrowing(
            pointcut = "execution(* com.tasc.blogging.*.*(..))",
            throwing= "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Exception in " + joinPoint.getSignature().getName() + " with cause = " + error.getCause() + " and message = " + error.getMessage());
    }
}
