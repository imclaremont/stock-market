package com.sk.skala.myapp.service;

import org.springframework.stereotype.Service;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.StockRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // ✅ 모든 주식 목록 조회
    public List<Stock> getAllStocks() {
        log.info("✅ [StockService] 모든 주식 정보 조회 요청");
        return stockRepository.getStockList();
    }

    // ✅ 특정 주식 조회 (이름 기준)
    public Stock getStockByName(String stockName) {
        log.info("🔍 [StockService] 주식 조회: {}", stockName);
        return stockRepository.findStock(stockName);
    }

    // ✅ 특정 주식 조회 (인덱스 기준)
    public Stock getStockByIndex(int index) {
        log.info("🔍 [StockService] 주식 조회 (인덱스): {}", index);
        return stockRepository.findStock(index);
    }

    // ✅ 새로운 주식 추가
    public Stock addStock(Stock stock) {
        log.info("➕ [StockService] 주식 추가: {}", stock);
        stockRepository.getStockList().add(stock);
        stockRepository.saveStockList();
        return stock;
    }
}
