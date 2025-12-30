package com.restoran.model;

import java.math.BigDecimal;

public class Indirim {

    // Finansal verilerde tutarlılık için alanlar final (değiştirilemez) yapıldı
    private final int id;
    private final BigDecimal amount;
    private final int kullaniciId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Indirim(IndirimBuilder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.kullaniciId = builder.kullaniciId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getKullaniciId() {
        return kullaniciId;
    }

    // --- Static Inner Builder Class ---
    public static class IndirimBuilder {
        private int id;
        private BigDecimal amount;
        private int kullaniciId;

        public IndirimBuilder() {
            // Boş constructor
        }

        public IndirimBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public IndirimBuilder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public IndirimBuilder setKullaniciId(int kullaniciId) {
            this.kullaniciId = kullaniciId;
            return this;
        }

        public Indirim build() {
            return new Indirim(this);
        }
    }
}