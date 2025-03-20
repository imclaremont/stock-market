package com.sk.skala.myapp.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AOPService {

    public void logAction(String actionName) {
        log.info("✅ [AOPService] 실행 중: {}", actionName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            log.debug("🔍 {} 수행 중...", actionName);
            System.out.println("--> 실행 중: " + actionName);
            
            Thread.sleep(500); // 가상의 로직 실행 시간 (테스트용)
            
            log.info("✅ {} 완료!", actionName);
        } catch (Exception e) {
            log.error("❌ {} 수행 중 오류 발생: {}", actionName, e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("⏳ {} 실행 시간: {}ms", actionName, (endTime - startTime));
        }
    }

    public void doAction() {
        logAction("기본 액션");
    }
}
