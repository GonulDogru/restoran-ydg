package com.restoran.model;

public class Vardiya {

    // Alanlar final yapılarak nesne değişmezliği (immutability) sağlanır
    private final int id;
    private final String saatler;
    private final int calisanId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Vardiya(VardiyaBuilder builder) {
        this.id = builder.id;
        this.saatler = builder.saatler;
        this.calisanId = builder.calisanId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getSaatler() {
        return saatler;
    }

    public int getCalisanId() {
        return calisanId;
    }

    // --- Static Inner Builder Class ---
    public static class VardiyaBuilder {
        private int id;
        private String saatler;
        private int calisanId;

        public VardiyaBuilder() {
            // Boş constructor
        }

        public VardiyaBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public VardiyaBuilder setSaatler(String saatler) {
            this.saatler = saatler;
            return this;
        }

        public VardiyaBuilder setCalisanId(int calisanId) {
            this.calisanId = calisanId;
            return this;
        }

        public Vardiya build() {
            return new Vardiya(this);
        }
    }
}