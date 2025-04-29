package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class SkalaStockMarket {

    private final PlayerService playerService;
    private final StockService stockService;
    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository;
    private final AOPService aopService;

    private Player player = null;

    public SkalaStockMarket(PlayerService playerService, StockService stockService, 
                            PlayerRepository playerRepository, StockRepository stockRepository,
                            AOPService aopService) {
        this.playerService = playerService;
        this.stockService = stockService;
        this.playerRepository = playerRepository;
        this.stockRepository = stockRepository;
        this.aopService = aopService;
    }

    @Transactional
    public void start() {
        if (isLocalProfile()) {
            runWithScanner();
        } else {
            log.info("✅ 운영 환경에서는 API 서버 모드로만 실행됩니다. Scanner 입력을 받지 않습니다.");
        }
    }

    private boolean isLocalProfile() {
        String profile = System.getProperty("spring.profiles.active", "default");
        return profile.equals("local");
    }

    private void runWithScanner() {
        try (Scanner scanner = new Scanner(System.in)) {
            player = getOrCreatePlayer(scanner);
            displayPlayerStocks();
    
            boolean running = true;
            while (running) {
                try {
                    running = processMenu(scanner);
                } catch (InputMismatchException e) {
                    log.warn("입력 오류: 올바른 숫자를 입력하세요.", e);
                    System.out.println("❌ 올바른 숫자를 입력하세요.");
                    scanner.nextLine(); // 입력 버퍼 초기화
                }
            }
        }
    }

    private Player getOrCreatePlayer(Scanner scanner) {
        System.out.print("플레이어 ID를 입력하세요: ");
        String playerId = scanner.nextLine();
        
        return playerRepository.findById(playerId).orElseGet(() -> {
            log.info("🆕 새로운 플레이어 생성: {}", playerId);
            System.out.print("초기 투자금을 입력하세요: ");

            while (!scanner.hasNextInt()) {
                System.out.println("🚨 숫자를 입력하세요.");
                scanner.next();
            }
            int money = scanner.nextInt();
            scanner.nextLine();

            Player newPlayer = new Player(playerId, money);
            playerRepository.save(newPlayer);
            return newPlayer;
        });
    }

    private boolean processMenu(Scanner scanner) {
        System.out.println("\n=== 🏦 스칼라 주식 프로그램 메뉴 ===");
        System.out.println("1. 📌 보유 주식 목록");
        System.out.println("2. 🛒 주식 구매");
        System.out.println("3. 💰 주식 판매");
        System.out.println("4. 🔄 액션 실행");
        System.out.println("0. 🚪 프로그램 종료");
        System.out.print("👉 선택: ");

        while (!scanner.hasNextInt()) {
            System.out.println("🚨 숫자를 입력하세요.");
            scanner.next();
        }
        int code = scanner.nextInt();
        scanner.nextLine();

        switch (code) {
            case 1 -> displayPlayerStocks();
            case 2 -> buyStock(scanner);
            case 3 -> sellStock(scanner);
            case 4 -> executeAopAction();
            case 0 -> {
                log.info("프로그램 종료");
                System.out.println("🔚 프로그램을 종료합니다...Bye");
                return false;
            }
            default -> {
                log.warn("❌ 잘못된 메뉴 선택: {}", code);
                System.out.println("🚨 올바른 번호를 선택하세요.");
            }
        }
        return true;
    }

    private void executeAopAction() {
        log.debug("✅ AOP Service 실행");
        aopService.logAction("액션 실행");
    }

    private void displayPlayerStocks() {
        log.info("📢 플레이어 정보 표시 - ID: {}, 잔액: {}", player.getPlayerId(), player.getPlayerMoney());
        System.out.println("\n📌 [플레이어 정보]");
        System.out.println("- 🆔 ID: " + player.getPlayerId());
        System.out.println("- 💰 잔액: " + player.getPlayerMoney());
        System.out.println("- 📜 보유 주식 목록");
        player.getPlayerStocks().forEach(System.out::println);
    }

    private void displayStockList() {
        log.info("📢 주식 목록 표시");
        List<Stock> stocks = stockRepository.findAll();
        System.out.println("\n📈 [주식 목록]");
        for (int i = 0; i < stocks.size(); i++) {
            System.out.println((i + 1) + ". " + stocks.get(i));
        }
    }

    @Transactional
    private void buyStock(Scanner scanner) {
        System.out.println("\n🛒 구매할 주식 번호를 선택하세요:");
        displayStockList();

        System.out.print("👉 선택: ");
        while (!scanner.hasNextInt()) { 
            System.out.println("🚨 숫자를 입력하세요.");
            scanner.next();
        }
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        List<Stock> stocks = stockRepository.findAll();
        if (index < 0 || index >= stocks.size()) {
            System.out.println("🚨 잘못된 선택입니다.");
            return;
        }

        Stock selectedStock = stocks.get(index);

        System.out.print("🛍 구매할 수량을 입력하세요: ");
        while (!scanner.hasNextInt()) {
            System.out.println("🚨 숫자를 입력하세요.");
            scanner.next();
        }
        int quantity = scanner.nextInt();
        scanner.nextLine();

        playerService.buyStock(player.getPlayerId(), selectedStock.getStockName(), quantity);
        System.out.println("✅ " + quantity + "주를 구매했습니다! 남은 금액: " + player.getPlayerMoney());
    }

    @Transactional
    private void sellStock(Scanner scanner) {
        System.out.println("\n💰 판매할 주식 번호를 선택하세요:");
        displayPlayerStocks();

        System.out.print("👉 선택: ");
        while (!scanner.hasNextInt()) {
            System.out.println("🚨 숫자를 입력하세요.");
            scanner.next();
        }
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        List<PlayerStock> stocks = player.getPlayerStocks();
        if (index < 0 || index >= stocks.size()) {
            System.out.println("🚨 잘못된 선택입니다.");
            return;
        }

        PlayerStock playerStock = stocks.get(index);
        System.out.print("📉 판매할 수량을 입력하세요: ");

        while (!scanner.hasNextInt()) {
            System.out.println("🚨 숫자를 입력하세요.");
            scanner.next();
        }
        int quantity = scanner.nextInt();
        scanner.nextLine();

        playerService.sellStock(player.getPlayerId(), playerStock.getStockName(), quantity);
        System.out.println("✅ " + quantity + "주 판매 완료! 현재 잔액: " + player.getPlayerMoney());
    }
}
