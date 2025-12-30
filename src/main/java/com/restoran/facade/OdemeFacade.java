package com.restoran.facade;

import com.restoran.model.Odeme;
import com.restoran.repository.RestaurantService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OdemeFacade {

    private final RestaurantService restaurantService;

    // 1. Constructor Injection
    public OdemeFacade(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Tüm ödemeleri veritabanından getirir.
     */
    public List<Odeme> getAllOdemeler() {
        return restaurantService.getAllOdemeler();
    }

    /**
     * ID'ye göre tek bir ödeme detayı getirir.
     * Düzenleme (Edit) sayfasında formu doldurmak için kullanılır.
     */
    public Odeme getOdemeById(int id) {
        return restaurantService.getOdemeById(id);
    }

    /**
     * Yeni bir ödeme kaydı oluşturur.
     */
    public void createOdeme(Odeme odeme) {
        restaurantService.saveOdeme(odeme);
    }

    /**
     * Mevcut bir ödeme kaydını günceller.
     * WebOdemeController içindeki updateOdeme hatasını bu metot çözer.
     */
    public void updateOdeme(int id, Odeme odeme) {
        restaurantService.updateOdeme(id, odeme);
    }

    /**
     * Bir ödemeyi veritabanından siler.
     */
    public void deleteOdeme(int id) {
        restaurantService.deleteOdeme(id);
    }
}