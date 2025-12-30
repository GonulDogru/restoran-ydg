package com.restoran.model;

public class Tatli {

    // Alanlar final yapılarak nesne güvenliği sağlanır
    private final int id;
    private final String name;
    private final int menuId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Tatli(TatliBuilder builder) {
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
    public static class TatliBuilder {
        private int id;
        private String name;
        private int menuId;

        public TatliBuilder() {
            // Boş constructor
        }

        public TatliBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public TatliBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TatliBuilder setMenuId(int menuId) {
            this.menuId = menuId;
            return this;
        }

        public Tatli build() {
            return new Tatli(this);
        }
    }
}