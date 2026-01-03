package com.restoran.integration;

import com.restoran.model.Siparis;
import com.restoran.repository.SiparisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class SiparisServiceIT {

    @Autowired
    private SiparisService siparisService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM siparis");
    }

    @Test
    void createSiparis_thenGetById_shouldReturnSameData() {
        Siparis s = new Siparis.SiparisBuilder()
                .setId(0)
                .setTarih("2025-12-30")
                .setAmount(120.5f)
                .setUserId(10)
                .setMasaId(3)
                .build();

        String msg = siparisService.createSiparis(s);
        assertEquals("Sipariş başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM siparis WHERE user_id = ? AND masa_id = ? ORDER BY id DESC LIMIT 1",
                Integer.class,
                10, 3
        );
        assertNotNull(id);

        Siparis fromDb = siparisService.getSiparisById(id);
        assertNotNull(fromDb);
        assertEquals("2025-12-30", fromDb.getTarih());
        assertEquals(120.5f, fromDb.getAmount(), 0.0001);
        assertEquals(10, fromDb.getUserId());
        assertEquals(3, fromDb.getMasaId());
    }

    @Test
    void createSiparis_withInvalidDate_shouldThrowIllegalArgumentException() {
        Siparis s = new Siparis.SiparisBuilder()
                .setId(0)
                .setTarih("30-12-2025") // yanlış format (yyyy-MM-dd olmalı)
                .setAmount(10f)
                .setUserId(1)
                .setMasaId(1)
                .build();

        assertThrows(IllegalArgumentException.class, () -> siparisService.createSiparis(s));
    }

    @Test
    void updateSiparis_shouldUpdateRow() {
        jdbcTemplate.update(
                "INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?, ?, ?, ?)",
                java.sql.Date.valueOf("2025-12-01"), 50.0, 1, 1
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM siparis ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Siparis updated = new Siparis.SiparisBuilder()
                .setId(999)
                .setTarih("2025-12-31")
                .setAmount(99.9f)
                .setUserId(2)
                .setMasaId(5)
                .build();

        String msg = siparisService.updateSiparis(id, updated);
        assertEquals("Sipariş başarıyla güncellendi.", msg);

        Siparis fromDb = siparisService.getSiparisById(id);
        assertNotNull(fromDb);
        assertEquals("2025-12-31", fromDb.getTarih());
        assertEquals(99.9f, fromDb.getAmount(), 0.0001);
        assertEquals(2, fromDb.getUserId());
        assertEquals(5, fromDb.getMasaId());
    }
}
