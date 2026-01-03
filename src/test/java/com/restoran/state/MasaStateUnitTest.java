package com.restoran.state;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MasaStateUnitTest {

    private String captureStdout(Runnable action) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return out.toString(StandardCharsets.UTF_8).trim();
    }

    @Test
    void bosDurum_getDurumAdi_shouldReturnBOS() {
        MasaState state = new BosDurum();
        assertEquals("BOŞ", state.getDurumAdi());
    }

    @Test
    void bosDurum_masaAc_shouldPrintWelcome() {
        MasaState state = new BosDurum();
        String printed = captureStdout(() -> state.masaAc(3));
        assertTrue(printed.contains("Masa 3 açıldı. Hoş geldiniz!"), printed);
    }

    @Test
    void bosDurum_siparisAl_shouldPrintError() {
        MasaState state = new BosDurum();
        String printed = captureStdout(() -> state.siparisAl(3));
        assertTrue(printed.contains("Hata: Önce masanın açılması gerekiyor."), printed);
    }

    @Test
    void doluDurum_getDurumAdi_shouldReturnDOLU() {
        MasaState state = new DoluDurum();
        assertEquals("DOLU", state.getDurumAdi());
    }

    @Test
    void doluDurum_masaAc_shouldPrintAlreadyFullError() {
        MasaState state = new DoluDurum();
        String printed = captureStdout(() -> state.masaAc(5));
        assertTrue(printed.contains("Hata: Masa zaten dolu."), printed);
    }

    @Test
    void doluDurum_siparisAl_shouldPrintTakingOrder() {
        MasaState state = new DoluDurum();
        String printed = captureStdout(() -> state.siparisAl(5));
        assertTrue(printed.contains("Masa 5 için sipariş alınıyor."), printed);
    }

    @Test
    void doluDurum_odemeAl_shouldPrintPaymentTaken() {
        MasaState state = new DoluDurum();
        String printed = captureStdout(() -> state.odemeAl(5));
        assertTrue(printed.contains("Masa 5 için ödeme alındı. Masa şimdi boş."), printed);
    }

    @Test
    void rezerveDurum_getDurumAdi_shouldReturnREZERVE() {
        MasaState state = new RezerveDurum();
        assertEquals("REZERVE", state.getDurumAdi());
    }

    @Test
    void rezerveDurum_masaAc_shouldPrintReservedOpen() {
        MasaState state = new RezerveDurum();
        String printed = captureStdout(() -> state.masaAc(7));
        assertTrue(printed.contains("Masa 7 (Rezervasyonlu): Müşteri geldi, masa kullanıma açıldı."), printed);
    }

    @Test
    void rezerveDurum_siparisAl_shouldPrintMustOpenFirst() {
        MasaState state = new RezerveDurum();
        String printed = captureStdout(() -> state.siparisAl(7));
        assertTrue(printed.contains("Hata: Rezerve masa için önce giriş (Masa Aç) işlemi yapılmalıdır."), printed);
    }

    @Test
    void rezerveDurum_odemeAl_shouldPrintCannotTakePayment() {
        MasaState state = new RezerveDurum();
        String printed = captureStdout(() -> state.odemeAl(7));
        assertTrue(printed.contains("Hata: Rezerve masadan ödeme alınamaz."), printed);
    }

    @Test
    void rezerveDurum_rezervasyonYap_shouldPrintAlreadyReserved() {
        MasaState state = new RezerveDurum();
        String printed = captureStdout(() -> state.rezervasyonYap(7));
        assertTrue(printed.contains("Hata: Masa 7 zaten rezerve edilmiş durumda."), printed);
    }
}
