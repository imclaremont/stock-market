package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.service.PlayerService;
import com.sk.skala.myapp.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Player API", description = "플레이어 관리 API")
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "전체 플레이어 조회", description = "모든 플레이어 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @Operation(summary = "특정 플레이어 조회", description = "플레이어 ID로 특정 플레이어 정보를 조회합니다.")
    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String playerId) {
        Player player = playerService.findPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

    @Operation(summary = "새로운 플레이어 추가", description = "새로운 플레이어를 추가합니다.")
    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        playerService.addPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @Operation(summary = "플레이어 정보 수정", description = "기존 플레이어 정보를 수정합니다.")
    @PutMapping("/{playerId}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String playerId, @RequestBody Player updatedPlayer) {
        Player existingPlayer = playerService.findPlayerById(playerId);

        existingPlayer.setPlayerMoney(updatedPlayer.getPlayerMoney());
        existingPlayer.setPlayerStocks(updatedPlayer.getPlayerStocks());

        return ResponseEntity.ok(existingPlayer);
    }

    @Operation(summary = "플레이어 삭제", description = "특정 플레이어를 삭제합니다.")
    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String playerId) {
        playerService.removePlayer(playerId);
        return ResponseEntity.noContent().build();
    }
}
