package com.restoran.integration;

import com.restoran.model.Geribildirim;
import com.restoran.repository.GeribildirimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class GeribildirimServiceIT {

    @Autowired
    private GeribildirimService geribildirimService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM geribildirim");
        jdbcTemplate.update("DELETE FROM kullanici");
    }

    private int insertUser() {
        jdbcTemplate.update("INSERT INTO kullanici (username, password, telefon, adres) VALUES (?,?,?,?)",
                "u1", "p1", "05000000000", "adres");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM kullanici ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    @Test
    void createGeribildirim_thenGetById_shouldReturnSameData() {
        int userId = insertUser();

        Geribildirim g = new Geribildirim.GeribildirimBuilder()
                .setId(0)
                .setUserId(userId)
                .setYorum("Çok iyi")
                .build();

        String msg = geribildirimService.createGeribildirim(g);
        assertEquals("Geribildirim başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM geribildirim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Geribildirim fromDb = geribildirimService.getGeribildirimById(id);
        assertNotNull(fromDb);
        assertEquals(userId, fromDb.getUserId());
        assertEquals("Çok iyi", fromDb.getYorum());
    }

    @Test
    void updateGeribildirim_shouldUpdateRow() {
        int userId = insertUser();
        jdbcTemplate.update("INSERT INTO geribildirim (yorum, user_id) VALUES (?,?)", "Eski", userId);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM geribildirim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Geribildirim updated = new Geribildirim.GeribildirimBuilder()
                .setId(999)
                .setUserId(userId)
                .setYorum("Yeni")
                .build();

        String msg = geribildirimService.updateGeribildirim(id, updated);
        assertEquals("Geribildirim başarıyla güncellendi.", msg);

        Geribildirim fromDb = geribildirimService.getGeribildirimById(id);
        assertNotNull(fromDb);
        assertEquals("Yeni", fromDb.getYorum());
    }

    @Test
    void deleteGeribildirim_shouldDeleteRow() {
        int userId = insertUser();
        jdbcTemplate.update("INSERT INTO geribildirim (yorum, user_id) VALUES (?,?)", "Silinecek", userId);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM geribildirim ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = geribildirimService.deleteGeribildirim(id);
        assertEquals("Geribildirim başarıyla silindi!", msg);

        assertNull(geribildirimService.getGeribildirimById(id));
    }
}
