package com.sk.skala.myapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    @GetMapping
    public ResponseEntity<String> getAllStocks() {
        return ResponseEntity.ok("주식 전체 목록 조회");
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<Map<String, String>> getStockById(@PathVariable String stockId) {
        return ResponseEntity.ok(Collections.singletonMap("stockId", stockId));
    }

    @GetMapping("/name/{stockName}")
    public ResponseEntity<Map<String, String>> getStockByName(@PathVariable String stockName) {
        return ResponseEntity.ok(Collections.singletonMap("stockName", stockName));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addStock(@RequestBody Map<String, String> stockData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockData);
    }

    @PutMapping("/{stockName}")
    public ResponseEntity<Map<String, String>> updateStock(@PathVariable String stockName, @RequestBody Map<String, String> updatedData) {
        return ResponseEntity.ok(updatedData);
    }

    @DeleteMapping("/{stockName}")
    public ResponseEntity<String> deleteStock(@PathVariable String stockName) {
        return ResponseEntity.noContent().build();
    }
}
