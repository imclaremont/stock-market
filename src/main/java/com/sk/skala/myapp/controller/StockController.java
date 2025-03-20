package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Stock API", description = "주식 관리 API")
@RestController
@RequestMapping("/api/v1/stocks") // ✅ API 버전 추가 (v1)
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "전체 주식 목록 조회", description = "저장된 모든 주식 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @Operation(summary = "특정 주식 조회 (ID)", description = "주식 ID로 특정 주식 정보를 조회합니다.")
    @GetMapping("/{stockId}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long stockId) {
        return ResponseEntity.ok(stockService.findStockById(stockId));
    }

    @Operation(summary = "특정 주식 조회 (이름)", description = "주식 이름으로 특정 주식 정보를 조회합니다.")
    @GetMapping("/name/{stockName}")
    public ResponseEntity<Stock> getStockByName(@PathVariable String stockName) {
        return ResponseEntity.ok(stockService.findStockByName(stockName));
    }

    @Operation(summary = "새로운 주식 추가", description = "새로운 주식을 추가합니다.")
    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(stock));
    }

    @Operation(summary = "주식 정보 수정", description = "기존 주식 정보를 수정합니다.")
    @PutMapping("/{stockId}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long stockId, @RequestBody Stock updatedStock) {
        return ResponseEntity.ok(stockService.updateStock(stockId, updatedStock));  // ✅ StockService에서 updateStock() 호출
    }

    @Operation(summary = "주식 삭제", description = "특정 주식을 삭제합니다.")
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long stockId) {
        stockService.removeStock(stockId);  // ✅ StockService에서 removeStock() 호출
        return ResponseEntity.noContent().build();
    }
}
