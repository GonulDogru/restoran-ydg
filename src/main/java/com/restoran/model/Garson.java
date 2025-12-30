package com.restoran.model;

public class Garson {

    // Alanlar final yapılarak nesnenin tutarlılığı korunur (Immutable)
    private final int id;
    private final String name;
    private final int calisanId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Garson(GarsonBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.calisanId = builder.calisanId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCalisanId() {
        return calisanId;
    }

    // --- Static Inner Builder Class ---
    public static class GarsonBuilder {
        private int id;
        private String name;
        private int calisanId;

        public GarsonBuilder() {
            // Boş constructor
        }

        public GarsonBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public GarsonBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GarsonBuilder setCalisanId(int calisanId) {
            this.calisanId = calisanId;
            return this;
        }

        public Garson build() {
            return new Garson(this);
        }
    }
}