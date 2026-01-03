package com.restoran.repository;

import com.restoran.model.Yemek;
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
class YemekServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private YemekService yemekService;

    @Test
    void getAllYemekler_shouldQueryAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM yemek"), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Yemek> result = yemekService.getAllYemekler();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM yemek"), any(RowMapper.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void createYemek_shouldInsert() {
        Yemek yemek = new Yemek.YemekBuilder()
                .setId(0)
                .setName("Adana")
                .setMenuId(3)
                .build();

        yemekService.createYemek(yemek);

        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO yemek (name, menu_id) VALUES (?, ?)"),
                eq("Adana"),
                eq(3)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getYemekById_shouldQueryById() {
        Yemek expected = new Yemek.YemekBuilder()
                .setId(1)
                .setName("Lahmacun")
                .setMenuId(2)
                .build();

        when(jdbcTemplate.queryForObject(eq("SELECT * FROM yemek WHERE id = ?"), any(RowMapper.class), eq(1)))
                .thenReturn(expected);

        Yemek result = yemekService.getYemekById(1);

        assertEquals(expected, result);
        verify(jdbcTemplate, times(1)).queryForObject(eq("SELECT * FROM yemek WHERE id = ?"), any(RowMapper.class), eq(1));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateYemek_shouldUpdateById() {
        Yemek yemek = new Yemek.YemekBuilder()
                .setId(999)
                .setName("Updated")
                .setMenuId(7)
                .build();

        yemekService.updateYemek(10, yemek);

        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE yemek SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Updated"),
                eq(7),
                eq(10)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteYemek_shouldDeleteById() {
        yemekService.deleteYemek(6);

        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM yemek WHERE id = ?"), eq(6));
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
