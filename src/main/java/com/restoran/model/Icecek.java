package com.restoran.model;

public class Icecek {

    // Alanlar final yapılarak nesne değişmezliği (immutability) sağlanır
    private final int id;
    private final String name;
    private final int menuId;

    // Constructor PRIVATE yapılır. Sadece Builder sınıfı erişebilir.
    private Icecek(IcecekBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.menuId = builder.menuId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMenuId() {
        return menuId;
    }

    // --- Static Inner Builder Class ---
    public static class IcecekBuilder {
        private int id;
        private String name;
        private int menuId;

        public IcecekBuilder() {
            // Boş constructor
        }

        public IcecekBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public IcecekBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public IcecekBuilder setMenuId(int menuId) {
            this.menuId = menuId;
            return this;
        }

        public Icecek build() {
            return new Icecek(this);
        }
    }
}