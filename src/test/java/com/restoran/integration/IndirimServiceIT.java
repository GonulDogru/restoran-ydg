package com.restoran.integration;

import com.restoran.model.Indirim;
import com.restoran.repository.IndirimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class IndirimServiceIT {

    @Autowired
    private IndirimService indirimService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM indirim");
        jdbcTemplate.update("DELETE FROM kullanici");
    }

    private int insertUser() {
        jdbcTemplate.update("INSERT INTO kullanici (username, password, telefon, adres) VALUES (?,?,?,?)",
                "u2", "p2", "05000000001", "adres2");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM kullanici ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    @Test
    void createIndirim_thenGetById_shouldReturnSameData() {
        int userId = insertUser();

        Indirim i = new Indirim.IndirimBuilder()
                .setId(0)
                .setAmount(new BigDecimal("15.50"))
                .setKullaniciId(userId)
                .build();

        String msg = indirimService.createIndirim(i);
        assertEquals("İndirim başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM indirim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Indirim fromDb = indirimService.getIndirimById(id);
        assertNotNull(fromDb);
        assertEquals(new BigDecimal("15.50"), fromDb.getAmount());
        assertEquals(userId, fromDb.getKullaniciId());
    }

    @Test
    void updateIndirim_shouldUpdateRow() {
        int userId = insertUser();
        jdbcTemplate.update("INSERT INTO indirim (amount, kullanici_id) VALUES (?,?)", new BigDecimal("5.00"), userId);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM indirim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Indirim updated = new Indirim.IndirimBuilder()
                .setId(999)
                .setAmount(new BigDecimal("20.00"))
                .setKullaniciId(userId)
                .build();

        String msg = indirimService.updateIndirim(id, updated);
        assertEquals("İndirim başarıyla güncellendi.", msg);

        Indirim fromDb = indirimService.getIndirimById(id);
        assertNotNull(fromDb);
        assertEquals(new BigDecimal("20.00"), fromDb.getAmount());
    }

    @Test
    void deleteIndirim_shouldDeleteRow() {
        int userId = insertUser();
        jdbcTemplate.update("INSERT INTO indirim (amount, kullanici_id) VALUES (?,?)", new BigDecimal("7.00"), userId);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM indirim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = indirimService.deleteIndirim(id);
        assertEquals("İndirim başarıyla silindi!", msg);

        assertNull(indirimService.getIndirimById(id));
    }
}
