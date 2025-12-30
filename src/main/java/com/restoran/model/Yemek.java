package com.restoran.model;

public class Yemek {
    private final int id;
    private final String name;
    private final int menuId;

    private Yemek(YemekBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.menuId = builder.menuId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getMenuId() { return menuId; }

    public static class YemekBuilder {
        private int id;
        private String name;
        private int menuId;

        public YemekBuilder setId(int id) { this.id = id; return this; }
        public YemekBuilder setName(String name) { this.name = name; return this; }
        public YemekBuilder setMenuId(int menuId) { this.menuId = menuId; return this; }
        public Yemek build() { return new Yemek(this); }
    }
}