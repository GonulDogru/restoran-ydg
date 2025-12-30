package com.restoran.model;

public class Admin {

    // Alanlar private (ve tercihen final) tanımlanır
    private final int id;
    private final String username;
    private final String password;
    private final int superadminId;

    // Constructor PRIVATE yapılır. Sadece Builder erişebilir.
    private Admin(AdminBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.superadminId = builder.superadminId;
    }

    // Sadece Getter metodları (Setter yok, nesne oluşturulduktan sonra değişmez)
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getSuperadminId() { return superadminId; }

    // --- Static Inner Builder Class ---
    public static class AdminBuilder {
        private int id;
        private String username;
        private String password;
        private int superadminId;

        // Zorunlu alanlar varsa constructor'a eklenebilir, yoksa boş bırakılır
        public AdminBuilder() {
        }

        public AdminBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public AdminBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public AdminBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public AdminBuilder setSuperadminId(int superadminId) {
            this.superadminId = superadminId;
            return this;
        }


        public Admin build() {
            if (username == null) throw new IllegalStateException("Kullanıcı adı boş olamaz");
            return new Admin(this);
        }
    }
}