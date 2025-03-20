package com.sk.skala.myapp.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "stockName")
public class Stock {

    @NotBlank(message = "주식 이름은 비어 있을 수 없습니다.")
    private String stockName;

    @Min(value = 0, message = "주식 가격은 0 이상이어야 합니다.")
    private int stockPrice;

    /**
     * ✅ 주식 정보 포맷 변환 (파일 저장용)
     * @return "주식이름:가격" 형식의 문자열
     */
    public String toFileFormat() {
        return stockName + StockConstants.STOCK_PROPS_DELIMITER + stockPrice;
    }
}
