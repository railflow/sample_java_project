package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    // --- add ---
    @Test
    void testAddPositive() {
        assertEquals(5, MathUtils.add(2, 3));
    }

    @Test
    void testAddWithZero() {
        assertEquals(7, MathUtils.add(7, 0));
    }

    @Test
    void testAddNegative() {
        assertEquals(-1, MathUtils.add(-3, 2));
    }

    @Test
    void testAddBothNegative() {
        assertEquals(-5, MathUtils.add(-2, -3));
    }

    // --- subtract ---
    @Test
    void testSubtractPositive() {
        assertEquals(3, MathUtils.subtract(5, 2));
    }

    @Test
    void testSubtractResultNegative() {
        assertEquals(-2, MathUtils.subtract(3, 5));
    }

    @Test
    void testSubtractZero() {
        assertEquals(4, MathUtils.subtract(4, 0));
    }

    // --- multiply ---
    @Test
    void testMultiplyPositive() {
        assertEquals(12, MathUtils.multiply(3, 4));
    }

    @Test
    void testMultiplyByZero() {
        assertEquals(0, MathUtils.multiply(5, 0));
    }

    @Test
    void testMultiplyNegative() {
        assertEquals(-6, MathUtils.multiply(-2, 3));
    }

    // --- divide (BUGGY: integer division) ---
    @Test
    void testDivideExact() {
        assertEquals(2.0, MathUtils.divide(6, 3), 0.001);
    }

    @Test
    void testDivideDecimalResult() {
        // BUG: returns 2.0 instead of 2.5
        assertEquals(2.5, MathUtils.divide(5, 2), 0.001);
    }

    @Test
    void testDivideOneThird() {
        // BUG: returns 0.0 instead of 0.333...
        assertEquals(0.333, MathUtils.divide(1, 3), 0.001);
    }

    @Test
    void testDivideByZeroThrows() {
        assertThrows(ArithmeticException.class, () -> MathUtils.divide(5, 0));
    }

    @Test
    void testDivideNegative() {
        // BUG: -1/2 = 0 in integer division, not -0.5
        assertEquals(-0.5, MathUtils.divide(-1, 2), 0.001);
    }

    // --- power (BUGGY: returns 0 for exponent 0) ---
    @Test
    void testPowerNormal() {
        assertEquals(8.0, MathUtils.power(2, 3), 0.001);
    }

    @Test
    void testPowerZeroExponent() {
        // BUG: returns 0 instead of 1
        assertEquals(1.0, MathUtils.power(5, 0), 0.001);
    }

    @Test
    void testPowerZeroBase() {
        assertEquals(0.0, MathUtils.power(0, 3), 0.001);
    }

    @Test
    void testPowerOne() {
        assertEquals(1.0, MathUtils.power(1, 100), 0.001);
    }

    // --- modulo (BUGGY: throws on negative) ---
    @Test
    void testModuloPositive() {
        assertEquals(1, MathUtils.modulo(7, 3));
    }

    @Test
    void testModuloNegativeDividend() {
        // BUG: throws IllegalArgumentException
        assertEquals(-1, MathUtils.modulo(-7, 3));
    }

    @Test
    void testModuloZeroResult() {
        assertEquals(0, MathUtils.modulo(6, 3));
    }

    // --- misc ---
    @Test
    void testAbsolute() {
        assertEquals(5.0, MathUtils.absolute(-5.0), 0.001);
    }

    @Test
    void testSquareRoot() {
        assertEquals(3.0, MathUtils.squareRoot(9.0), 0.001);
    }

    @Test
    void testSquareRootNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.squareRoot(-1));
    }

    @Test
    void testMax() {
        assertEquals(7, MathUtils.max(3, 7));
    }

    @Test
    void testMin() {
        assertEquals(3, MathUtils.min(3, 7));
    }
}
