package com.restoran.repository;

import com.restoran.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class RestaurantServiceIT {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clean() {
        // Test izolasyonu
        jdbcTemplate.update("DELETE FROM restaurant");
    }

    @Test
    void createListUpdateDelete_flow() {
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setName("IT Restaurant")
                .setAddress("IT Adres")
                .build();

        String createResult = restaurantService.createRestaurant(r);
        assertEquals("Başarılı", createResult);

        List<Restaurant> all = restaurantService.getAllRestaurants();
        assertEquals(1, all.size());
        int id = all.get(0).getId();

        Restaurant updated = new Restaurant.RestaurantBuilder()
                .setId(id)
                .setName("IT Restaurant")
                .setAddress("Yeni Adres")
                .build();

        String updateResult = restaurantService.updateRestaurant(id, updated);
        assertEquals("Güncellendi", updateResult);

        Restaurant loaded = restaurantService.getRestaurantById(id);
        assertNotNull(loaded);
        assertEquals("Yeni Adres", loaded.getAddress());

        String deleteResult = restaurantService.deleteRestaurant(id);
        assertEquals("Silindi", deleteResult);

        assertTrue(restaurantService.getAllRestaurants().isEmpty());
    }
}
