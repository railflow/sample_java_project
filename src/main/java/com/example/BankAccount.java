package com.example;

public class BankAccount {

    private String owner;
    private double balance;

    public BankAccount(String owner, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.owner = owner;
        this.balance = initialBalance;
    }

    // BUG: accepts negative deposit amounts (should throw)
    public void deposit(double amount) {
        balance += amount;
    }

    // BUG: allows balance to go negative (no overdraft check)
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        balance -= amount;
    }

    public void transfer(BankAccount target, double amount) {
        this.withdraw(amount);
        target.deposit(amount);
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }

    // BUG: truncates instead of rounds (uses int cast)
    public void applyInterest(double rate) {
        double interest = balance * rate;
        balance += (int) interest; // should be Math.round or just add the double
    }

    public String getStatement() {
        return String.format("Account[%s]: $%.2f", owner, balance);
    }
}
