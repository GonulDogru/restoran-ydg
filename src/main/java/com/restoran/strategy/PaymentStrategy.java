package com.restoran.strategy;

public interface PaymentStrategy {
    void processPayment(double amount);
    String getStrategyName();
}