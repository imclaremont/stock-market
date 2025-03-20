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

    // ✅ 컨트롤러와 서비스 계층을 대상으로 포인트컷 설정
    @Pointcut("execution(* com.sk.skala.myapp.controller.*.*(..)) || execution(* com.sk.skala.myapp.service.*.*(..))")
    public void applicationMethods() {}

    // ✅ 메소드 실행 전에 요청 파라미터 로깅
    @Before("applicationMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("📥 [요청] 메서드 실행: " + methodName + " | 파라미터: " + Arrays.toString(args));
    }

    // ✅ 정상적으로 실행된 후 반환값 로깅
    @AfterReturning(pointcut = "applicationMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("📤 [응답] 메서드 완료: " + methodName + " | 반환값: " + result);
    }

    // ✅ 예외 발생 시 예외 정보 로깅
    @AfterThrowing(pointcut = "applicationMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.severe("❌ [오류] 예외 발생: " + methodName + " | 예외 메시지: " + exception.getMessage());
    }
}
