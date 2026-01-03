package com.restoran.repository;

import com.restoran.model.Tatli;
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
class TatliServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TatliService tatliService;

    @Test
    void createTatli_rowsPositive_shouldReturnSuccessMessage() {
        Tatli tatli = new Tatli.TatliBuilder().setId(0).setName("Baklava").setMenuId(2).build();

        when(jdbcTemplate.update(eq("INSERT INTO tatli (name, menu_id) VALUES (?, ?)"), eq("Baklava"), eq(2)))
                .thenReturn(1);

        String result = tatliService.createTatli(tatli);

        assertEquals("Tatlı başarıyla eklendi.", result);
        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO tatli (name, menu_id) VALUES (?, ?)"), eq("Baklava"), eq(2));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getAllTatlilar_shouldQueryAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM tatli"), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Tatli> result = tatliService.getAllTatlilar();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM tatli"), any(RowMapper.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getTatliById_shouldQueryById() {
        Tatli expected = new Tatli.TatliBuilder().setId(1).setName("Sütlaç").setMenuId(3).build();

        when(jdbcTemplate.queryForObject(eq("SELECT * FROM tatli WHERE id = ?"), any(RowMapper.class), eq(1)))
                .thenReturn(expected);

        Tatli result = tatliService.getTatliById(1);

        assertEquals(expected, result);
        verify(jdbcTemplate, times(1)).queryForObject(eq("SELECT * FROM tatli WHERE id = ?"), any(RowMapper.class), eq(1));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateTatli_rowsPositive_shouldReturnSuccessMessage() {
        Tatli tatli = new Tatli.TatliBuilder().setId(999).setName("Künefe").setMenuId(7).build();

        when(jdbcTemplate.update(
                eq("UPDATE tatli SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Künefe"), eq(7), eq(10)
        )).thenReturn(1);

        String result = tatliService.updateTatli(10, tatli);

        assertEquals("Tatlı başarıyla güncellendi.", result);
        verify(jdbcTemplate, times(1)).update(eq("UPDATE tatli SET name = ?, menu_id = ? WHERE id = ?"), eq("Künefe"), eq(7), eq(10));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteTatli_rowsPositive_shouldReturnSuccessMessage() {
        when(jdbcTemplate.update(eq("DELETE FROM tatli WHERE id = ?"), eq(3))).thenReturn(1);

        String result = tatliService.deleteTatli(3);

        assertEquals("Tatlı başarıyla silindi!", result);
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM tatli WHERE id = ?"), eq(3));
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
