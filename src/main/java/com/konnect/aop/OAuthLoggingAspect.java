package com.konnect.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class OAuthLoggingAspect {
    // CustomOAuth2UserService: loadUser method
    @Pointcut("execution(* com.konnect.auth.service.CustomOAuth2UserService.loadUser(..))")
    public void loadUserPointcut() {}

    @Before("loadUserPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[OAuth2] loadUser 시작 - {}", joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.info("[OAuth2] userRequest: {}", args[0]);
        }
    }

    @AfterReturning(pointcut = "loadUserPointcut()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("[OAuth2] loadUser 완료 - result: {}", result);
    }

    @AfterThrowing(pointcut = "loadUserPointcut()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        log.error("[OAuth2] loadUser 예외 발생 - {}", ex.getMessage(), ex);
    }
}
