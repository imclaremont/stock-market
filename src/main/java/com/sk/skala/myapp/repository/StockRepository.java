package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     *  특정 주식 조회 (이름 기준)
     */
    Optional<Stock> findByStockName(String stockName);

    /**
     *  주식 존재 여부 확인 (이름 기준)
     */
    boolean existsByStockName(String stockName);

    /**
     *  특정 주식 조회 (ID 기준, 예외처리 포함)
     */
    default Stock findStockById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("주식을 찾을 수 없습니다. (ID: " + id + ")"));
    }

    /**
     *  특정 주식 조회 (이름 기준, 예외처리 포함)
     */
    default Stock findStockByName(String stockName) {
        return findByStockName(stockName)
                .orElseThrow(() -> new NotFoundException("주식을 찾을 수 없습니다: " + stockName));
    }
}
