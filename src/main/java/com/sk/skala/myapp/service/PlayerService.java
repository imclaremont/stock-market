package com.sk.skala.myapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.StockRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository; // ✅ 주식 거래를 위해 StockRepository 추가

    public PlayerService(PlayerRepository playerRepository, StockRepository stockRepository) {
        this.playerRepository = playerRepository;
        this.stockRepository = stockRepository;
    }

    // ✅ 모든 플레이어 목록 조회
    public List<Player> getAllPlayers() {
        log.info("✅ [PlayerService] 모든 플레이어 조회 요청");
        return playerRepository.getAllPlayers();
    }

    // ✅ 특정 ID로 플레이어 조회
    public Player findPlayerById(String playerId) {
        log.info("🔍 [PlayerService] 플레이어 조회: ID = {}", playerId);
        return Optional.ofNullable(playerRepository.findPlayer(playerId))
            .orElseThrow(() -> new NotFoundException("플레이어를 찾을 수 없습니다: " + playerId));
    }

    // ✅ 플레이어 이름으로 조회 (새로운 기능 추가)
    public Player findPlayerByName(String playerName) {
        log.info("🔍 [PlayerService] 플레이어 조회: Name = {}", playerName);
        return playerRepository.getAllPlayers().stream()
            .filter(player -> player.getPlayerId().equalsIgnoreCase(playerName))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("플레이어를 찾을 수 없습니다: " + playerName));
    }

    // ✅ 새로운 플레이어 추가
    public void addPlayer(Player player) {
        log.info("➕ [PlayerService] 플레이어 추가: {}", player.getPlayerId());
        playerRepository.addPlayer(player);
    }

    // ✅ 플레이어 삭제 기능 추가
    public void removePlayer(String playerId) {
        log.info("🗑 [PlayerService] 플레이어 삭제: {}", playerId);
        playerRepository.removePlayer(playerId);
    }

    // ✅ 플레이어의 주식 구매 기능
    public void buyStock(String playerId, String stockName, int quantity) {
        Player player = findPlayerById(playerId);
        Stock stock = stockRepository.findStock(stockName);

        if (stock == null) {
            throw new NotFoundException("해당 주식을 찾을 수 없습니다: " + stockName);
        }

        int totalCost = stock.getStockPrice() * quantity;

        if (player.getPlayerMoney() < totalCost) {
            throw new IllegalArgumentException("잔액이 부족합니다. 필요 금액: " + totalCost + ", 현재 잔액: " + player.getPlayerMoney());
        }

        // 잔액 차감
        player.setPlayerMoney(player.getPlayerMoney() - totalCost);

        // 보유 주식 목록 업데이트
        Optional<PlayerStock> existingStock = player.getPlayerStocks().stream()
            .filter(s -> s.getStockName().equalsIgnoreCase(stockName))
            .findFirst();

        if (existingStock.isPresent()) {
            existingStock.get().setStockQuantity(existingStock.get().getStockQuantity() + quantity);
        } else {
            player.addStock(new PlayerStock(stock, quantity));
        }

        log.info("✅ [PlayerService] {}님이 {} 주식을 {}주 구매 완료!", playerId, stockName, quantity);
    }

    // ✅ 플레이어의 주식 판매 기능
    public void sellStock(String playerId, String stockName, int quantity) {
        Player player = findPlayerById(playerId);
        Stock stock = stockRepository.findStock(stockName);

        if (stock == null) {
            throw new NotFoundException("해당 주식을 찾을 수 없습니다: " + stockName);
        }

        Optional<PlayerStock> existingStock = player.getPlayerStocks().stream()
            .filter(s -> s.getStockName().equalsIgnoreCase(stockName))
            .findFirst();

        if (existingStock.isEmpty()) {
            throw new IllegalArgumentException("보유한 주식이 없습니다: " + stockName);
        }

        PlayerStock playerStock = existingStock.get();

        if (playerStock.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("판매할 주식 수량이 부족합니다. 보유 수량: " + playerStock.getStockQuantity());
        }

        // 잔액 추가
        int totalRevenue = stock.getStockPrice() * quantity;
        player.setPlayerMoney(player.getPlayerMoney() + totalRevenue);

        // 보유 주식 개수 업데이트
        playerStock.setStockQuantity(playerStock.getStockQuantity() - quantity);

        // 만약 주식 수량이 0이 되면 제거
        if (playerStock.getStockQuantity() == 0) {
            player.getPlayerStocks().remove(playerStock);
        }

        log.info("✅ [PlayerService] {}님이 {} 주식을 {}주 판매 완료!", playerId, stockName, quantity);
    }
}
