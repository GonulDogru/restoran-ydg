package com.restoran.integration;

import com.restoran.model.Iletisim;
import com.restoran.repository.IletisimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class IletisimServiceIT {

    @Autowired
    private IletisimService iletisimService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM iletisim");
        jdbcTemplate.update("DELETE FROM kullanici");
        jdbcTemplate.update("DELETE FROM restaurant");
    }

    private int insertUser() {
        jdbcTemplate.update("INSERT INTO kullanici (username, password, telefon, adres) VALUES (?,?,?,?)",
                "u3", "p3", "05000000002", "adres3");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM kullanici ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertRestaurant() {
        jdbcTemplate.update("INSERT INTO restaurant (name, adres) VALUES (?,?)", "R2", "Adres2");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM restaurant ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    @Test
    void createIletisim_thenGetById_shouldReturnSameData() {
        int userId = insertUser();
        int restaurantId = insertRestaurant();

        Iletisim i = new Iletisim.IletisimBuilder()
                .setId(0)
                .setUserId(userId)
                .setRestaurantId(restaurantId)
                .setMesaj("Merhaba")
                .build();

        String msg = iletisimService.createIletisim(i);
        assertEquals("İletişim mesajı başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM iletisim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Iletisim fromDb = iletisimService.getIletisimById(id);
        assertNotNull(fromDb);
        assertEquals(userId, fromDb.getUserId());
        assertEquals(restaurantId, fromDb.getRestaurantId());
        assertEquals("Merhaba", fromDb.getMesaj());
    }

    @Test
    void updateIletisim_shouldUpdateRow() {
        int userId = insertUser();
        int restaurantId = insertRestaurant();

        jdbcTemplate.update("INSERT INTO iletisim (user_id, restaurant_id, mesaj) VALUES (?,?,?)",
                userId, restaurantId, "Eski");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM iletisim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Iletisim updated = new Iletisim.IletisimBuilder()
                .setId(999)
                .setUserId(userId)
                .setRestaurantId(restaurantId)
                .setMesaj("Yeni")
                .build();

        String msg = iletisimService.updateIletisim(id, updated);
        assertEquals("İletişim mesajı başarıyla güncellendi.", msg);

        Iletisim fromDb = iletisimService.getIletisimById(id);
        assertNotNull(fromDb);
        assertEquals("Yeni", fromDb.getMesaj());
    }

    @Test
    void deleteIletisim_shouldDeleteRow() {
        int userId = insertUser();
        int restaurantId = insertRestaurant();

        jdbcTemplate.update("INSERT INTO iletisim (user_id, restaurant_id, mesaj) VALUES (?,?,?)",
                userId, restaurantId, "Sil");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM iletisim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = iletisimService.deleteIletisim(id);
        assertEquals("İletişim mesajı başarıyla silindi!", msg);

        assertNull(iletisimService.getIletisimById(id));
    }
}
