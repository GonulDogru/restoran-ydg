package com.restoran.model;

public class Calisan {

    // Alanlar 'final' yapılarak nesnenin oluşturulduktan sonra değişmesi engellenir (Immutability)
    private final int id;
    private final String name;
    private final int restaurantId;

    // Constructor PRIVATE yapılır. Sadece Builder sınıfı erişebilir.
    private Calisan(CalisanBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.restaurantId = builder.restaurantId;
    }

    // Sadece Getter metodları (Setter yok)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    // --- Static Inner Builder Class ---
    public static class CalisanBuilder {
        private int id;
        private String name;
        private int restaurantId;

        // Yapıcı metodu başlatmak için
        public CalisanBuilder() {
        }

        public CalisanBuilder setId(int id) {
            this.id = id;
            return this; // Zincirleme kullanım için
        }

        public CalisanBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CalisanBuilder setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Calisan build() {
            // Gerekirse burada validasyon yapılabilir (örn: isim boş mu?)
            return new Calisan(this);
        }
    }
}