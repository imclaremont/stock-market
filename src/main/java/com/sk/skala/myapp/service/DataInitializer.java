package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializer {

    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        // ✅ 샘플 주식 데이터 생성
        Stock apple = new Stock("Apple", 150);
        Stock google = new Stock("Google", 2800);
        Stock amazon = new Stock("Amazon", 3400);
        Stock tesla = new Stock("Tesla", 1200);
        Stock samsung = new Stock("Samsung", 700);

        stockRepository.saveAll(List.of(apple, google, amazon, tesla, samsung));

        // ✅ 샘플 플레이어 데이터 생성 (playerStocks 빈 리스트 설정)
        Player player1 = new Player("player001", 10000, new ArrayList<>());
        Player player2 = new Player("player002", 20000, new ArrayList<>());
        Player player3 = new Player("player003", 15000, new ArrayList<>());

        // ✅ 플레이어의 주식 보유 데이터 추가
        PlayerStock ps1 = new PlayerStock(apple, 2, player1);
        PlayerStock ps2 = new PlayerStock(google, 1, player1);
        PlayerStock ps3 = new PlayerStock(amazon, 3, player2);
        PlayerStock ps4 = new PlayerStock(tesla, 4, player2);
        PlayerStock ps5 = new PlayerStock(samsung, 5, player3);

        player1.addStock(ps1);
        player1.addStock(ps2);
        player2.addStock(ps3);
        player2.addStock(ps4);
        player3.addStock(ps5);

        playerRepository.saveAll(List.of(player1, player2, player3));

        System.out.println("✅ 더미 데이터가 초기화되었습니다.");
    }
}
