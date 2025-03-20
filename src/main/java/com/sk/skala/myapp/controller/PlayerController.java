package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Player API", description = "플레이어 관리 API")
@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "전체 플레이어 조회", description = "모든 플레이어 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @Operation(summary = "플레이어 ID로 조회", description = "ID에 해당하는 플레이어 정보를 조회합니다.")
    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String playerId) {
        return ResponseEntity.ok(playerService.findPlayerById(playerId));
    }

    @Operation(summary = "플레이어 이름으로 조회", description = "이름에 해당하는 플레이어 정보를 조회합니다.")
    @GetMapping("/name/{playerName}")
    public ResponseEntity<Player> getPlayerByName(@PathVariable String playerName) {
        return ResponseEntity.ok(playerService.findPlayerByName(playerName));
    }

    @Operation(summary = "새로운 플레이어 추가", description = "새로운 플레이어를 추가합니다.")
    @PostMapping
    public ResponseEntity<Void> addPlayer(@RequestBody Player player) {
        playerService.addPlayer(player);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "플레이어 정보 수정", description = "기존 플레이어 정보를 수정합니다.")
    @PutMapping("/{playerId}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String playerId, @RequestBody Player updatedPlayer) {
        Player player = playerService.findPlayerById(playerId);
        player.setPlayerMoney(updatedPlayer.getPlayerMoney());
        player.setPlayerStocks(updatedPlayer.getPlayerStocks());

        playerService.addPlayer(player);
        return ResponseEntity.ok(player);
    }

    @Operation(summary = "플레이어 삭제", description = "특정 플레이어를 삭제합니다.")
    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String playerId) {
        playerService.removePlayer(playerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "플레이어의 주식 정보 조회", description = "특정 플레이어가 보유한 주식 정보를 조회합니다.")
    @GetMapping("/{playerId}/stocks")
    public ResponseEntity<List<PlayerStock>> getPlayerStocks(@PathVariable String playerId) {
        Player player = playerService.findPlayerById(playerId);
        return ResponseEntity.ok(player.getPlayerStocks());
    }

    @Operation(summary = "플레이어 주식 구매", description = "플레이어가 주식을 구매합니다.")
    @PostMapping("/{playerId}/buy")
    public ResponseEntity<String> buyStock(@PathVariable String playerId, @RequestParam String stockName, @RequestParam int quantity) {
        playerService.buyStock(playerId, stockName, quantity);
        return ResponseEntity.ok("✅ 주식 구매 완료: " + stockName + " (" + quantity + "주)");
    }

    @Operation(summary = "플레이어 주식 판매", description = "플레이어가 주식을 판매합니다.")
    @PostMapping("/{playerId}/sell")
    public ResponseEntity<String> sellStock(@PathVariable String playerId, @RequestParam String stockName, @RequestParam int quantity) {
        playerService.sellStock(playerId, stockName, quantity);
        return ResponseEntity.ok("✅ 주식 판매 완료: " + stockName + " (" + quantity + "주)");
    }
}
