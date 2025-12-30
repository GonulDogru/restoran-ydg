package com.restoran.model;

import com.restoran.state.MasaState;
import com.restoran.state.BosDurum;

public class Masa {
    private int id;
    private int numara;
    private int kapasite;
    private int restaurantId;
    private MasaState durum; // State Pattern için

    // Builder kullanımı için constructor
    public Masa() {
        this.durum = new BosDurum();
    }

    // Getter ve Setterlar
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNumara() { return numara; }
    public void setNumara(int numara) { this.numara = numara; }
    public int getKapasite() { return kapasite; }
    public void setKapasite(int kapasite) { this.kapasite = kapasite; }
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public MasaState getDurum() { return durum; }
    public void setDurum(MasaState durum) { this.durum = durum; }
}