package com.restoran.repository;

import com.restoran.model.Icecek;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
    void createIcecek_shouldInsertAndReturnSuccessMessage_whenRowsPositive() {
        Icecek icecek = new Icecek.IcecekBuilder()
                .setId(0)
                .setName("Kola")
                .setMenuId(2)
                .build();

        when(jdbcTemplate.update(
                eq("INSERT INTO icecek (name, menu_id) VALUES (?, ?)"),
                eq("Kola"), eq(2)
        )).thenReturn(1);

        String msg = icecekService.createIcecek(icecek);

        assertEquals("İçecek başarıyla eklendi.", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO icecek (name, menu_id) VALUES (?, ?)"),
                eq("Kola"), eq(2)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getAllIcecekler_shouldQueryTable() {
        when(jdbcTemplate.query(
                eq("SELECT * FROM icecek"),
                ArgumentMatchers.<RowMapper<Icecek>>any()
        )).thenReturn(List.of());

        List<Icecek> list = icecekService.getAllIcecekler();

        assertNotNull(list);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM icecek"),
                ArgumentMatchers.<RowMapper<Icecek>>any()
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getIcecekById_shouldReturnFirstElement_whenFound() {
        int id = 5;
        Icecek expected = new Icecek.IcecekBuilder()
                .setId(id)
                .setName("Ayran")
                .setMenuId(3)
                .build();

        // DİKKAT: Service query(...) kullanıyor, queryForObject değil.
        when(jdbcTemplate.query(
                eq("SELECT * FROM icecek WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Icecek>>any(),
                eq(id)
        )).thenReturn(List.of(expected));

        Icecek result = icecekService.getIcecekById(id);

        assertSame(expected, result);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM icecek WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Icecek>>any(),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getIcecekById_shouldReturnNull_whenNotFound() {
        int id = 999;

        when(jdbcTemplate.query(
                eq("SELECT * FROM icecek WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Icecek>>any(),
                eq(id)
        )).thenReturn(List.of());

        Icecek result = icecekService.getIcecekById(id);

        assertNull(result);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM icecek WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Icecek>>any(),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateIcecek_shouldExecuteUpdateAndReturnSuccess_whenRowsPositive() {
        int id = 7;
        Icecek icecek = new Icecek.IcecekBuilder()
                .setId(id)
                .setName("Fanta")
                .setMenuId(1)
                .build();

        when(jdbcTemplate.update(
                eq("UPDATE icecek SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Fanta"), eq(1), eq(id)
        )).thenReturn(1);

        String msg = icecekService.updateIcecek(id, icecek);

        assertEquals("İçecek başarıyla güncellendi.", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE icecek SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Fanta"), eq(1), eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteIcecek_shouldExecuteDeleteAndReturnSuccess_whenRowsPositive() {
        int id = 4;

        when(jdbcTemplate.update(
                eq("DELETE FROM icecek WHERE id = ?"),
                eq(id)
        )).thenReturn(1);

        String msg = icecekService.deleteIcecek(id);

        assertEquals("İçecek başarıyla silindi!", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM icecek WHERE id = ?"),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
