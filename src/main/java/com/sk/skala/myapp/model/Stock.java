package com.sk.skala.myapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "stockName")
@Builder // ✅ Builder 패턴 추가 (추천)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ✅ 기본 키 (자동 생성)

    @NotBlank(message = "주식 이름은 비어 있을 수 없습니다.")
    @Column(name = "stock_name", unique = true, nullable = false, length = 100)
    private String stockName;  // ✅ 주식 이름 (중복 방지)

    @Min(value = 0, message = "주식 가격은 0 이상이어야 합니다.")
    @Column(name = "stock_price", nullable = false)
    private int stockPrice;  // ✅ 주식 가격

    /**
     * ✅ 필요한 생성자 추가
     */
    public Stock(String stockName, int stockPrice) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
    }

    /**
     * ✅ 주식 정보 포맷 변환 (파일 저장용)
     * @return "주식이름:가격" 형식의 문자열
     */
    public String toFileFormat() {
        return stockName + StockConstants.STOCK_PROPS_DELIMITER + stockPrice;
    }
}
