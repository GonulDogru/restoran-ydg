package com.restoran.state;

import com.restoran.model.Masa;

/**
 * RezerveDurum: Masanın bir müşteri için ayrıldığı durumu temsil eder.
 */
public class RezerveDurum implements MasaState {

    @Override
    public void masaAc(int masaId) {
        // Rezerve masa açılabilir ve "Dolu" durumuna geçiş yapabilir.
        System.out.println("Masa " + masaId + " (Rezervasyonlu): Müşteri geldi, masa kullanıma açıldı.");
    }

    @Override
    public void siparisAl(int masaId) {
        // Masa henüz fiilen açılmadığı (oturulmadığı) için sipariş alınamaz.
        System.out.println("Hata: Rezerve masa için önce giriş (Masa Aç) işlemi yapılmalıdır.");
    }

    @Override
    public void odemeAl(int masaId) {
        // Henüz hizmet verilmediği için ödeme alınamaz.
        System.out.println("Hata: Rezerve masadan ödeme alınamaz.");
    }

    @Override
    public void rezervasyonYap(int masaId) {
        // Masa zaten rezerve edilmiş durumda.
        System.out.println("Hata: Masa " + masaId + " zaten rezerve edilmiş durumda.");
    }

    @Override
    public String getDurumAdi() {
        return "REZERVE";
    }
}