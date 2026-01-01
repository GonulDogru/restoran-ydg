package com.restoran.repository;

import com.restoran.model.Rezervasyon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RezervasyonServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RezervasyonService rezervasyonService;

    @Test
    void createRezervasyon_whenRowsPositive_shouldReturnSuccessMessage() {
        Rezervasyon rezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setSaat("19:30")
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "INSERT INTO rezervasyon (tarih, saat, user_id, masa_id) VALUES (?, ?, ?, ?)";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Time.class),
                eq(1),
                eq(2)
        )).thenReturn(1);

        String result = rezervasyonService.createRezervasyon(rezervasyon);

        assertEquals("Rezervasyon başarıyla eklendi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql),
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Time.class),
                eq(1),
                eq(2)
        );
    }

    @Test
    void createRezervasyon_whenRowsZero_shouldReturnFailureMessage() {
        Rezervasyon rezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setSaat("19:30")
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "INSERT INTO rezervasyon (tarih, saat, user_id, masa_id) VALUES (?, ?, ?, ?)";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Time.class),
                eq(1),
                eq(2)
        )).thenReturn(0);

        String result = rezervasyonService.createRezervasyon(rezervasyon);

        assertEquals("Rezervasyon eklenemedi.", result);
    }

    @Test
    void createRezervasyon_whenInvalidTime_shouldThrow_andNotCallJdbc() {
        Rezervasyon rezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setSaat("19.30") // yanlış format
                .setUserId(1)
                .setMasaId(2)
                .build();

        assertThrows(IllegalArgumentException.class, () -> rezervasyonService.createRezervasyon(rezervasyon));
        verifyNoInteractions(jdbcTemplate);
    }

    @Test
    void getAllRezervasyonlar_shouldQueryTable() {
        String sql = "SELECT * FROM rezervasyon";
        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Rezervasyon>>any()))
                .thenReturn(List.of());

        List<Rezervasyon> result = rezervasyonService.getAllRezervasyonlar();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Rezervasyon>>any());
    }

    @Test
    void getRezervasyonById_whenEmpty_shouldReturnNull() {
        int id = 99;
        String sql = "SELECT * FROM rezervasyon WHERE id = ?";

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Rezervasyon>>any(), eq(id)))
                .thenReturn(List.of());

        Rezervasyon result = rezervasyonService.getRezervasyonById(id);

        assertNull(result);
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Rezervasyon>>any(), eq(id));
    }

    @Test
    void getRezervasyonById_whenFound_shouldReturnFirst() {
        int id = 1;
        String sql = "SELECT * FROM rezervasyon WHERE id = ?";

        Rezervasyon found = new Rezervasyon.RezervasyonBuilder()
                .setId(id)
                .setTarih("2026-01-01")
                .setSaat("20:00")
                .setUserId(2)
                .setMasaId(3)
                .build();

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Rezervasyon>>any(), eq(id)))
                .thenReturn(List.of(found));

        Rezervasyon result = rezervasyonService.getRezervasyonById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Rezervasyon>>any(), eq(id));
    }

    @Test
    void updateRezervasyon_whenRowsPositive_shouldReturnSuccessMessage() {
        int id = 7;
        Rezervasyon rezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(id)
                .setTarih("2026-01-01")
                .setSaat("19:30")
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "UPDATE rezervasyon SET tarih = ?, saat = ?, user_id = ?, masa_id = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Time.class),
                eq(1),
                eq(2),
                eq(id)
        )).thenReturn(1);

        String result = rezervasyonService.updateRezervasyon(id, rezervasyon);

        assertEquals("Rezervasyon başarıyla güncellendi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql),
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Time.class),
                eq(1),
                eq(2),
                eq(id)
        );
    }

    @Test
    void updateRezervasyon_whenRowsZero_shouldReturnNotFoundMessage() {
        int id = 7;
        Rezervasyon rezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(id)
                .setTarih("2026-01-01")
                .setSaat("19:30")
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "UPDATE rezervasyon SET tarih = ?, saat = ?, user_id = ?, masa_id = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Time.class),
                eq(1),
                eq(2),
                eq(id)
        )).thenReturn(0);

        String result = rezervasyonService.updateRezervasyon(id, rezervasyon);

        assertEquals("Rezervasyon bulunamadı!", result);
    }

    @Test
    void deleteRezervasyon_whenRowsPositive_shouldReturnSuccessMessage() {
        int id = 3;
        String sql = "DELETE FROM rezervasyon WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(1);

        String result = rezervasyonService.deleteRezervasyon(id);

        assertEquals("Rezervasyon başarıyla silindi!", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq(id));
    }

    @Test
    void deleteRezervasyon_whenRowsZero_shouldReturnNotFoundMessage() {
        int id = 3;
        String sql = "DELETE FROM rezervasyon WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(0);

        String result = rezervasyonService.deleteRezervasyon(id);

        assertEquals("Rezervasyon bulunamadı!", result);
    }
}
