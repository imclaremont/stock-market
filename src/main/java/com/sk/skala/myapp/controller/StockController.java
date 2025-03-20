package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.service.StockService;
import com.sk.skala.myapp.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Stock API", description = "주식 관리 API")
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "전체 주식 목록 조회", description = "저장된 모든 주식 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @Operation(summary = "특정 주식 조회 (ID)", description = "주식 ID로 특정 주식 정보를 조회합니다.")
    @GetMapping("/{stockId}")
    public ResponseEntity<Stock> getStockById(@PathVariable int stockId) {
        Stock stock = stockService.findStockById(stockId);
        return ResponseEntity.ok(stock);
    }

    @Operation(summary = "특정 주식 조회 (이름)", description = "주식 이름으로 특정 주식 정보를 조회합니다.")
    @GetMapping("/name/{stockName}")
    public ResponseEntity<Stock> getStockByName(@PathVariable String stockName) {
        Stock stock = stockService.findStockByName(stockName);
        return ResponseEntity.ok(stock);
    }

    @Operation(summary = "새로운 주식 추가", description = "새로운 주식을 추가합니다.")
    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        stockService.createStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(stock);
    }

    @Operation(summary = "주식 정보 수정", description = "기존 주식 정보를 수정합니다.")
    @PutMapping("/{stockName}")
    public ResponseEntity<Stock> updateStock(@PathVariable String stockName, @RequestBody Stock updatedStock) {
        Stock existingStock = stockService.findStockByName(stockName);
        existingStock.setStockPrice(updatedStock.getStockPrice());

        stockService.saveStockList();
        return ResponseEntity.ok(existingStock);
    }

    @Operation(summary = "주식 삭제", description = "특정 주식을 삭제합니다.")
    @DeleteMapping("/{stockName}")
    public ResponseEntity<Void> deleteStock(@PathVariable String stockName) {
        stockService.removeStock(stockName);
        return ResponseEntity.noContent().build();
    }
}
