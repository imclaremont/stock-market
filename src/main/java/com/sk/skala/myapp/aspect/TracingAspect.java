package com.sk.skala.myapp.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Slf4j
@Component
public class TracingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    // 컨트롤러 계층 포인트컷
    @Pointcut("execution(* com.sk.skala.myapp.controller.*.*(..))")
    public void controllerLayer() {}

    // 서비스 계층 포인트컷
    @Pointcut("execution(* com.sk.skala.myapp.service.*.*(..))")
    public void serviceLayer() {}

    // 컨트롤러 + 서비스 계층을 모두 포함하는 포인트컷
    @Pointcut("controllerLayer() || serviceLayer()")
    public void tracingTargets() {}

    @Around("tracingTargets()")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        long startTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime = dateFormat.format(new Date(startTime));

        String argsString;
        try {
            argsString = objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Exception e) {
            argsString = "[JSON 변환 오류]";
        }

        log.info("📥 [ENTER] {}.{}() | 실행 시간: {} | 요청 파라미터: {}", className, methodName, formattedTime, argsString);

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            String resultString;
            try {
                resultString = (result != null) ? objectMapper.writeValueAsString(result) : "null";
            } catch (Exception e) {
                resultString = "[JSON 변환 오류]";
            }

            log.info("📤 [EXIT] {}.{}() | 실행 시간: {}ms | 응답 데이터: {}", className, methodName, executionTime, resultString);

            return result;
        } catch (Throwable t) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("❌ [ERROR] {}.{}() | 실행 시간: {}ms | 예외 발생: {}", className, methodName, executionTime, t.getMessage());
            throw t;
        }
    }
}
