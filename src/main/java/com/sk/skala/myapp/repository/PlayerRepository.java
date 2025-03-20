package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    /**
     * ✅ 특정 플레이어 조회 (ID 기준)
     */
    default Player findPlayerById(String id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("플레이어를 찾을 수 없습니다: " + id));
    }

    /**
     * ✅ 특정 플레이어 조회 (이름 기준)
     */
    Optional<Player> findByPlayerId(String playerName);
}
