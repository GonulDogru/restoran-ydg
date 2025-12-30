package com.restoran.decorator;

public class EkstraSos extends YemekDecorator {
    public EkstraSos(Yemek yemek) {
        super(yemek);
    }

    @Override
    public String getAciklama() {
        return super.getAciklama() + ", Özel Sos";
    }

    @Override
    public double getFiyat() {
        return super.getFiyat() + 10.0; // Sos farkı
    }
}