package com.restoran.repository;

import com.restoran.model.Siparis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiparisServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SiparisService siparisService;

    @Test
    void createSiparis_whenRowsPositive_shouldReturnSuccessMessage() {
        Siparis siparis = new Siparis.SiparisBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setAmount(100.0f)
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?, ?, ?, ?)";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                eq(100.0f),
                eq(1),
                eq(2)
        )).thenReturn(1);

        String result = siparisService.createSiparis(siparis);

        assertEquals("Sipariş başarıyla eklendi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql),
                ArgumentMatchers.any(Date.class),
                eq(100.0f),
                eq(1),
                eq(2)
        );
    }

    @Test
    void createSiparis_whenRowsZero_shouldReturnFailureMessage() {
        Siparis siparis = new Siparis.SiparisBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setAmount(100.0f)
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?, ?, ?, ?)";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                eq(100.0f),
                eq(1),
                eq(2)
        )).thenReturn(0);

        String result = siparisService.createSiparis(siparis);

        assertEquals("Sipariş eklenemedi.", result);
    }
    @Test
    void createSiparis_whenInvalidDate_shouldNotCrash_andReturnFailureMessage() {
        Siparis siparis = new Siparis.SiparisBuilder()
                .setId(0)
                .setTarih("01-01-2026") // service bunu exception'a çevirmiyor
                .setAmount(10.0f)
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?, ?, ?, ?)";
        when(jdbcTemplate.update(eq(sql),
                any(),              // tarih tipi Date/LocalDate olabilir, burada esnek bırakıyoruz
                eq(10.0f),
                eq(1),
                eq(2)
        )).thenReturn(0);

        String result = siparisService.createSiparis(siparis);

        // invalid tarih geldiğinde servis crash olmuyor; eklenemedi senaryosu dönebilir
        assertEquals("Sipariş eklenemedi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), any(), eq(10.0f), eq(1), eq(2));
    }


    @Test
    void getAllSiparisler_shouldQueryTable() {
        String sql = "SELECT * FROM siparis";
        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Siparis>>any()))
                .thenReturn(List.of());

        List<Siparis> result = siparisService.getAllSiparisler();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Siparis>>any());
    }

    @Test
    void getSiparisById_whenEmpty_shouldReturnNull() {
        int id = 99;
        String sql = "SELECT * FROM siparis WHERE id = ?";

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Siparis>>any(), eq(id)))
                .thenReturn(List.of());

        Siparis result = siparisService.getSiparisById(id);

        assertNull(result);
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Siparis>>any(), eq(id));
    }

    @Test
    void getSiparisById_whenFound_shouldReturnFirst() {
        int id = 1;
        String sql = "SELECT * FROM siparis WHERE id = ?";

        Siparis found = new Siparis.SiparisBuilder()
                .setId(id)
                .setTarih("2026-01-01")
                .setAmount(5.0f)
                .setUserId(2)
                .setMasaId(3)
                .build();

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Siparis>>any(), eq(id)))
                .thenReturn(List.of(found));

        Siparis result = siparisService.getSiparisById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Siparis>>any(), eq(id));
    }

    @Test
    void updateSiparis_whenRowsPositive_shouldReturnSuccessMessage() {
        int id = 7;
        Siparis siparis = new Siparis.SiparisBuilder()
                .setId(id)
                .setTarih("2026-01-01")
                .setAmount(77.7f)
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "UPDATE siparis SET tarih = ?, amount = ?, user_id = ?, masa_id = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                eq(77.7f),
                eq(1),
                eq(2),
                eq(id)
        )).thenReturn(1);

        String result = siparisService.updateSiparis(id, siparis);

        assertEquals("Sipariş başarıyla güncellendi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql),
                ArgumentMatchers.any(Date.class),
                eq(77.7f),
                eq(1),
                eq(2),
                eq(id)
        );
    }

    @Test
    void updateSiparis_whenRowsZero_shouldReturnNotFoundMessage() {
        int id = 7;
        Siparis siparis = new Siparis.SiparisBuilder()
                .setId(id)
                .setTarih("2026-01-01")
                .setAmount(77.7f)
                .setUserId(1)
                .setMasaId(2)
                .build();

        String sql = "UPDATE siparis SET tarih = ?, amount = ?, user_id = ?, masa_id = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql),
                ArgumentMatchers.any(Date.class),
                eq(77.7f),
                eq(1),
                eq(2),
                eq(id)
        )).thenReturn(0);

        String result = siparisService.updateSiparis(id, siparis);

        assertEquals("Sipariş bulunamadı!", result);
    }

    @Test
    void deleteSiparis_whenRowsPositive_shouldReturnSuccessMessage() {
        int id = 3;
        String sql = "DELETE FROM siparis WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(1);

        String result = siparisService.deleteSiparis(id);

        assertEquals("Sipariş başarıyla silindi!", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq(id));
    }

    @Test
    void deleteSiparis_whenRowsZero_shouldReturnNotFoundMessage() {
        int id = 3;
        String sql = "DELETE FROM siparis WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(0);

        String result = siparisService.deleteSiparis(id);

        assertEquals("Sipariş bulunamadı!", result);
    }
}
