package com.restoran.integration;

import com.restoran.model.Teslimat;
import com.restoran.repository.TeslimatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class TeslimatServiceIT {

    @Autowired
    private TeslimatService teslimatService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM teslimat");
        jdbcTemplate.update("DELETE FROM siparis");
        jdbcTemplate.update("DELETE FROM restaurant");
    }

    private int insertRestaurant() {
        jdbcTemplate.update("INSERT INTO restaurant (name, adres) VALUES (?,?)", "R3", "Adres3");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM restaurant ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertSiparis() {
        // siparis.tarih TIMESTAMP
        jdbcTemplate.update("INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?,?,?,?)",
                Timestamp.valueOf("2025-12-30 12:00:00"), 50.00, 1, 1);
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM siparis ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    @Test
    void createTeslimat_thenGetById_shouldReturnSameData() {
        int restaurantId = insertRestaurant();
        int siparisId = insertSiparis();

        Teslimat t = new Teslimat.TeslimatBuilder()
                .setId(0)
                .setAddress("Malatya Merkez")
                .setDurum("Hazırlanıyor")
                .setRestaurantId(restaurantId)
                .setSiparisId(siparisId)
                .build();

        String msg = teslimatService.createTeslimat(t);
        assertEquals("Teslimat başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM teslimat ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Teslimat fromDb = teslimatService.getTeslimatById(id);
        assertNotNull(fromDb);
        assertEquals("Malatya Merkez", fromDb.getAddress());
        assertEquals("Hazırlanıyor", fromDb.getDurum());
        assertEquals(restaurantId, fromDb.getRestaurantId());
        assertEquals(siparisId, fromDb.getSiparisId());
    }

    @Test
    void updateTeslimat_shouldUpdateRow() {
        int restaurantId = insertRestaurant();
        int siparisId = insertSiparis();

        jdbcTemplate.update("INSERT INTO teslimat (address, durum, restaurant_id, siparis_id) VALUES (?,?,?,?)",
                "Adres", "Yolda", restaurantId, siparisId);
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM teslimat ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Teslimat updated = new Teslimat.TeslimatBuilder()
                .setId(999)
                .setAddress("Yeni Adres")
                .setDurum("Teslim Edildi")
                .setRestaurantId(restaurantId)
                .setSiparisId(siparisId)
                .build();

        String msg = teslimatService.updateTeslimat(id, updated);
        assertEquals("Teslimat başarıyla güncellendi.", msg);

        Teslimat fromDb = teslimatService.getTeslimatById(id);
        assertNotNull(fromDb);
        assertEquals("Yeni Adres", fromDb.getAddress());
        assertEquals("Teslim Edildi", fromDb.getDurum());
    }

    @Test
    void deleteTeslimat_shouldDeleteRow() {
        int restaurantId = insertRestaurant();
        int siparisId = insertSiparis();

        jdbcTemplate.update("INSERT INTO teslimat (address, durum, restaurant_id, siparis_id) VALUES (?,?,?,?)",
                "Adres", "Yolda", restaurantId, siparisId);
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM teslimat ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = teslimatService.deleteTeslimat(id);
        assertEquals("Teslimat başarıyla silindi!", msg);

        assertNull(teslimatService.getTeslimatById(id));
    }
}
