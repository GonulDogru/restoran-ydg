package com.restoran.state;

public class BosDurum implements MasaState {
    @Override
    public void masaAc(int masaId) { System.out.println("Masa " + masaId + " açıldı. Hoş geldiniz!"); }
    @Override
    public void siparisAl(int masaId) { System.out.println("Hata: Önce masanın açılması gerekiyor."); }
    @Override
    public void odemeAl(int masaId) { System.out.println("Hata: Boş masadan ödeme alınamaz."); }
    @Override
    public void rezervasyonYap(int masaId) { System.out.println("Masa " + masaId + " rezerve edildi."); }
    @Override
    public String getDurumAdi() { return "BOŞ"; }
}