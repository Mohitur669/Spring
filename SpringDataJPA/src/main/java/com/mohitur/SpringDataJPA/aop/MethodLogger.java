package com.mohitur.SpringDataJPA.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
@Component
public class MethodLogger {

    private static final Logger logger = LogManager.getLogger("MethodLogger");

    @Before("execution(* com.mohitur.SpringDataJPA..*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName(); // Just class name
        String methodName = signature.getName(); // Just method name

        logger.info("Entering method: {}.{}", className, methodName);
    }

    @After("execution(* com.mohitur.SpringDataJPA..*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        logger.info("Exiting method: {}.{}", className, methodName);
    }
}