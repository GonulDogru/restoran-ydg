package com.restoran.model;

public class Stok {

    // Alanlar final yapılarak stok verisinin bütünlüğü korunur
    private final int id;
    private final int miktar;
    private final int yemekId;
    private final int icecekId;
    private final int tatliId;
    private final int tedarikciId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Stok(StokBuilder builder) {
        this.id = builder.id;
        this.miktar = builder.miktar;
        this.yemekId = builder.yemekId;
        this.icecekId = builder.icecekId;
        this.tatliId = builder.tatliId;
        this.tedarikciId = builder.tedarikciId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public int getMiktar() {
        return miktar;
    }

    public int getYemekId() {
        return yemekId;
    }

    public int getIcecekId() {
        return icecekId;
    }

    public int getTatliId() {
        return tatliId;
    }

    public int getTedarikciId() {
        return tedarikciId;
    }

    // --- Static Inner Builder Class ---
    public static class StokBuilder {
        private int id;
        private int miktar;
        private int yemekId;
        private int icecekId;
        private int tatliId;
        private int tedarikciId;

        public StokBuilder() {
            // Boş constructor
        }

        public StokBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public StokBuilder setMiktar(int miktar) {
            this.miktar = miktar;
            return this;
        }

        public StokBuilder setYemekId(int yemekId) {
            this.yemekId = yemekId;
            return this;
        }

        public StokBuilder setIcecekId(int icecekId) {
            this.icecekId = icecekId;
            return this;
        }

        public StokBuilder setTatliId(int tatliId) {
            this.tatliId = tatliId;
            return this;
        }

        public StokBuilder setTedarikciId(int tedarikciId) {
            this.tedarikciId = tedarikciId;
            return this;
        }

        public Stok build() {
            // Validasyon örneği: Stok miktarı negatif olamaz
            if (this.miktar < 0) {
                // throw new IllegalStateException("Stok miktarı negatif olamaz");
            }
            return new Stok(this);
        }
    }
}