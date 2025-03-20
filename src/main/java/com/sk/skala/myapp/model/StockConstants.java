package com.sk.skala.myapp.model;

/**
 * ✅ 상수 클래스로서의 역할을 명확하게 정의
 * - `final`을 사용하여 상속 방지
 * - `private 생성자`를 사용하여 인스턴스 생성 차단
 */
public final class StockConstants {

    private StockConstants() {
        throw new UnsupportedOperationException("StockConstants 클래스는 인스턴스화할 수 없습니다.");
    }

    /** 🔹 텍스트 파일 내에서 컬럼을 구분하는 구분자 */
    public static final String DELIMITER = ",";

    /** 🔹 stock.txt 내 주식 속성 구분자 (예: 종목명:가격:수량) */
    public static final String STOCK_PROPS_DELIMITER = ":";

    /** 🔹 stock.txt 내 종목을 구분하기 위한 구분자 (예: 종목명:가격:수량|종목명:가격:수량) */
    public static final String STOCK_DELIMITER = "\\|";
}
