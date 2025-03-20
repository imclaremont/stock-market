package com.sk.skala.myapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.repository.PlayerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
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
}
