package com.restoran.model;

public class Iletisim {

    // Alanlar final yapılarak nesne güvenliği sağlanır
    private final int id;
    private final int userId;
    private final int restaurantId;
    private final String mesaj;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Iletisim(IletisimBuilder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.restaurantId = builder.restaurantId;
        this.mesaj = builder.mesaj;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getMesaj() {
        return mesaj;
    }

    // --- Static Inner Builder Class ---
    public static class IletisimBuilder {
        private int id;
        private int userId;
        private int restaurantId;
        private String mesaj;

        public IletisimBuilder() {
            // Boş constructor
        }

        public IletisimBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public IletisimBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public IletisimBuilder setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public IletisimBuilder setMesaj(String mesaj) {
            this.mesaj = mesaj;
            return this;
        }

        public Iletisim build() {
            return new Iletisim(this);
        }
    }
}