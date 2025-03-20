package com.sk.skala.myapp.model;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = "stockQuantity")
public class PlayerStock extends Stock {

    @Min(value = 0, message = "주식 수량은 0 이상이어야 합니다.")
    private int stockQuantity;

    /**
     * ✅ Stock 객체를 기반으로 PlayerStock 생성
     */
    public PlayerStock(Stock stock, int quantity) {
        super(stock.getStockName(), stock.getStockPrice());
        this.stockQuantity = quantity;
    }

    /**
     * ✅ 파일에서 읽은 정보로 PlayerStock 생성
     */
    public PlayerStock(String name, String price, String quantity) {
        super(name, Integer.parseInt(price));
        this.stockQuantity = Integer.parseInt(quantity);
    }
}
