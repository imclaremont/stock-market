package com.sk.skala.myapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    // 컨트롤러와 서비스 계층을 대상으로 포인트컷 설정
    @Pointcut("execution(* com.sk.skala.myapp.controller.*.*(..)) || execution(* com.sk.skala.myapp.service.*.*(..))")
    public void applicationMethods() {}

    // 메소드 실행 전에 요청 파라미터 로깅
    @Before("applicationMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("📥 [요청] 메서드 실행: " + methodName + " | 파라미터: " + Arrays.toString(args));
    }

    @AfterReturning(pointcut = "applicationMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();

        // result가 null이면 "null"로 출력
        String resultString = (result != null) ? result.toString() : "null";

        logger.info("📤 [응답] 메서드 완료: " + methodName + " | 반환값: " + resultString);
    }

    // 예외 발생 시 예외 정보 로깅 (예외를 다시 던지도록 수정)
    @AfterThrowing(pointcut = "applicationMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        
        logger.severe("❌ [오류] 예외 발생: " + methodName + " | 예외 메시지: " + exception.getMessage());

        // 예외를 다시 던져서 AOP가 `null`을 반환하지 않도록 수정
        throw new RuntimeException(exception);
    }
}
