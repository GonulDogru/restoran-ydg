package com.restoran.strategy;

public class CashPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println(amount + " TL nakit olarak tahsil edildi. Kasa güncelleniyor.");
    }

    @Override
    public String getStrategyName() {
        return "Nakit";
    }
}