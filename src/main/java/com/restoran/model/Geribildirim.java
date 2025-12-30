package com.restoran.model;

public class Geribildirim {

    // Alanlar final tanımlanarak nesne değişmezliği (immutability) sağlanır
    private final int id;
    private final String yorum;
    private final int userId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Geribildirim(GeribildirimBuilder builder) {
        this.id = builder.id;
        this.yorum = builder.yorum;
        this.userId = builder.userId;
    }

    // Sadece Getter metodları
    public int getId() {
        return id;
    }

    public String getYorum() {
        return yorum;
    }

    public int getUserId() {
        return userId;
    }

    // --- Static Inner Builder Class ---
    public static class GeribildirimBuilder {
        private int id;
        private String yorum;
        private int userId;

        public GeribildirimBuilder() {
            // Boş constructor
        }

        public GeribildirimBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public GeribildirimBuilder setYorum(String yorum) {
            this.yorum = yorum;
            return this;
        }

        public GeribildirimBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Geribildirim build() {
            return new Geribildirim(this);
        }
    }
}