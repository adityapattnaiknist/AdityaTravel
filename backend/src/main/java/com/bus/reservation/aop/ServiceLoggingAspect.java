package com.bus.reservation.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("execution(public * com.bus.reservation.service..*(..))")
    public Object logServiceCalls(ProceedingJoinPoint pjp) throws Throwable {
        String signature = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();
        try {
            log.info("→ {} args={}", signature, pjp.getArgs());
            Object res = pjp.proceed();
            long ms = System.currentTimeMillis() - start;
            log.info("← {} took={}ms", signature, ms);
            return res;
        } catch (Throwable t) {
            long ms = System.currentTimeMillis() - start;
            log.error("× {} failed after {}ms: {}", signature, ms, t.getMessage());
            throw t;
        }
    }
}

