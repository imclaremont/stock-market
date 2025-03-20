package com.sk.skala.myapp.repository;

import org.springframework.stereotype.Repository;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.model.StockConstants;
import com.sk.skala.myapp.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class StockRepository {

    private final String STOCK_FILE = "/config/workspace/basic/backend-spring/myapp/src/main/resources/data/stocks.txt";
    private final List<Stock> stockList = new ArrayList<>();

    public StockRepository() {
        loadStockList();
    }

    // ✅ 모든 주식 목록 반환
    public List<Stock> getStockList() {
        return stockList;
    }

    public void loadStockList() {
        File file = new File(STOCK_FILE);
        if (!file.exists()) {
            log.warn("📂 주식 정보 파일이 없습니다. 기본 데이터를 추가합니다.");
            addDefaultStocks();
            saveStockList();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Stock stock = parseLineToStock(line);
                if (stock != null) {
                    stockList.add(stock);
                }
            }
            log.info("✅ 주식 정보 로드 완료");
        } catch (IOException e) {
            log.error("❌ 주식 정보 파일 읽기 오류: {}", e.getMessage());
        }
    }

    public void saveStockList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE))) {
            for (Stock stock : stockList) {
                writer.write(stock.getStockName() + StockConstants.DELIMITER + stock.getStockPrice());
                writer.newLine();
            }
            log.info("💾 주식 정보 저장 완료");
        } catch (IOException e) {
            log.error("❌ 파일 저장 중 오류 발생: {}", e.getMessage());
        }
    }

    private Stock parseLineToStock(String line) {
        String[] fields = line.split(StockConstants.DELIMITER);
        if (fields.length > 1) {
            return new Stock(fields[0], Integer.parseInt(fields[1]));
        }
        log.warn("⚠ 잘못된 주식 데이터 형식: {}", line);
        return null;
    }

    public String getStockListForMenu() {
        return stockList.stream()
                .map(stock -> (stockList.indexOf(stock) + 1) + ". " + stock)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public Stock findStock(int index) {
        if (index >= 0 && index < stockList.size()) {
            return stockList.get(index);
        }
        throw new NotFoundException("🔍 주식을 찾을 수 없습니다. (인덱스: " + index + ")");
    }

    public Stock findStock(String name) {
        return Optional.ofNullable(stockList.stream()
                .filter(stock -> stock.getStockName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null))
                .orElseThrow(() -> new NotFoundException("🔍 주식을 찾을 수 없습니다: " + name));
    }

    private void addDefaultStocks() {
        stockList.add(new Stock("TechCorp", 100));
        stockList.add(new Stock("GreenEnergy", 80));
        stockList.add(new Stock("HealthPlus", 120));
        stockList.add(new Stock("Samsung", 300));
    }
}
