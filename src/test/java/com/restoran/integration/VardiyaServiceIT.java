package com.restoran.integration;

import com.restoran.model.Vardiya;
import com.restoran.repository.VardiyaService;
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
class VardiyaServiceIT {

    @Autowired
    private VardiyaService vardiyaService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM vardiya");
        jdbcTemplate.update("DELETE FROM calisan");
    }

    private int createCalisanAndGetId(String name, int restaurantId) {
        jdbcTemplate.update("INSERT INTO calisan (name, restaurant_id) VALUES (?, ?)", name, restaurantId);
        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM calisan WHERE name = ? AND restaurant_id = ? ORDER BY id DESC LIMIT 1",
                Integer.class,
                name, restaurantId
        );
        assertNotNull(id);
        return id;
    }

    @Test
    void createVardiya_thenGetById_shouldReturnSameData() {
        int calisanId = createCalisanAndGetId("Ali", 1);

        Vardiya v = new Vardiya.VardiyaBuilder()
                .setId(0)
                .setSaatler("09:00-17:00")
                .setCalisanId(calisanId)
                .build();

        String msg = vardiyaService.createVardiya(v);
        assertEquals("Vardiya başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM vardiya WHERE calisan_id = ? AND saatler = ? ORDER BY id DESC LIMIT 1",
                Integer.class,
                calisanId, "09:00-17:00"
        );
        assertNotNull(id);

        Vardiya fromDb = vardiyaService.getVardiyaById(id);
        assertNotNull(fromDb);
        assertEquals("09:00-17:00", fromDb.getSaatler());
        assertEquals(calisanId, fromDb.getCalisanId());
    }

    @Test
    void updateVardiya_shouldUpdateRow() {
        int calisanId1 = createCalisanAndGetId("Ayşe", 2);
        int calisanId2 = createCalisanAndGetId("Fatma", 2);

        jdbcTemplate.update(
                "INSERT INTO vardiya (saatler, calisan_id) VALUES (?, ?)",
                "10:00-18:00", calisanId1
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM vardiya ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Vardiya updated = new Vardiya.VardiyaBuilder()
                .setId(999) // update'da id parametresi kullanılıyor; burada önemli değil
                .setSaatler("12:00-20:00")
                .setCalisanId(calisanId2)
                .build();

        String msg = vardiyaService.updateVardiya(id, updated);
        assertEquals("Vardiya başarıyla güncellendi.", msg);

        Vardiya fromDb = vardiyaService.getVardiyaById(id);
        assertNotNull(fromDb);
        assertEquals("12:00-20:00", fromDb.getSaatler());
        assertEquals(calisanId2, fromDb.getCalisanId());
    }

    @Test
    void deleteVardiya_shouldDeleteRow() {
        int calisanId = createCalisanAndGetId("Can", 3);

        jdbcTemplate.update(
                "INSERT INTO vardiya (saatler, calisan_id) VALUES (?, ?)",
                "08:00-16:00", calisanId
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM vardiya ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = vardiyaService.deleteVardiya(id);
        assertEquals("Vardiya başarıyla silindi!", msg);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vardiya WHERE id = ?", Integer.class, id);
        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    void getAllVardiyalar_shouldReturnList() {
        int calisanId = createCalisanAndGetId("Deniz", 1);

        jdbcTemplate.update("INSERT INTO vardiya (saatler, calisan_id) VALUES (?, ?)", "09:00-17:00", calisanId);
        jdbcTemplate.update("INSERT INTO vardiya (saatler, calisan_id) VALUES (?, ?)", "17:00-01:00", calisanId);

        List<Vardiya> list = vardiyaService.getAllVardiyalar();
        assertNotNull(list);
        assertTrue(list.size() >= 2);
    }
}
