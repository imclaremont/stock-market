package com.sk.skala.myapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    @GetMapping
    public ResponseEntity<String> getAllPlayers() {
        return ResponseEntity.ok("플레이어 전체 목록 조회");
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<Map<String, String>> getPlayerById(@PathVariable String playerId) {
        return ResponseEntity.ok(Collections.singletonMap("playerId", playerId));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addPlayer(@RequestBody Map<String, String> playerData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerData);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<Map<String, String>> updatePlayer(@PathVariable String playerId, @RequestBody Map<String, String> updatedData) {
        return ResponseEntity.ok(updatedData);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> deletePlayer(@PathVariable String playerId) {
        return ResponseEntity.noContent().build();
    }
}
