package com.sk.skala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.sk.skala.myapp.service.SkalaStockMarket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class MyappApplication {
    
    public static void main(String[] args) {
        log.info("🚀 [MyappApplication] 애플리케이션 시작");

        try {
            // Spring ApplicationContext를 가져옴
            ApplicationContext context = SpringApplication.run(MyappApplication.class, args);

            // SkalaStockMarket을 스프링 컨테이너에서 가져와 실행
            SkalaStockMarket skalaStockMarket = context.getBean(SkalaStockMarket.class);
            skalaStockMarket.start();
        } catch (Exception e) {
            log.error("❌ [MyappApplication] 애플리케이션 실행 중 오류 발생: {}", e.getMessage(), e);
        }

        log.info("🛑 [MyappApplication] 애플리케이션 종료");
    }
}
