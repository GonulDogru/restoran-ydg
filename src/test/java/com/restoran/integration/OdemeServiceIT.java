package com.restoran.integration;

import com.restoran.model.Odeme;
import com.restoran.repository.RestaurantService;
import com.restoran.strategy.CashPayment;
import com.restoran.strategy.CreditCardPayment;
import com.restoran.strategy.PaymentStrategy;
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
class OdemeServiceIT {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM odeme");
    }

    @Test
    void cashPaymentFlow_save_thenGetAll_thenGetById_shouldMatch() {
        PaymentStrategy strategy = new CashPayment();

        Odeme odeme = new Odeme.OdemeBuilder()
                .setId(0)
                .setMasaId(3)
                .setTutar(120.0)
                .setOdemeYontemi(strategy.getStrategyName()) // "Nakit"
                .build();

        // INSERT
        restaurantService.saveOdeme(odeme);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM odeme ORDER BY id DESC LIMIT 1",
                Integer.class
        );
        assertNotNull(id);

        // GET BY ID
        Odeme fromDb = restaurantService.getOdemeById(id);
        assertNotNull(fromDb);
        assertEquals(3, fromDb.getMasaId());
        assertEquals(120.0, fromDb.getTutar(), 0.0001);
        assertEquals("Nakit", fromDb.getOdemeYontemi());

        // GET ALL
        List<Odeme> all = restaurantService.getAllOdemeler();
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    void creditCardPayment_update_thenDelete_shouldWork() {
        PaymentStrategy strategy = new CreditCardPayment();

        // önce insert
        Odeme initial = new Odeme.OdemeBuilder()
                .setId(0)
                .setMasaId(1)
                .setTutar(50.0)
                .setOdemeYontemi(strategy.getStrategyName()) // "Kredi Kartı"
                .build();
        restaurantService.saveOdeme(initial);

        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM odeme ORDER BY id DESC LIMIT 1",
                Integer.class
        );
        assertNotNull(id);

        // update
        Odeme updated = new Odeme.OdemeBuilder()
                .setId(999)
                .setMasaId(5)
                .setTutar(99.9)
                .setOdemeYontemi("Nakit")
                .build();

        restaurantService.updateOdeme(id, updated);

        Odeme fromDb = restaurantService.getOdemeById(id);
        assertNotNull(fromDb);
        assertEquals(5, fromDb.getMasaId());
        assertEquals(99.9, fromDb.getTutar(), 0.0001);
        assertEquals("Nakit", fromDb.getOdemeYontemi());

        // delete
        restaurantService.deleteOdeme(id);
        assertNull(restaurantService.getOdemeById(id));
    }
}
