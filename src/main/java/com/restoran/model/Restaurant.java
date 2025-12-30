package com.restoran.model;

public class Restaurant {

    // Alanlar final yapılarak nesne güvenliği sağlanır (Immutable)
    private final int id;
    private final String name;
    private final String address;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Restaurant(RestaurantBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.address = builder.address;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // --- Static Inner Builder Class ---
    public static class RestaurantBuilder {
        private int id;
        private String name;
        private String address;

        public RestaurantBuilder() {
            // Boş constructor
        }

        public RestaurantBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public RestaurantBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RestaurantBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Restaurant build() {

            if (name == null) throw new IllegalStateException("Restoran adı boş olamaz");
            return new Restaurant(this);
        }
    }
}