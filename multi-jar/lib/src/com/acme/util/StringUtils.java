package com.acme.util;

public class StringUtils {
    public static String shout(String input) {
        if (input == null) return "";
        return input.trim().toUpperCase() + "!!!";
    }
}
