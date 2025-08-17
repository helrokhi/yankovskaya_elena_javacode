package ru.pro;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BankAccount {
    private long balance;
    private final UUID accNumber;

    public BankAccount(long balance) {
        this.accNumber = UUID.randomUUID();
        this.balance = balance;
    }

    public void deposit(long amountToPut) {
        //зачисляем деньги на счет
        balance = balance + amountToPut;
    }

    public void withdraw(long amountToTake) {
        //списываем деньги со счета
        balance = balance - amountToTake;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accNumber=" + accNumber +
                ", balance=" + balance +
                '}';
    }
}
