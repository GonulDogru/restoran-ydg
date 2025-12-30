package com.restoran.model;

public class Siparis {

    // Alanlar final yapılarak siparişin sonradan bozulması engellenir
    private final int id;
    private final String tarih;
    private final float amount; // Öneri: Finansal veriler için BigDecimal kullanılması daha güvenlidir.
    private final int userId;
    private final int masaId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Siparis(SiparisBuilder builder) {
        this.id = builder.id;
        this.tarih = builder.tarih;
        this.amount = builder.amount;
        this.userId = builder.userId;
        this.masaId = builder.masaId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getTarih() {
        return tarih;
    }

    public float getAmount() {
        return amount;
    }

    public int getUserId() {
        return userId;
    }

    public int getMasaId() {
        return masaId;
    }

    // --- Static Inner Builder Class ---
    public static class SiparisBuilder {
        private int id;
        private String tarih;
        private float amount;
        private int userId;
        private int masaId;

        public SiparisBuilder() {
            // Boş constructor
        }

        public SiparisBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public SiparisBuilder setTarih(String tarih) {
            this.tarih = tarih;
            return this;
        }

        public SiparisBuilder setAmount(float amount) {
            this.amount = amount;
            return this;
        }

        public SiparisBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public SiparisBuilder setMasaId(int masaId) {
            this.masaId = masaId;
            return this;
        }

        public Siparis build() {
            return new Siparis(this);
        }
    }
}