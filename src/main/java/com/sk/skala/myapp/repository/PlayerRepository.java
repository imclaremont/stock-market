package com.sk.skala.myapp.repository;

import org.springframework.stereotype.Repository;
import com.sk.skala.myapp.exception.NotFoundException;
import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.StockConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class PlayerRepository {
    private final String PLAYER_FILE = "/config/workspace/basic/backend-spring/myapp/src/main/resources/data/players.txt";
    private final List<Player> playerList = new ArrayList<>();

    // ✅ 모든 플레이어 목록 가져오기
    public List<Player> getAllPlayers() {
        return playerList;
    }

    public void loadPlayerList() {
        File file = new File(PLAYER_FILE);
        if (!file.exists()) {
            log.warn("📂 플레이어 정보 파일이 없습니다. 새 파일을 생성합니다.");
            savePlayerList(); 
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = parseLineToPlayer(line);
                if (player != null) {
                    playerList.add(player);
                }
            }
            log.info("✅ 플레이어 정보 로드 완료: {}명", playerList.size());
        } catch (IOException e) {
            log.error("❌ 플레이어 정보 파일 읽기 오류: {}", e.getMessage());
        }
    }

    public void savePlayerList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_FILE))) {
            for (Player player : playerList) {
                writer.write(player.getPlayerId() + StockConstants.DELIMITER + player.getPlayerMoney());
                if (!player.getPlayerStocks().isEmpty()) {
                    writer.write(StockConstants.DELIMITER + player.getPlayerStocksForFile());
                }
                writer.newLine();
            }
            log.info("💾 플레이어 정보 저장 완료");
        } catch (IOException e) {
            log.error("❌ 파일 저장 중 오류 발생: {}", e.getMessage());
        }
    }

    private Player parseLineToPlayer(String line) {
        String[] fields = line.split(StockConstants.DELIMITER);
        if (fields.length < 2) {
            log.warn("⚠ 잘못된 데이터 형식: {}", line);
            return null;
        }

        Player player = new Player();
        player.setPlayerId(fields[0]);
        player.setPlayerMoney(Integer.parseInt(fields[1]));

        if (fields.length > 2 && fields[2].contains(StockConstants.STOCK_PROPS_DELIMITER)) {
            player.setPlayerStocks(parseFieldToStockList(fields[2]));
        }

        return player;
    }

    private List<PlayerStock> parseFieldToStockList(String field) {
        List<PlayerStock> list = new ArrayList<>();
        String[] stocks = field.split(StockConstants.STOCK_DELIMITER);

        for (String stockData : stocks) {
            String[] props = stockData.split(StockConstants.STOCK_PROPS_DELIMITER);
            if (props.length == 3) {
                list.add(new PlayerStock(props[0], props[1], props[2]));
            }
        }
        return list;
    }

    public Player findPlayer(String id) {
        return Optional.ofNullable(
                playerList.stream()
                        .filter(player -> player.getPlayerId().equals(id))
                        .findFirst()
                        .orElse(null)
        ).orElseThrow(() -> new NotFoundException("플레이어를 찾을 수 없습니다: " + id));
    }

    public void addPlayer(Player player) {
        playerList.add(player);
        savePlayerList();
        log.info("➕ 플레이어 추가 완료: {}", player.getPlayerId());
    }

    public void removePlayer(String id) {
        Player player = findPlayer(id);
        playerList.remove(player);
        savePlayerList();
        log.info("🗑 플레이어 삭제 완료: {}", id);
    }
}
