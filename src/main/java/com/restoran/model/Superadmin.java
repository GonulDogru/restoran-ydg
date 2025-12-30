package com.restoran.model;

public class Superadmin {

    // Alanlar final yapılarak nesne güvenliği sağlanır
    private final int id;
    private final String username;
    private final String password;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Superadmin(SuperadminBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // --- Static Inner Builder Class ---
    public static class SuperadminBuilder {
        private int id;
        private String username;
        private String password;

        public SuperadminBuilder() {
            // Boş constructor
        }

        public SuperadminBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public SuperadminBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public SuperadminBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Superadmin build() {
            return new Superadmin(this);
        }
    }
}