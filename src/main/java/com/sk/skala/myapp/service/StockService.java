package com.sk.skala.myapp.service;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // ✅ 모든 주식 목록 조회
    public List<Stock> getAllStocks() {
        log.info("📊 [StockService] 모든 주식 정보 조회 요청");
        return stockRepository.findAll();
    }

    // ✅ 특정 주식 조회 (이름 기준)
    public Stock findStockByName(String stockName) {
        log.info("🔍 [StockService] 주식 조회 (이름): {}", stockName);
        return stockRepository.findByStockName(stockName)
                .orElseThrow(() -> new NotFoundException("해당 이름의 주식을 찾을 수 없습니다: " + stockName));
    }

    // ✅ 특정 주식 조회 (ID 기준)
    public Stock findStockById(Long stockId) {
        log.info("🔍 [StockService] 주식 조회 (ID): {}", stockId);
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new NotFoundException("해당 ID의 주식이 없습니다: " + stockId));
    }

    // ✅ 새로운 주식 추가
    @Transactional
    public Stock createStock(Stock stock) {
        log.info("➕ [StockService] 주식 추가: {}", stock);
        return stockRepository.save(stock);
    }

    // ✅ 주식 정보 수정 메서드 추가
    @Transactional
    public Stock updateStock(Long stockId, Stock updatedStock) {
        log.info("✏️ [StockService] 주식 정보 수정: ID = {}, 업데이트 데이터 = {}", stockId, updatedStock);

        // 기존 주식 조회
        Stock existingStock = findStockById(stockId);

        // 주식 정보 업데이트
        existingStock.setStockName(updatedStock.getStockName());
        existingStock.setStockPrice(updatedStock.getStockPrice());

        return stockRepository.save(existingStock);
    }

    // ✅ 주식 삭제 (ID 기준)
    @Transactional
    public void removeStock(Long stockId) {
        log.info("🗑 [StockService] 주식 삭제 (ID): {}", stockId);
        Stock stock = findStockById(stockId);
        stockRepository.delete(stock);
    }
}
