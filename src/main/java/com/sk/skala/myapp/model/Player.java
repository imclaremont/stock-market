package com.sk.skala.myapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "playerId")
public class Player {

    @Id
    @NotBlank(message = "playerId는 비어있을 수 없습니다.")
    @Column(name = "player_id", unique = true, nullable = false, length = 50)
    private String playerId;

    @Min(value = 0, message = "playerMoney는 0 이상이어야 합니다.")
    @Column(name = "player_money", nullable = false)
    private int playerMoney = 10_000;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlayerStock> playerStocks = new ArrayList<>();

    public Player(String playerId, int playerMoney) {
        this.playerId = playerId;
        this.playerMoney = playerMoney;
        this.playerStocks = new ArrayList<>();
    }

    public Player(String playerId) {
        this.playerId = playerId;
        this.playerMoney = 10_000;
    }

    public void addStock(PlayerStock stock) {
        playerStocks.stream()
            .filter(existingStock -> existingStock.getStockName().equals(stock.getStockName()))
            .findFirst()
            .ifPresentOrElse(
                existingStock -> existingStock.setStockQuantity(existingStock.getStockQuantity() + stock.getStockQuantity()),
                () -> {
                    stock.setPlayer(this); // 연관관계 설정
                    playerStocks.add(stock);
                }
            );
    }

    public void updatePlayerStock(PlayerStock stock) {
        playerStocks.removeIf(existingStock -> 
            existingStock.getStockName().equals(stock.getStockName()) && stock.getStockQuantity() == 0
        );

        playerStocks.stream()
            .filter(existingStock -> existingStock.getStockName().equals(stock.getStockName()))
            .findFirst()
            .ifPresent(existingStock -> {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(stock.getStockQuantity());
            });
    }

    public PlayerStock findStock(int index) {
        return (index >= 0 && index < playerStocks.size()) ? playerStocks.get(index) : null;
    }

    public String getPlayerStocksForFile() {
        return playerStocks.stream()
                .map(PlayerStock::toString)
                .reduce((s1, s2) -> s1 + "|" + s2)
                .orElse("");
    }

    public String getPlayerStocksForMenu() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerStocks.size(); i++) {
            sb.append(i + 1).append(". ").append(playerStocks.get(i)).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
