package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount account;
    private BankAccount target;

    @BeforeEach
    void setUp() {
        account = new BankAccount("Alice", 100.0);
        target = new BankAccount("Bob", 50.0);
    }

    // --- constructor ---
    @Test
    void testInitialBalance() {
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    void testOwner() {
        assertEquals("Alice", account.getOwner());
    }

    @Test
    void testNegativeInitialBalanceThrows() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("Eve", -10));
    }

    // --- deposit (BUGGY: accepts negative) ---
    @Test
    void testDepositPositive() {
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance(), 0.001);
    }

    @Test
    void testDepositZero() {
        account.deposit(0);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    void testDepositNegativeThrows() {
        // BUG: does not throw, silently subtracts from balance
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-50.0));
    }

    @Test
    void testDepositMultiple() {
        account.deposit(25.0);
        account.deposit(25.0);
        assertEquals(150.0, account.getBalance(), 0.001);
    }

    // --- withdraw (BUGGY: no overdraft check) ---
    @Test
    void testWithdrawValid() {
        account.withdraw(30.0);
        assertEquals(70.0, account.getBalance(), 0.001);
    }

    @Test
    void testWithdrawExactBalance() {
        account.withdraw(100.0);
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    void testWithdrawOverdraftThrows() {
        // BUG: does not throw, allows negative balance
        assertThrows(IllegalStateException.class, () -> account.withdraw(200.0));
    }

    @Test
    void testWithdrawZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
    }

    @Test
    void testWithdrawNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-10));
    }

    // --- transfer ---
    @Test
    void testTransferValid() {
        account.transfer(target, 30.0);
        assertEquals(70.0, account.getBalance(), 0.001);
        assertEquals(80.0, target.getBalance(), 0.001);
    }

    @Test
    void testTransferOverdraftThrows() {
        // BUG: no overdraft check so this will not throw
        assertThrows(IllegalStateException.class, () -> account.transfer(target, 200.0));
    }

    // --- applyInterest (BUGGY: truncates instead of rounds) ---
    @Test
    void testApplyInterestSimple() {
        BankAccount acc = new BankAccount("Test", 200.0);
        acc.applyInterest(0.05); // 10.0 interest, exact
        assertEquals(210.0, acc.getBalance(), 0.001);
    }

    @Test
    void testApplyInterestFractional() {
        BankAccount acc = new BankAccount("Test", 100.0);
        acc.applyInterest(0.055); // 5.5 interest, should round to 5.5 but int cast gives 5
        // BUG: returns 105.0 instead of 105.5
        assertEquals(105.5, acc.getBalance(), 0.001);
    }

    @Test
    void testApplyInterestRounding() {
        BankAccount acc = new BankAccount("Test", 33.0);
        acc.applyInterest(0.1); // 3.3 interest, int cast = 3, so balance = 36
        // BUG: returns 36.0 instead of 36.3
        assertEquals(36.3, acc.getBalance(), 0.001);
    }

    // --- statement ---
    @Test
    void testGetStatement() {
        String stmt = account.getStatement();
        assertTrue(stmt.contains("Alice"));
        assertTrue(stmt.contains("100.00"));
    }

    @Test
    void testGetStatementAfterDeposit() {
        account.deposit(50.0);
        String stmt = account.getStatement();
        assertTrue(stmt.contains("150.00"));
    }
}
