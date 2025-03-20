package com.sk.skala.myapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "player_stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id", "stockName"})
public class PlayerStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ✅ 기본 키 (자동 생성)

    @Column(name = "stock_name", nullable = false)
    private String stockName;  // ✅ 주식 이름

    @Column(name = "stock_price", nullable = false)
    private int stockPrice;  // ✅ 주식 가격

    @Min(value = 0, message = "주식 수량은 0 이상이어야 합니다.")
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;  // ✅ 보유 주식 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude // 🔹 무한 순환 참조 방지
    private Player player;

    /**
     * ✅ Stock 정보를 기반으로 PlayerStock 생성
     */
    public PlayerStock(Stock stock, int quantity, Player player) {
        this.stockName = stock.getStockName();
        this.stockPrice = stock.getStockPrice();
        this.stockQuantity = quantity;
        this.player = player;
    }

    /**
     * ✅ 파일에서 읽은 정보로 PlayerStock 생성
     */
    public PlayerStock(String name, int price, int quantity, Player player) {
        this.stockName = name;
        this.stockPrice = price;
        this.stockQuantity = quantity;
        this.player = player;
    }
}
