package com.restoran.decorator;

public class EkstraPeynir extends YemekDecorator {
    public EkstraPeynir(Yemek yemek) {
        super(yemek);
    }

    @Override
    public String getAciklama() {
        return super.getAciklama() + ", Ekstra Peynir";
    }

    @Override
    public double getFiyat() {
        return super.getFiyat() + 15.0; // Peynir farkı
    }
}