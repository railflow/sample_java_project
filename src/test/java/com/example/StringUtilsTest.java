package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    // --- reverse ---
    @Test
    void testReverseNormal() {
        assertEquals("olleh", StringUtils.reverse("hello"));
    }

    @Test
    void testReverseEmpty() {
        assertEquals("", StringUtils.reverse(""));
    }

    @Test
    void testReverseSingleChar() {
        assertEquals("a", StringUtils.reverse("a"));
    }

    @Test
    void testReverseNull() {
        assertNull(StringUtils.reverse(null));
    }

    // --- isPalindrome (BUGGY: case-sensitive) ---
    @Test
    void testIsPalindromeLowercase() {
        assertTrue(StringUtils.isPalindrome("racecar"));
    }

    @Test
    void testIsPalindromeMixedCase() {
        // BUG: returns false because comparison is case-sensitive
        assertTrue(StringUtils.isPalindrome("Racecar"));
    }

    @Test
    void testIsPalindromeUppercase() {
        // BUG: returns false because comparison is case-sensitive
        assertTrue(StringUtils.isPalindrome("LEVEL"));
    }

    @Test
    void testIsPalindromeWithSpaces() {
        assertTrue(StringUtils.isPalindrome("A man a plan a canal Panama".replaceAll(" ", "").toLowerCase()));
    }

    @Test
    void testNotPalindrome() {
        assertFalse(StringUtils.isPalindrome("hello"));
    }

    // --- countVowels (BUGGY: misses uppercase vowels) ---
    @Test
    void testCountVowelsLowercase() {
        assertEquals(2, StringUtils.countVowels("hello"));
    }

    @Test
    void testCountVowelsUppercase() {
        // BUG: returns 0 instead of 3
        assertEquals(3, StringUtils.countVowels("AEI"));
    }

    @Test
    void testCountVowelsMixed() {
        // BUG: only counts 'e', missing 'E' and 'A'
        assertEquals(3, StringUtils.countVowels("hEllo wArld"));
    }

    @Test
    void testCountVowelsNoVowels() {
        assertEquals(0, StringUtils.countVowels("gym"));
    }

    @Test
    void testCountVowelsEmpty() {
        assertEquals(0, StringUtils.countVowels(""));
    }

    // --- capitalizeWords ---
    @Test
    void testCapitalizeWordsNormal() {
        assertEquals("Hello World", StringUtils.capitalizeWords("hello world"));
    }

    @Test
    void testCapitalizeWordsSingle() {
        assertEquals("Java", StringUtils.capitalizeWords("java"));
    }

    @Test
    void testCapitalizeWordsEmpty() {
        assertEquals("", StringUtils.capitalizeWords(""));
    }

    @Test
    void testCapitalizeWordsNull() {
        assertNull(StringUtils.capitalizeWords(null));
    }

    // --- truncate (BUGGY: off-by-one with <) ---
    @Test
    void testTruncateLong() {
        assertEquals("Hello...", StringUtils.truncate("Hello World", 5));
    }

    @Test
    void testTruncateExactLength() {
        // BUG: "Hello" has length 5, but condition is s.length() < 5, so 5 is NOT less than 5
        // therefore it truncates to "Hello..." instead of returning "Hello"
        assertEquals("Hello", StringUtils.truncate("Hello", 5));
    }

    @Test
    void testTruncateShort() {
        assertEquals("Hi", StringUtils.truncate("Hi", 10));
    }

    @Test
    void testTruncateNull() {
        assertNull(StringUtils.truncate(null, 5));
    }

    // --- misc ---
    @Test
    void testIsNullOrEmptyNull() {
        assertTrue(StringUtils.isNullOrEmpty(null));
    }

    @Test
    void testIsNullOrEmptyEmpty() {
        assertTrue(StringUtils.isNullOrEmpty(""));
    }

    @Test
    void testIsNullOrEmptyNonEmpty() {
        assertFalse(StringUtils.isNullOrEmpty("hello"));
    }

    @Test
    void testRepeat() {
        assertEquals("abcabc", StringUtils.repeat("abc", 2));
    }

    @Test
    void testCountOccurrences() {
        assertEquals(3, StringUtils.countOccurrences("banana", "a"));
    }
}
