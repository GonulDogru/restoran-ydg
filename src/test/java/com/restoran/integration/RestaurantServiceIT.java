package com.restoran.integration;

import com.restoran.model.Restaurant;
import com.restoran.repository.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class RestaurantServiceIT {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM restaurant");
    }

    @Test
    void createRestaurant_thenGetById_shouldReturnSameData() {
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setId(0)
                .setName("IT Restaurant")
                .setAddress("Malatya")
                .build();

        String createMsg = restaurantService.createRestaurant(r);
        assertEquals("Başarılı", createMsg);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM restaurant WHERE name = ? AND adres = ? ORDER BY id DESC LIMIT 1",
                Integer.class,
                "IT Restaurant",
                "Malatya"
        );
        assertNotNull(id);

        Restaurant fromDb = restaurantService.getRestaurantById(id);
        assertNotNull(fromDb);
        assertEquals("IT Restaurant", fromDb.getName());
        assertEquals("Malatya", fromDb.getAddress());
    }

    @Test
    void updateRestaurant_thenGetById_shouldReflectChanges() {
        jdbcTemplate.update("INSERT INTO restaurant (name, adres) VALUES (?, ?)", "Old", "OldAddr");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM restaurant WHERE name = 'Old' ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Restaurant updated = new Restaurant.RestaurantBuilder()
                .setId(999)
                .setName("New")
                .setAddress("NewAddr")
                .build();

        String msg = restaurantService.updateRestaurant(id, updated);
        assertEquals("Güncellendi", msg);

        Restaurant fromDb = restaurantService.getRestaurantById(id);
        assertNotNull(fromDb);
        assertEquals("New", fromDb.getName());
        assertEquals("NewAddr", fromDb.getAddress());
    }

    @Test
    void deleteRestaurant_thenGetById_shouldReturnNull() {
        jdbcTemplate.update("INSERT INTO restaurant (name, adres) VALUES (?, ?)", "ToDelete", "Addr");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM restaurant WHERE name = 'ToDelete' ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = restaurantService.deleteRestaurant(id);
        assertEquals("Silindi", msg);

        Restaurant fromDb = restaurantService.getRestaurantById(id);
        assertNull(fromDb);
    }
}
