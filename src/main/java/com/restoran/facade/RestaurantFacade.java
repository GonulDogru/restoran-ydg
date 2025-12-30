package com.restoran.facade;

import com.restoran.model.Restaurant; // Güncel model paketi
import com.restoran.repository.RestaurantService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final RestaurantService restaurantService;

    public RestaurantFacade(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public String createRestaurant(Restaurant restaurant) {
        // İsterseniz burada restoran adı veya adres kontrolü yapabilirsiniz.
        return restaurantService.createRestaurant(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    public Restaurant getRestaurantById(int id) {
        return restaurantService.getRestaurantById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Restaurant nesnesi immutable olduğu için (setter yok),
     * parametre olarak gelen ID'yi koruyarak ve yeni verileri kullanarak
     * Builder aracılığıyla yeni bir nesne oluşturuyoruz.
     */
    public String updateRestaurant(int id, Restaurant yeniVeriler) {
        Restaurant guncellenecekRestoran = new Restaurant.RestaurantBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setName(yeniVeriler.getName())
                .setAddress(yeniVeriler.getAddress())
                .build();

        return restaurantService.updateRestaurant(id, guncellenecekRestoran);
    }

    public String deleteRestaurant(int id) {
        return restaurantService.deleteRestaurant(id);
    }
}