package com.restoran.model;

public class Menu {
    private final int id;
    private final String name;
    private final int restaurantId;

    private Menu(MenuBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.restaurantId = builder.restaurantId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getRestaurantId() { return restaurantId; }

    public static class MenuBuilder {
        private int id;
        private String name;
        private int restaurantId;

        public MenuBuilder setId(int id) { this.id = id; return this; }
        public MenuBuilder setName(String name) { this.name = name; return this; }
        public MenuBuilder setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; return this; }
        public Menu build() { return new Menu(this); }
    }
}