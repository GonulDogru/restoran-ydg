package com.restoran.repository;

import com.restoran.model.Icecek;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IcecekServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private IcecekService icecekService;

    @Test
    void createIcecek_rowsPositive_shouldReturnSuccessMessage() {
        Icecek icecek = new Icecek.IcecekBuilder().setId(0).setName("Kola").setMenuId(1).build();

        when(jdbcTemplate.update(eq("INSERT INTO icecek (name, menu_id) VALUES (?, ?)"), eq("Kola"), eq(1)))
                .thenReturn(1);

        String result = icecekService.createIcecek(icecek);

        assertEquals("İçecek başarıyla eklendi.", result);
        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO icecek (name, menu_id) VALUES (?, ?)"), eq("Kola"), eq(1));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void createIcecek_rowsZero_shouldReturnFailureMessage() {
        Icecek icecek = new Icecek.IcecekBuilder().setId(0).setName("Kola").setMenuId(1).build();

        when(jdbcTemplate.update(eq("INSERT INTO icecek (name, menu_id) VALUES (?, ?)"), eq("Kola"), eq(1)))
                .thenReturn(0);

        String result = icecekService.createIcecek(icecek);

        assertEquals("İçecek eklenemedi.", result);
        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO icecek (name, menu_id) VALUES (?, ?)"), eq("Kola"), eq(1));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getAllIcecekler_shouldQueryAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM icecek"), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Icecek> result = icecekService.getAllIcecekler();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM icecek"), any(RowMapper.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getIcecekById_shouldQueryById() {
        Icecek expected = new Icecek.IcecekBuilder().setId(2).setName("Ayran").setMenuId(1).build();

        when(jdbcTemplate.queryForObject(eq("SELECT * FROM icecek WHERE id = ?"), any(RowMapper.class), eq(2)))
                .thenReturn(expected);

        Icecek result = icecekService.getIcecekById(2);

        assertEquals(expected, result);
        verify(jdbcTemplate, times(1)).queryForObject(eq("SELECT * FROM icecek WHERE id = ?"), any(RowMapper.class), eq(2));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateIcecek_rowsPositive_shouldReturnSuccessMessage() {
        Icecek icecek = new Icecek.IcecekBuilder().setId(999).setName("Fanta").setMenuId(7).build();

        when(jdbcTemplate.update(
                eq("UPDATE icecek SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Fanta"), eq(7), eq(10)
        )).thenReturn(1);

        String result = icecekService.updateIcecek(10, icecek);

        assertEquals("İçecek başarıyla güncellendi.", result);
        verify(jdbcTemplate, times(1)).update(eq("UPDATE icecek SET name = ?, menu_id = ? WHERE id = ?"), eq("Fanta"), eq(7), eq(10));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteIcecek_rowsPositive_shouldReturnSuccessMessage() {
        when(jdbcTemplate.update(eq("DELETE FROM icecek WHERE id = ?"), eq(3))).thenReturn(1);

        String result = icecekService.deleteIcecek(3);

        assertEquals("İçecek başarıyla silindi!", result);
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM icecek WHERE id = ?"), eq(3));
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
