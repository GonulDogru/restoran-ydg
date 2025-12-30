package com.restoran.decorator;

public abstract class YemekDecorator implements Yemek {
    protected Yemek dekoreEdilenYemek;

    public YemekDecorator(Yemek yemek) {
        this.dekoreEdilenYemek = yemek;
    }

    @Override
    public String getAciklama() {
        return dekoreEdilenYemek.getAciklama();
    }

    @Override
    public double getFiyat() {
        return dekoreEdilenYemek.getFiyat();
    }
}