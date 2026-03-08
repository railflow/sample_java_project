package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class OrderCalculatorTest {

    private OrderCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new OrderCalculator(100.0);
    }

    // --- constructor ---
    @Test
    void testInitialTotal() {
        assertEquals(100.0, calc.getTotal(), 0.001);
    }

    @Test
    void testNegativeTotalThrows() {
        assertThrows(IllegalArgumentException.class, () -> new OrderCalculator(-1));
    }

    // --- computeTotal ---
    @Test
    void testComputeTotal() {
        double[] prices = {10.0, 20.0, 30.0};
        assertEquals(60.0, calc.computeTotal(prices), 0.001);
    }

    @Test
    void testComputeTotalEmpty() {
        double[] prices = {};
        assertEquals(0.0, calc.computeTotal(prices), 0.001);
    }

    @Test
    void testComputeTotalSingle() {
        double[] prices = {99.99};
        assertEquals(99.99, calc.computeTotal(prices), 0.001);
    }

    // --- applyDiscount (BUGGY: double-discount on second call) ---
    @Test
    void testApplyDiscountTenPercent() {
        assertEquals(90.0, calc.applyDiscount(10), 0.001);
    }

    @Test
    void testApplyDiscountZero() {
        assertEquals(100.0, calc.applyDiscount(0), 0.001);
    }

    @Test
    void testApplyDiscountFull() {
        assertEquals(0.0, calc.applyDiscount(100), 0.001);
    }

    @Test
    void testApplyDiscountIdempotent() {
        // BUG: second call applies 10% to already-discounted 90, giving 81 instead of 90
        calc.applyDiscount(10);
        double secondResult = calc.applyDiscount(10);
        assertEquals(90.0, secondResult, 0.001);
    }

    @Test
    void testApplyDiscountInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.applyDiscount(-5));
    }

    @Test
    void testApplyDiscountOver100Throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.applyDiscount(101));
    }

    // --- computeTax (BUGGY: rounds down instead of up) ---
    @Test
    void testComputeTaxExact() {
        // 100 * 10% = 10.0, no rounding needed
        assertEquals(10.0, calc.computeTax(10), 0.001);
    }

    @Test
    void testComputeTaxFractional() {
        // 100 * 8.875% = 8.875, should round to 8.88 but floors to 8.87
        assertEquals(8.88, calc.computeTax(8.875), 0.001);
    }

    @Test
    void testComputeTaxSmall() {
        OrderCalculator small = new OrderCalculator(33.33);
        // 33.33 * 7% = 2.3331, should round to 2.34 but floors to 2.33
        assertEquals(2.34, small.computeTax(7), 0.001);
    }

    // --- getTotalWithTax ---
    @Test
    void testGetTotalWithTax() {
        assertEquals(110.0, calc.getTotalWithTax(10), 0.001);
    }

    // --- computeShipping ---
    @Test
    void testShippingFreeOverHundred() {
        OrderCalculator big = new OrderCalculator(150.0);
        assertEquals(0.0, big.computeShipping(5.0), 0.001);
    }

    @Test
    void testShippingUnderHundred() {
        OrderCalculator small = new OrderCalculator(50.0);
        assertEquals(12.5, small.computeShipping(5.0), 0.001);
    }

    @Test
    void testShippingZeroWeightThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.computeShipping(0));
    }

    @Test
    void testIsDiscountApplied() {
        assertFalse(calc.isDiscountApplied());
        calc.applyDiscount(10);
        assertTrue(calc.isDiscountApplied());
    }
}
