package com.example;

public class StringUtils {

    public static String reverse(String s) {
        if (s == null) return null;
        return new StringBuilder(s).reverse().toString();
    }

    // BUG: case-sensitive comparison (treats "Racecar" as not palindrome)
    public static boolean isPalindrome(String s) {
        if (s == null) return false;
        String cleaned = s.replaceAll("[^a-zA-Z0-9]", "");
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    // BUG: only counts lowercase vowels, misses uppercase
    public static int countVowels(String s) {
        if (s == null) return 0;
        int count = 0;
        for (char c : s.toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
    }

    public static String capitalizeWords(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else {
                result.append(capitalizeNext ? Character.toUpperCase(c) : c);
                capitalizeNext = false;
            }
        }
        return result.toString();
    }

    // BUG: uses < instead of <= (off-by-one), so exact-length strings get truncated
    public static String truncate(String s, int maxLength) {
        if (s == null) return null;
        if (s.length() < maxLength) {
            return s;
        }
        return s.substring(0, maxLength) + "...";
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String repeat(String s, int times) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static int countOccurrences(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty()) return 0;
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
}
