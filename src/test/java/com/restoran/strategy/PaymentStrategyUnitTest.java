package com.restoran.strategy;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStrategyUnitTest {

    @Test
    void cashPayment_getStrategyName_shouldReturnNakit() {
        PaymentStrategy strategy = new CashPayment();
        assertEquals("Nakit", strategy.getStrategyName());
    }

    @Test
    void creditCardPayment_getStrategyName_shouldReturnKrediKarti() {
        PaymentStrategy strategy = new CreditCardPayment();
        assertEquals("Kredi Kartı", strategy.getStrategyName());
    }

    @Test
    void cashPayment_processPayment_shouldPrintExpectedMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try {
            PaymentStrategy strategy = new CashPayment();
            strategy.processPayment(150.0);
        } finally {
            System.setOut(original);
        }

        String printed = out.toString(StandardCharsets.UTF_8).trim();
        assertTrue(printed.contains("150.0 TL nakit olarak tahsil edildi"), printed);
    }

    @Test
    void creditCardPayment_processPayment_shouldPrintExpectedMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try {
            PaymentStrategy strategy = new CreditCardPayment();
            strategy.processPayment(250.0);
        } finally {
            System.setOut(original);
        }

        String printed = out.toString(StandardCharsets.UTF_8).trim();
        assertTrue(printed.contains("250.0 TL kredi kartından çekildi"), printed);
    }
}
