package com.restoran.integration;

import com.restoran.model.Rezervasyon;
import com.restoran.repository.RezervasyonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class RezervasyonServiceIT {

    @Autowired
    private RezervasyonService rezervasyonService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM rezervasyon");
    }

    @Test
    void createRezervasyon_thenGetById_shouldReturnSameData() {
        Rezervasyon r = new Rezervasyon.RezervasyonBuilder()
                .setId(0)
                .setTarih("2025-12-30")
                .setSaat("19:30")
                .setUserId(7)
                .setMasaId(2)
                .build();

        String msg = rezervasyonService.createRezervasyon(r);
        assertEquals("Rezervasyon başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM rezervasyon WHERE user_id = ? AND masa_id = ? ORDER BY id DESC LIMIT 1",
                Integer.class,
                7, 2
        );
        assertNotNull(id);

        Rezervasyon fromDb = rezervasyonService.getRezervasyonById(id);
        assertNotNull(fromDb);
        assertEquals("2025-12-30", fromDb.getTarih());
        assertEquals("19:30", fromDb.getSaat());
        assertEquals(7, fromDb.getUserId());
        assertEquals(2, fromDb.getMasaId());
    }

    @Test
    void createRezervasyon_withInvalidTime_shouldThrowIllegalArgumentException() {
        Rezervasyon r = new Rezervasyon.RezervasyonBuilder()
                .setId(0)
                .setTarih("2025-12-30")
                .setSaat("19.30") // yanlış format (HH:mm olmalı)
                .setUserId(1)
                .setMasaId(1)
                .build();

        assertThrows(IllegalArgumentException.class, () -> rezervasyonService.createRezervasyon(r));
    }

    @Test
    void deleteRezervasyon_thenGetById_shouldReturnNull() {
        jdbcTemplate.update(
                "INSERT INTO rezervasyon (tarih, saat, user_id, masa_id) VALUES (?, ?, ?, ?)",
                java.sql.Date.valueOf("2025-12-01"),
                java.sql.Time.valueOf("18:00:00"),
                1, 1
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM rezervasyon ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = rezervasyonService.deleteRezervasyon(id);
        assertEquals("Rezervasyon başarıyla silindi!", msg);

        Rezervasyon fromDb = rezervasyonService.getRezervasyonById(id);
        assertNull(fromDb);
    }
}
