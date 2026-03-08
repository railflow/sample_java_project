package com.example;

public class OrderCalculator {

    private double total;
    private boolean discountApplied;

    public OrderCalculator(double initialTotal) {
        if (initialTotal < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        this.total = initialTotal;
        this.discountApplied = false;
    }

    public double computeTotal(double[] prices) {
        double sum = 0;
        for (double price : prices) {
            sum += price;
        }
        this.total = sum;
        this.discountApplied = false;
        return this.total;
    }

    // BUG: applies discount to already-discounted total on second call (doesn't reset)
    public double applyDiscount(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        total = total - (total * discountPercent / 100);
        discountApplied = true;
        return total;
    }

    // BUG: rounds down instead of up for fractional cents (uses Math.floor)
    public double computeTax(double taxRate) {
        double tax = total * taxRate / 100;
        return Math.floor(tax * 100) / 100;
    }

    public double getTotalWithTax(double taxRate) {
        return total + computeTax(taxRate);
    }

    public double getTotal() {
        return total;
    }

    public boolean isDiscountApplied() {
        return discountApplied;
    }

    public double computeShipping(double weightKg) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        if (total > 100) {
            return 0; // free shipping over $100
        }
        return weightKg * 2.5;
    }
}
