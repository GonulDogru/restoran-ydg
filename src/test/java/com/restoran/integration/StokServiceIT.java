package com.restoran.integration;

import com.restoran.model.Stok;
import com.restoran.repository.StokService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class StokServiceIT {

    @Autowired
    private StokService stokService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM stok");
        jdbcTemplate.update("DELETE FROM yemek");
        jdbcTemplate.update("DELETE FROM icecek");
        jdbcTemplate.update("DELETE FROM tatli");
        jdbcTemplate.update("DELETE FROM tedarikci");
        jdbcTemplate.update("DELETE FROM menu");
        jdbcTemplate.update("DELETE FROM restaurant");
    }

    private int insertRestaurant() {
        jdbcTemplate.update("INSERT INTO restaurant (name, adres) VALUES (?,?)", "R1", "Adres1");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM restaurant ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertMenu(int restaurantId) {
        jdbcTemplate.update("INSERT INTO menu (name, restaurant_id) VALUES (?,?)", "Menu1", restaurantId);
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM menu ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertYemek(int menuId) {
        jdbcTemplate.update("INSERT INTO yemek (menu_id, name) VALUES (?,?)", menuId, "Yemek1");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM yemek ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertIcecek(int menuId) {
        jdbcTemplate.update("INSERT INTO icecek (menu_id, name) VALUES (?,?)", menuId, "Icecek1");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM icecek ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertTatli(int menuId) {
        jdbcTemplate.update("INSERT INTO tatli (menu_id, name) VALUES (?,?)", menuId, "Tatli1");
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM tatli ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    private int insertTedarikci(int restaurantId) {
        jdbcTemplate.update("INSERT INTO tedarikci (name, phone, restaurant_id) VALUES (?,?,?)",
                "Tedarikci1", "05550000000", restaurantId);
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM tedarikci ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);
        return id;
    }

    @Test
    void createStok_thenGetById_shouldReturnSameData() {
        int restaurantId = insertRestaurant();
        int menuId = insertMenu(restaurantId);
        int yemekId = insertYemek(menuId);
        int icecekId = insertIcecek(menuId);
        int tatliId = insertTatli(menuId);
        int tedarikciId = insertTedarikci(restaurantId);

        Stok s = new Stok.StokBuilder()
                .setId(0)
                .setMiktar(25)
                .setYemekId(yemekId)
                .setIcecekId(icecekId)
                .setTatliId(tatliId)
                .setTedarikciId(tedarikciId)
                .build();

        String msg = stokService.createStok(s);
        assertEquals("Stok başarıyla eklendi.", msg);

        Integer id = jdbcTemplate.queryForObject("SELECT id FROM stok ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Stok fromDb = stokService.getStokById(id);
        assertNotNull(fromDb);
        assertEquals(25, fromDb.getMiktar());
        assertEquals(yemekId, fromDb.getYemekId());
        assertEquals(icecekId, fromDb.getIcecekId());
        assertEquals(tatliId, fromDb.getTatliId());
        assertEquals(tedarikciId, fromDb.getTedarikciId());
    }

    @Test
    void updateStok_shouldUpdateRow() {
        int restaurantId = insertRestaurant();
        int menuId = insertMenu(restaurantId);
        int yemekId = insertYemek(menuId);
        int icecekId = insertIcecek(menuId);
        int tatliId = insertTatli(menuId);
        int tedarikciId = insertTedarikci(restaurantId);

        jdbcTemplate.update(
                "INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?,?,?,?,?)",
                10, yemekId, icecekId, tatliId, tedarikciId
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM stok ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        Stok updated = new Stok.StokBuilder()
                .setId(999)
                .setMiktar(99)
                .setYemekId(yemekId)
                .setIcecekId(icecekId)
                .setTatliId(tatliId)
                .setTedarikciId(tedarikciId)
                .build();

        String msg = stokService.updateStok(id, updated);
        assertEquals("Stok başarıyla güncellendi.", msg);

        Stok fromDb = stokService.getStokById(id);
        assertNotNull(fromDb);
        assertEquals(99, fromDb.getMiktar());
    }

    @Test
    void deleteStok_shouldDeleteRow() {
        jdbcTemplate.update(
                "INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?,?,?,?,?)",
                5, 1, 1, 1, 1
        );
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM stok ORDER BY id DESC LIMIT 1", Integer.class);
        assertNotNull(id);

        String msg = stokService.deleteStok(id);
        assertEquals("Stok başarıyla silindi!", msg);

        assertNull(stokService.getStokById(id));
    }
}
