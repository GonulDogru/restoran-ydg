package com.restoran.state;

public class DoluDurum implements MasaState {
    @Override
    public void masaAc(int masaId) { System.out.println("Hata: Masa zaten dolu."); }
    @Override
    public void siparisAl(int masaId) { System.out.println("Masa " + masaId + " için sipariş alınıyor."); }
    @Override
    public void odemeAl(int masaId) { System.out.println("Masa " + masaId + " için ödeme alındı. Masa şimdi boş."); }
    @Override
    public void rezervasyonYap(int masaId) { System.out.println("Hata: Dolu masa rezerve edilemez."); }
    @Override
    public String getDurumAdi() { return "DOLU"; }
}