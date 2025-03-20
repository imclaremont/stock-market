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

    // ✅ 특정 주식 조회 (ID 기준)
    public Stock findStockById(int stockId) {
        log.info("🔍 [StockService] 주식 조회 (ID): {}", stockId);
        Stock stock = stockRepository.findStock(stockId);
        if (stock == null) {
            throw new NotFoundException("해당 ID의 주식이 없습니다: " + stockId);
        }
        return stock;
    }

    // ✅ 새로운 주식 추가 (중복 방지 로직 추가)
    public Stock createStock(Stock stock) {
        log.info("➕ [StockService] 주식 추가: {}", stock);

        // 이미 존재하는 주식인지 확인
        boolean exists = stockRepository.getStockList().stream()
            .anyMatch(s -> s.getStockName().equalsIgnoreCase(stock.getStockName()));

        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 주식입니다: " + stock.getStockName());
        }

        stockRepository.getStockList().add(stock);
        stockRepository.saveStockList();
        return stock;
    }

    // ✅ 주식 삭제 (이름 기준)
    public void removeStock(String stockName) {
        log.info("🗑 [StockService] 주식 삭제 (이름): {}", stockName);
        Stock stock = findStockByName(stockName);

        List<Stock> stockList = stockRepository.getStockList();
        stockList.remove(stock);
        stockRepository.saveStockList();
    }

    // ✅ 주식 삭제 (ID 기준, 새로운 기능 추가)
    public void removeStockById(int stockId) {
        log.info("🗑 [StockService] 주식 삭제 (ID): {}", stockId);
        Stock stock = findStockById(stockId);

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
