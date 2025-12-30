package com.restoran.strategy;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println(amount + " TL kredi kartından çekildi. Banka onayı alındı.");
    }

    @Override
    public String getStrategyName() {
        return "Kredi Kartı";
    }
}