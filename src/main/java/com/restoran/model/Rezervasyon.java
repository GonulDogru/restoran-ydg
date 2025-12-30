package com.restoran.model;

public class Rezervasyon {

    // Alanlar final yapılarak nesne güvenliği sağlanır (Immutable)
    private final int id;
    private final String tarih;
    private final String saat;
    private final int userId;
    private final int masaId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Rezervasyon(RezervasyonBuilder builder) {
        this.id = builder.id;
        this.tarih = builder.tarih;
        this.saat = builder.saat;
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

    public String getSaat() {
        return saat;
    }

    public int getUserId() {
        return userId;
    }

    public int getMasaId() {
        return masaId;
    }

    // --- Static Inner Builder Class ---
    public static class RezervasyonBuilder {
        private int id;
        private String tarih;
        private String saat;
        private int userId;
        private int masaId;

        public RezervasyonBuilder() {
            // Boş constructor
        }

        public RezervasyonBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public RezervasyonBuilder setTarih(String tarih) {
            this.tarih = tarih;
            return this;
        }

        public RezervasyonBuilder setSaat(String saat) {
            this.saat = saat;
            return this;
        }

        public RezervasyonBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public RezervasyonBuilder setMasaId(int masaId) {
            this.masaId = masaId;
            return this;
        }

        public Rezervasyon build() {
            return new Rezervasyon(this);
        }
    }
}