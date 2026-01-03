package com.restoran.integration;

import com.restoran.model.Tedarikci;
import com.restoran.repository.TedarikciService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class TedarikciServiceIT {

    @Autowired
    private TedarikciService tedarikciService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM tedarikci");
        jdbcTemplate.update("DELETE FROM restaurant");
    }

    private int insertRestaurant() {
        jdbcTemplate.update("INSERT INTO restaurant (name, adres) VALUES (?,?)", "R1", "Adres1");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM restaurant ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    @Test
    void createTedarikci_thenGetById_shouldReturnSameData() {
        int restaurantId = insertRestaurant();

        Tedarikci t = new Tedarikci.TedarikciBuilder()
                .setId(0)
                .setName("ABC Ltd")
                .setPhone("05550000000")
                .setRestaurantId(restaurantId)
                .build();

        String msg = tedarikciService.createTedarikci(t);
        assertEquals("Tedarikçi başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM tedarikci ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Tedarikci fromDb = tedarikciService.getTedarikciById(id);
        assertNotNull(fromDb);
        assertEquals("ABC Ltd", fromDb.getName());
        assertEquals("05550000000", fromDb.getPhone());
        assertEquals(restaurantId, fromDb.getRestaurantId());
    }

    @Test
    void updateTedarikci_shouldUpdateRow() {
        int restaurantId = insertRestaurant();
        jdbcTemplate.update("INSERT INTO tedarikci (name, phone, restaurant_id) VALUES (?,?,?)",
                "X", "000", restaurantId);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM tedarikci ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Tedarikci updated = new Tedarikci.TedarikciBuilder()
                .setId(999)
                .setName("NEW")
                .setPhone("111")
                .setRestaurantId(restaurantId)
                .build();

        String msg = tedarikciService.updateTedarikci(id, updated);
        assertEquals("Tedarikçi başarıyla güncellendi.", msg);

        Tedarikci fromDb = tedarikciService.getTedarikciById(id);
        assertNotNull(fromDb);
        assertEquals("NEW", fromDb.getName());
        assertEquals("111", fromDb.getPhone());
    }

    @Test
    void deleteTedarikci_shouldDeleteRow() {
        int restaurantId = insertRestaurant();
        jdbcTemplate.update("INSERT INTO tedarikci (name, phone, restaurant_id) VALUES (?,?,?)",
                "X", "000", restaurantId);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM tedarikci ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = tedarikciService.deleteTedarikci(id);
        assertEquals("Tedarikçi başarıyla silindi!", msg);

        assertNull(tedarikciService.getTedarikciById(id));
    }
}
