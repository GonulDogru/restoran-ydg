package com.restoran.model;

public class Odeme {

    private final int id;
    private final int masaId;       // Servis katmanında masa_id olarak kullanılıyor
    private final double tutar;     // Servis katmanında getTutar() ve double olarak bekleniyor
    private final String odemeYontemi; // Servis katmanında getOdemeYontemi() olarak bekleniyor

    // Private Constructor
    private Odeme(OdemeBuilder builder) {
        this.id = builder.id;
        this.masaId = builder.masaId;
        this.tutar = builder.tutar;
        this.odemeYontemi = builder.odemeYontemi;
    }

    // Getter Metotları (Service sınıfı ile tam uyumlu)
    public int getId() {
        return id;
    }

    public int getMasaId() {
        return masaId;
    }

    public double getTutar() {
        return tutar;
    }

    public String getOdemeYontemi() {
        return odemeYontemi;
    }

    // --- Static Inner Builder Class ---
    public static class OdemeBuilder {
        private int id;
        private int masaId;
        private double tutar;
        private String odemeYontemi;

        public OdemeBuilder() {
        }

        public OdemeBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public OdemeBuilder setMasaId(int masaId) {
            this.masaId = masaId;
            return this;
        }

        public OdemeBuilder setTutar(double tutar) {
            this.tutar = tutar;
            return this;
        }

        public OdemeBuilder setOdemeYontemi(String odemeYontemi) {
            this.odemeYontemi = odemeYontemi;
            return this;
        }

        public Odeme build() {
            return new Odeme(this);
        }
    }
}