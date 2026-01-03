package com.restoran.repository;

import com.restoran.model.Tatli;
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
class TatliServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TatliService tatliService;

    @Test
    void createTatli_shouldInsertAndReturnSuccessMessage_whenRowsPositive() {
        Tatli tatli = new Tatli.TatliBuilder()
                .setId(0)
                .setName("Sütlaç")
                .setMenuId(2)
                .build();

        when(jdbcTemplate.update(
                eq("INSERT INTO tatli (name, menu_id) VALUES (?, ?)"),
                eq("Sütlaç"), eq(2)
        )).thenReturn(1);

        String msg = tatliService.createTatli(tatli);

        assertEquals("Tatlı başarıyla eklendi.", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO tatli (name, menu_id) VALUES (?, ?)"),
                eq("Sütlaç"), eq(2)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getAllTatlilar_shouldQueryTable() {
        when(jdbcTemplate.query(
                eq("SELECT * FROM tatli"),
                ArgumentMatchers.<RowMapper<Tatli>>any()
        )).thenReturn(List.of());

        List<Tatli> list = tatliService.getAllTatlilar();

        assertNotNull(list);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM tatli"),
                ArgumentMatchers.<RowMapper<Tatli>>any()
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getTatliById_shouldReturnFirstElement_whenFound() {
        int id = 5;
        Tatli expected = new Tatli.TatliBuilder()
                .setId(id)
                .setName("Baklava")
                .setMenuId(3)
                .build();

        // DİKKAT: Service query(...) kullanıyor.
        when(jdbcTemplate.query(
                eq("SELECT * FROM tatli WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Tatli>>any(),
                eq(id)
        )).thenReturn(List.of(expected));

        Tatli result = tatliService.getTatliById(id);

        assertSame(expected, result);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM tatli WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Tatli>>any(),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateTatli_shouldExecuteUpdateAndReturnSuccess_whenRowsPositive() {
        int id = 7;
        Tatli tatli = new Tatli.TatliBuilder()
                .setId(id)
                .setName("Künefe")
                .setMenuId(1)
                .build();

        when(jdbcTemplate.update(
                eq("UPDATE tatli SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Künefe"), eq(1), eq(id)
        )).thenReturn(1);

        String msg = tatliService.updateTatli(id, tatli);

        assertEquals("Tatlı başarıyla güncellendi.", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE tatli SET name = ?, menu_id = ? WHERE id = ?"),
                eq("Künefe"), eq(1), eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteTatli_shouldExecuteDeleteAndReturnSuccess_whenRowsPositive() {
        int id = 4;

        when(jdbcTemplate.update(
                eq("DELETE FROM tatli WHERE id = ?"),
                eq(id)
        )).thenReturn(1);

        String msg = tatliService.deleteTatli(id);

        assertEquals("Tatlı başarıyla silindi!", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM tatli WHERE id = ?"),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
