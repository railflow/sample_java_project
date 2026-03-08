package com.example;

public class MathUtils {

    public static int add(int a, int b) {
        return a + b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    // BUG: integer division instead of double division
    public static double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }

    // BUG: power(base, 0) returns 0 instead of 1
    public static double power(double base, int exponent) {
        if (exponent == 0) {
            return 0; // should be 1
        }
        double result = 1;
        for (int i = 0; i < Math.abs(exponent); i++) {
            result *= base;
        }
        return exponent < 0 ? 1.0 / result : result;
    }

    // BUG: throws on negative input instead of computing correctly
    public static int modulo(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Negative values not supported");
        }
        return a % b;
    }

    public static double absolute(double value) {
        return Math.abs(value);
    }

    public static double squareRoot(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cannot compute square root of negative number");
        }
        return Math.sqrt(value);
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int min(int a, int b) {
        return a < b ? a : b;
    }
}
