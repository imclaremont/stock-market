package com.sk.skala.myapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.exception.BadRequestException;
import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.StockRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerService {
    @Autowired // 🔹 명시적 주입 확인
    private final PlayerRepository playerRepository;
    @Autowired // 🔹 명시적 주입 확인
    private final StockRepository stockRepository; 

    public PlayerService(PlayerRepository playerRepository, StockRepository stockRepository) {
        this.playerRepository = playerRepository;
        this.stockRepository = stockRepository;
    }

    public List<Player> getAllPlayers() {
        log.debug("✅ [PlayerService] 모든 플레이어 조회 요청");
    
        List<Player> players = playerRepository.findAll();
        
        if (players == null || players.isEmpty()) {
            log.warn("⚠️ [PlayerService] 등록된 플레이어가 없음.");
            return new ArrayList<>();  // 🔹 Null 대신 빈 리스트 반환
        }
    
        log.info("✅ [PlayerService] 조회된 플레이어 수: {}", players.size());
        return players;
    }

    // ✅ 특정 ID로 플레이어 조회 (예외 처리 추가)
    public Player findPlayerById(String playerId) {
        log.debug("🔍 [PlayerService] 플레이어 조회: ID = {}", playerId);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("플레이어를 찾을 수 없습니다: " + playerId));

        // ✅ 플레이어의 주식 목록이 null이면 빈 리스트로 설정
        if (player.getPlayerStocks() == null) {
            log.warn("⚠️ [PlayerService] 플레이어 '{}'의 playerStocks가 null! 빈 리스트로 초기화", playerId);
            player.setPlayerStocks(new ArrayList<>());
        }

        return player;
    }

    // ✅ 특정 플레이어를 이름 기준으로 조회
    public Player findPlayerByName(String playerName) {
        log.debug("🔍 [PlayerService] 플레이어 조회 (이름): {}", playerName);
    
        return playerRepository.findByPlayerId(playerName)
                .orElseThrow(() -> new NotFoundException("플레이어를 찾을 수 없습니다: " + playerName));
    }

    // ✅ 플레이어 추가 (playerStocks 초기화 보장)
    @Transactional
    public void addPlayer(Player player) {
        log.debug("➕ [PlayerService] 플레이어 추가: {}", player.getPlayerId());

        if (player.getPlayerId() == null || player.getPlayerId().isBlank()) {
            throw new BadRequestException("플레이어 ID는 필수입니다.");
        }

        if (player.getPlayerStocks() == null) {
            player.setPlayerStocks(new ArrayList<>()); // 🔹 Null 방지
        }

        playerRepository.save(player);
        log.info("✅ [PlayerService] 플레이어 '{}' 추가 완료", player.getPlayerId());
    }

    // ✅ 플레이어 삭제 (예외 처리 추가)
    @Transactional
    public void removePlayer(String playerId) {
        log.info("🗑 [PlayerService] 플레이어 삭제: {}", playerId);

        if (!playerRepository.existsById(playerId)) {
            throw new NotFoundException("플레이어를 찾을 수 없습니다: " + playerId);
        }

        playerRepository.deleteById(playerId);
    }

    // ✅ 플레이어의 주식 구매 기능 (방어 코드 추가)
    @Transactional
    public void buyStock(String playerId, String stockName, int quantity) {
        Player player = findPlayerById(playerId);

        Stock stock = stockRepository.findByStockName(stockName)
                .orElseThrow(() -> new NotFoundException("해당 주식을 찾을 수 없습니다: " + stockName));

        if (quantity <= 0) {
            throw new BadRequestException("구매 수량은 1 이상이어야 합니다.");
        }

        int totalCost = stock.getStockPrice() * quantity;
        if (player.getPlayerMoney() < totalCost) {
            throw new BadRequestException("잔액이 부족합니다. 필요 금액: " + totalCost + ", 현재 잔액: " + player.getPlayerMoney());
        }

        if (player.getPlayerStocks() == null) {
            player.setPlayerStocks(new ArrayList<>());
        }

        Optional<PlayerStock> existingStock = player.getPlayerStocks().stream()
            .filter(s -> s.getStockName().equalsIgnoreCase(stockName))
            .findFirst();

        if (existingStock.isPresent()) {
            existingStock.get().setStockQuantity(existingStock.get().getStockQuantity() + quantity);
        } else {
            player.addStock(new PlayerStock(stock, quantity, player));
        }

        player.setPlayerMoney(player.getPlayerMoney() - totalCost);
        playerRepository.save(player);

        log.info("✅ [PlayerService] {}님이 {} 주식을 {}주 구매 완료!", playerId, stockName, quantity);
    }

    // ✅ 플레이어의 주식 판매 기능
    @Transactional
    public void sellStock(String playerId, String stockName, int quantity) {
        Player player = findPlayerById(playerId);

        Stock stock = stockRepository.findByStockName(stockName)
                .orElseThrow(() -> new NotFoundException("해당 주식을 찾을 수 없습니다: " + stockName));

        Optional<PlayerStock> existingStock = player.getPlayerStocks().stream()
            .filter(s -> s.getStockName().equalsIgnoreCase(stockName))
            .findFirst();

        if (existingStock.isEmpty()) {
            throw new BadRequestException("보유한 주식이 없습니다: " + stockName);
        }

        PlayerStock playerStock = existingStock.get();
        if (playerStock.getStockQuantity() < quantity) {
            throw new BadRequestException("판매할 주식 수량이 부족합니다. 보유 수량: " + playerStock.getStockQuantity());
        }

        playerStock.setStockQuantity(playerStock.getStockQuantity() - quantity);

        if (playerStock.getStockQuantity() == 0) {
            player.getPlayerStocks().remove(playerStock);
        }

        int totalRevenue = stock.getStockPrice() * quantity;
        player.setPlayerMoney(player.getPlayerMoney() + totalRevenue);
        playerRepository.save(player);

        log.info("✅ [PlayerService] {}님이 {} 주식을 {}주 판매 완료!", playerId, stockName, quantity);
    }
}
