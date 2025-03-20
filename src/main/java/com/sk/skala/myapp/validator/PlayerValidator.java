package com.sk.skala.myapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.sk.skala.myapp.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PlayerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;
        log.debug("🔍 [PlayerValidator] 유효성 검사 시작: {}", player.getPlayerId());

        // 플레이어 머니가 0 이상인지 검사
        if (player.getPlayerMoney() < 0) {
            log.warn("❌ [유효성 검사 실패] 플레이어 머니가 음수임: {}", player.getPlayerMoney());
            errors.rejectValue("playerMoney", "negative.value", "💰 플레이어 머니는 0 이상이어야 합니다.");
        }

        // 보유 주식 목록이 비어 있는지 검사
        if (player.getPlayerStocks() == null || player.getPlayerStocks().isEmpty()) {
            log.warn("❌ [유효성 검사 실패] 플레이어가 보유한 주식이 없음: {}", player.getPlayerId());
            errors.rejectValue("playerStocks", "empty.list", "📉 플레이어의 보유 주식 목록은 비어있을 수 없습니다.");
        }

        log.info("✅ [PlayerValidator] 유효성 검사 완료: {}", player.getPlayerId());
    }
}
