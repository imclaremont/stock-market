package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.Optional;
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

    @Autowired
    public SkalaStockMarket(PlayerService playerService, StockService stockService, 
                            PlayerRepository playerRepository, StockRepository stockRepository,
                            AOPService aopService) {
        this.playerService = playerService;
        this.stockService = stockService;
        this.playerRepository = playerRepository;
        this.stockRepository = stockRepository;
        this.aopService = aopService;
    }

    public void start() {
        playerRepository.loadPlayerList();

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
        player = playerService.findPlayerById(playerId);

        if (player == null) {
            log.info("🆕 새로운 플레이어 생성: {}", playerId);
            player = new Player(playerId);

            System.out.print("초기 투자금을 입력하세요: ");
            while (!scanner.hasNextInt()) { // 숫자가 아닌 경우 방지
                System.out.println("🚨 숫자를 입력하세요.");
                scanner.next(); // 버퍼 초기화
            }
            int money = scanner.nextInt();
            scanner.nextLine(); // 버퍼 초기화

            player.setPlayerMoney(money);
            playerService.addPlayer(player); // ✅ 새 플레이어를 추가하여 저장
        }
        return player;
    }

    private boolean processMenu(Scanner scanner) {
        System.out.println("\n=== 🏦 스칼라 주식 프로그램 메뉴 ===");
        System.out.println("1. 📌 보유 주식 목록");
        System.out.println("2. 🛒 주식 구매");
        System.out.println("3. 💰 주식 판매");
        System.out.println("4. 🔄 액션 실행");
        System.out.println("0. 🚪 프로그램 종료");
        System.out.print("👉 선택: ");

        while (!scanner.hasNextInt()) { // 숫자가 아닌 경우 방지
            System.out.println("🚨 숫자를 입력하세요.");
            scanner.next(); // 버퍼 초기화
        }
        int code = scanner.nextInt();
        scanner.nextLine(); // 버퍼 초기화

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
        System.out.println(player.getPlayerStocksForMenu());
    }

    private void displayStockList() {
        log.info("📢 주식 목록 표시");
        System.out.println("\n📈 [주식 목록]");
        System.out.println(stockRepository.getStockListForMenu());
    }
    
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

        if (index < 0 || index >= stockRepository.getStockList().size()) {
            System.out.println("🚨 잘못된 선택입니다.");
            return;
        }

        Stock selectedStock = stockRepository.findStock(index);
        if (selectedStock == null) {
            System.out.println("🚨 해당 주식을 찾을 수 없습니다.");
            return;
        }

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

        if (index < 0 || index >= player.getPlayerStocks().size()) {
            System.out.println("🚨 잘못된 선택입니다.");
            return;
        }

        PlayerStock playerStock = player.getPlayerStocks().get(index);
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
