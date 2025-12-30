package com.restoran.decorator;

public class AnaYemek implements Yemek {
    private String isim;
    private double fiyat;

    public AnaYemek(String isim, double fiyat) {
        this.isim = isim;
        this.fiyat = fiyat;
    }

    @Override
    public String getAciklama() {
        return isim;
    }

    @Override
    public double getFiyat() {
        return fiyat;
    }
}