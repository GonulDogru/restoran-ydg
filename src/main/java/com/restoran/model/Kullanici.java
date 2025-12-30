package com.restoran.model;

public class Kullanici {
    private int id;
    private String username;
    private String password;
    private String adres;   // address -> adres oldu
    private String telefon; // phone -> telefon oldu

    // Builder yapısı kullanıyorsanız builder içindeki isimleri de güncelleyin
    public static class KullaniciBuilder {
        private int id;
        private String username;
        private String password;
        private String adres;
        private String telefon;

        public KullaniciBuilder setId(int id) { this.id = id; return this; }
        public KullaniciBuilder setUsername(String username) { this.username = username; return this; }
        public KullaniciBuilder setPassword(String password) { this.password = password; return this; }
        public KullaniciBuilder setAdres(String adres) { this.adres = adres; return this; }
        public KullaniciBuilder setTelefon(String telefon) { this.telefon = telefon; return this; }

        public Kullanici build() {
            Kullanici k = new Kullanici();
            k.id = this.id;
            k.username = this.username;
            k.password = this.password;
            k.adres = this.adres;
            k.telefon = this.telefon;
            return k;
        }
    }

    // Getter'lar
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getAdres() { return adres; }
    public String getTelefon() { return telefon; }
}