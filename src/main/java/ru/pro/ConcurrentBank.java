package ru.pro;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {
    private final Map<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(long balance) {
        BankAccount account = new BankAccount(balance);
        accounts.put(account.getAccNumber(), account);
        return account;
    }

    public void transfer(BankAccount fromAccount, BankAccount toAccount, long amount) {
        String message;
        BankAccount firstLock = fromAccount.getAccNumber().compareTo(toAccount.getAccNumber()) > 0
                ? fromAccount
                : toAccount;
        BankAccount secondLock = fromAccount.getAccNumber().compareTo(toAccount.getAccNumber()) > 0
                ? toAccount
                : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                message = transaction(fromAccount, toAccount, amount);
            }
        }
        System.out.println(message);
    }

    public long getTotalBalance() {
        return accounts.values().stream()
                .mapToLong(BankAccount::getBalance)
                .sum();
    }

    private String transaction(BankAccount fromAccount, BankAccount toAccount, long amount) {
        String message = "transfer{" +
                "from " + fromAccount +
                ", to " + toAccount +
                ", amount - " + amount +
                '}';

        if (isPossible(fromAccount, toAccount, amount)) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            message = message.concat(" выполнена (")
                    .concat(fromAccount.toString())
                    .concat(", ")
                    .concat(toAccount.toString())
                    .concat(") ")
                    .concat(String.valueOf((fromAccount.getAccNumber().compareTo(toAccount.getAccNumber()) < 0)))
                    .concat(". ");
        } else {
            message = message.concat(" невозможна.");
        }
        return message;
    }

    private boolean isBank(BankAccount account) {
        //Если ли аккаунт в банке?
        return accounts.containsValue(account);
    }

    private boolean isEnough(BankAccount account, long amount) {
        //Достаточно средств
        return account.getBalance() >= amount;
    }

    private boolean isEquals(BankAccount fromAccount, BankAccount toAccount) {
        //Совпадают ли счета
        return fromAccount.equals(toAccount);
    }


    private boolean isPossible(BankAccount fromAccount, BankAccount toAccount, long amount) {
        return isBank(fromAccount) && isBank(toAccount) &&
                !isEquals(fromAccount, toAccount) &&
                amount > 0 && isEnough(fromAccount, amount);
    }
}
