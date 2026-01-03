package com.restoran.integration;

import com.restoran.model.Calisan;
import com.restoran.repository.CalisanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class CalisanServiceIT {

    @Autowired
    private CalisanService calisanService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        // İlişki ihtimaline karşı önce vardiya temizlemek güvenli (FK olmasa da iyi pratik)
        jdbcTemplate.update("DELETE FROM vardiya");
        jdbcTemplate.update("DELETE FROM calisan");
    }

    @Test
    void createCalisan_thenGetById_shouldReturnSameData() {
        Calisan c = new Calisan.CalisanBuilder()
                .setId(0)
                .setName("Ahmet")
                .setRestaurantId(1)
                .build();

        String msg = calisanService.createCalisan(c);
        assertEquals("Çalışan başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM calisan WHERE name = ? AND restaurant_id = ? ORDER BY id DESC LIMIT 1",
                Integer.class,
                "Ahmet", 1
        );
        assertNotNull(id);

        Calisan fromDb = calisanService.getCalisanById(id);
        assertNotNull(fromDb);
        assertEquals("Ahmet", fromDb.getName());
        assertEquals(1, fromDb.getRestaurantId());
    }

    @Test
    void updateCalisan_shouldUpdateRow() {
        jdbcTemplate.update(
                "INSERT INTO calisan (name, restaurant_id) VALUES (?, ?)",
                "Mehmet", 2
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM calisan ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Calisan updated = new Calisan.CalisanBuilder()
                .setId(999) // service update'da WHERE için ayrıca id parametresi kullanıyor; burada önemli değil
                .setName("Mehmet Güncel")
                .setRestaurantId(5)
                .build();

        String msg = calisanService.updateCalisan(id, updated);
        assertEquals("Çalışan başarıyla güncellendi.", msg);

        Calisan fromDb = calisanService.getCalisanById(id);
        assertNotNull(fromDb);
        assertEquals("Mehmet Güncel", fromDb.getName());
        assertEquals(5, fromDb.getRestaurantId());
    }

    @Test
    void deleteCalisan_shouldDeleteRow() {
        jdbcTemplate.update(
                "INSERT INTO calisan (name, restaurant_id) VALUES (?, ?)",
                "Zeynep", 3
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM calisan ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = calisanService.deleteCalisan(id);
        assertEquals("Çalışan başarıyla silindi!", msg);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM calisan WHERE id = ?", Integer.class, id);
        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    void getAllCalisanlar_shouldReturnList() {
        jdbcTemplate.update("INSERT INTO calisan (name, restaurant_id) VALUES (?, ?)", "A", 1);
        jdbcTemplate.update("INSERT INTO calisan (name, restaurant_id) VALUES (?, ?)", "B", 1);

        List<Calisan> list = calisanService.getAllCalisanlar();
        assertNotNull(list);
        assertTrue(list.size() >= 2);
    }
}
