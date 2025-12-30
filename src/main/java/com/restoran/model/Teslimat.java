package com.restoran.model;

public class Teslimat {

    // Alanlar final yapılarak nesne güvenliği sağlanır (Immutable)
    private final int id;
    private final String address;
    private final String durum; // İpucu: Bunu Enum yapmak daha güvenlidir (bkz: Not)
    private final int restaurantId;
    private final int siparisId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Teslimat(TeslimatBuilder builder) {
        this.id = builder.id;
        this.address = builder.address;
        this.durum = builder.durum;
        this.restaurantId = builder.restaurantId;
        this.siparisId = builder.siparisId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getDurum() {
        return durum;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getSiparisId() {
        return siparisId;
    }

    // --- Static Inner Builder Class ---
    public static class TeslimatBuilder {
        private int id;
        private String address;
        private String durum;
        private int restaurantId;
        private int siparisId;

        public TeslimatBuilder() {
            // Boş constructor
        }

        public TeslimatBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public TeslimatBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public TeslimatBuilder setDurum(String durum) {
            this.durum = durum;
            return this;
        }

        public TeslimatBuilder setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public TeslimatBuilder setSiparisId(int siparisId) {
            this.siparisId = siparisId;
            return this;
        }

        public Teslimat build() {
            return new Teslimat(this);
        }
    }
}