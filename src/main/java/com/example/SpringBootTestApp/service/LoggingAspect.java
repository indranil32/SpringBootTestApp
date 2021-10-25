package com.example.SpringBootTestApp.service;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * https://howtodoinjava.com/spring-boot2/logging/performance-logging-aspectj-aop/
 * https://www.javaguides.net/2019/05/spring-boot-spring-aop-logging-example-tutorial.html
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    //AOP expression for which methods shall be intercepted
    @Around("execution(* com.example..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: {}.{}() with argument[s] = {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName(), Arrays.toString(proceedingJoinPoint.getArgs()));
        }
        try {
            //Measure method execution time
            stopWatch.start();
            Object result = proceedingJoinPoint.proceed();
            stopWatch.stop();
            //Log method execution time
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exit: {}.{}() with result = {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                        proceedingJoinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal argument: {} in {}.{}()", Arrays.toString(proceedingJoinPoint.getArgs()),
                    proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
            throw e;
        }
    }
}
