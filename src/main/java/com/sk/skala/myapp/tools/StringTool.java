package com.sk.skala.myapp.tools;

import java.util.regex.Pattern;

public class StringTool {

    // 문자열이 비어 있는지 확인
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    // 여러 문자열 중 하나라도 비어 있는지 확인
    public static boolean isAnyEmpty(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 문자열 배열을 쉼표(,)로 연결
    public static String join(String... strings) {
        StringBuilder buff = new StringBuilder();
        int index = 0;
        for (String str : strings) {
            if (index > 0) {
                buff.append(",");
            }
            buff.append(str);
            index++;
        }
        return buff.toString();
    }

    // SQL 인젝션 공격을 방지하기 위한 특정 문자 제거
    public static String removeInjection(String source) {
        if (source == null) {
            return "";
        }

        Pattern specials = Pattern.compile("['\"\\-#()@;=*/+]");
        String dest = specials.matcher(source).replaceAll("");

        Pattern sql = Pattern.compile("(union|select|from|where)", Pattern.CASE_INSENSITIVE);
        return sql.matcher(dest).replaceAll("");
    }

    // SQL LIKE 검색을 위한 패턴 변환
    public static String like(String string) {
        return "%" + string + "%";
    }

    // 스네이크 케이스(underscore_case)를 카멜 케이스(camelCase)로 변환
    public static String toCamel(String source) {
        if (isEmpty(source)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        boolean toUpper = false;
        for (char ch : source.toCharArray()) {
            if (ch == '_') {
                toUpper = true;
            } else {
                if (toUpper) {
                    builder.append(Character.toUpperCase(ch));
                    toUpper = false;
                } else {
                    builder.append(ch);
                }
            }
        }
        return builder.toString();
    }

    // 카멜 케이스(camelCase)를 스네이크 케이스(underscore_case)로 변환
    public static String toSnake(String source) {
        if (isEmpty(source)) {
            return "";
        }

        StringBuilder buff = new StringBuilder();
        buff.append(Character.toLowerCase(source.charAt(0)));
        for (int i = 1; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (Character.isUpperCase(ch)) {
                buff.append('_').append(Character.toLowerCase(ch));
            } else {
                buff.append(ch);
            }
        }
        return buff.toString();
    }
}
