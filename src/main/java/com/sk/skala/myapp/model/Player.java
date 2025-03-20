package com.sk.skala.myapp.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "playerId")
public class Player {

    @NotBlank(message = "playerId는 비어있을 수 없습니다.")
    private String playerId;

    @Min(value = 0, message = "playerMoney는 0 이상이어야 합니다.")
    private int playerMoney = 10_000;

    private List<PlayerStock> playerStocks = new ArrayList<>();

    public Player(String playerId) {
        this.playerId = playerId;
        this.playerMoney = 10_000;
    }

    /**
     * ✅ 플레이어가 주식을 추가하는 메서드
     * 동일한 주식이 있으면 수량 증가, 없으면 새로 추가
     */
    public void addStock(PlayerStock stock) {
        playerStocks.stream()
            .filter(existingStock -> existingStock.getStockName().equals(stock.getStockName()))
            .findFirst()
            .ifPresentOrElse(
                existingStock -> existingStock.setStockQuantity(existingStock.getStockQuantity() + stock.getStockQuantity()),
                () -> playerStocks.add(stock)
            );
    }

    /**
     * ✅ 플레이어의 보유 주식 업데이트
     * - 수량이 0이면 리스트에서 제거
     * - 기존 주식의 가격과 수량을 업데이트
     */
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

    /**
     * ✅ 보유 주식 리스트에서 특정 인덱스의 주식 찾기
     */
    public PlayerStock findStock(int index) {
        return (index >= 0 && index < playerStocks.size()) ? playerStocks.get(index) : null;
    }

    /**
     * ✅ 파일 저장을 위한 주식 목록 문자열 변환
     */
    public String getPlayerStocksForFile() {
        return playerStocks.stream()
                .map(PlayerStock::toString)
                .reduce((s1, s2) -> s1 + "|" + s2)
                .orElse("");
    }

    /**
     * ✅ 플레이어가 보유한 주식 목록을 출력용 문자열로 변환
     */
    public String getPlayerStocksForMenu() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerStocks.size(); i++) {
            sb.append(i + 1).append(". ").append(playerStocks.get(i)).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
