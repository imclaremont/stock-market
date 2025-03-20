package com.sk.skala.myapp.service;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return stockRepository.getStockList();
    }

    // ✅ 특정 주식 조회 (이름 기준)
    public Stock findStockByName(String stockName) {
        log.info("🔍 [StockService] 주식 조회 (이름): {}", stockName);
        return stockRepository.findStock(stockName);
    }

    // ✅ 특정 주식 조회 (인덱스 기준)
    public Stock findStockByIndex(int index) {
        log.info("🔍 [StockService] 주식 조회 (인덱스): {}", index);
        Stock stock = stockRepository.findStock(index);
        if (stock == null) {
            throw new NotFoundException("해당 인덱스에 주식이 없습니다: " + index);
        }
        return stock;
    }

    // ✅ 새로운 주식 추가
    public Stock createStock(Stock stock) {
        log.info("➕ [StockService] 주식 추가: {}", stock);
        stockRepository.getStockList().add(stock);
        stockRepository.saveStockList();
        return stock;
    }

    // ✅ 주식 삭제
    public void removeStock(String stockName) {
        log.info("🗑 [StockService] 주식 삭제: {}", stockName);
        Stock stock = findStockByName(stockName);

        List<Stock> stockList = stockRepository.getStockList();
        stockList.remove(stock);
        stockRepository.saveStockList();
    }

    // ✅ 주식 데이터 저장
    public void saveStockList() {
        log.info("💾 [StockService] 주식 목록 저장");
        stockRepository.saveStockList();
    }
}
