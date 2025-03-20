package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    /**
     * ✅ 특정 플레이어 조회 (ID 기준)
     */
    default Player findPlayerById(String id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("플레이어를 찾을 수 없습니다: " + id));
    }
}
