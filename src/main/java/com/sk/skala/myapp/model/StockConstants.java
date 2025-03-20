package com.sk.skala.myapp.model;

import lombok.experimental.UtilityClass;

/**
 * 상수 클래스로서의 역할을 명확하게 정의
 * - `@UtilityClass`를 사용하여 생성자 자동 차단
 * - JPA 및 파일 처리에서 사용할 상수 값들을 중앙 관리
 */
@UtilityClass
public class StockConstants {

    /** 🔹 CSV 파일 내에서 컬럼을 구분하는 구분자 */
    public static final String CSV_DELIMITER = ",";

    /** 🔹 stock.txt 내 주식 속성 구분자 (예: 종목명:가격:수량) */
    public static final String STOCK_PROPS_DELIMITER = ":";

    /** 🔹 stock.txt 내 종목을 구분하기 위한 구분자 (예: 종목명:가격:수량 | 종목명:가격:수량) */
    public static final String STOCK_DELIMITER = "\\|";

    /** 🔹 JPA 엔티티에서 사용할 테이블 명칭 */
    public static final String TABLE_STOCKS = "stocks";
    public static final String TABLE_PLAYER = "players";
    public static final String TABLE_PLAYER_STOCKS = "player_stocks";
}
