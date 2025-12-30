package com.restoran.model;

public class Tedarikci {

    // Alanlar final yapılarak nesne güvenliği sağlanır (Immutable)
    private final int id;
    private final String name;
    private final String phone;
    private final int restaurantId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Tedarikci(TedarikciBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.restaurantId = builder.restaurantId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    // --- Static Inner Builder Class ---
    public static class TedarikciBuilder {
        private int id;
        private String name;
        private String phone;
        private int restaurantId;

        public TedarikciBuilder() {
            // Boş constructor
        }

        public TedarikciBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public TedarikciBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TedarikciBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public TedarikciBuilder setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Tedarikci build() {
            return new Tedarikci(this);
        }
    }
}