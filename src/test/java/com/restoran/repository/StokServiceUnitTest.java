package com.restoran.repository;

import com.restoran.model.Stok;
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
class StokServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StokService stokService;

    @Test
    void createStok_rowsPositive_shouldReturnSuccessMessage() {
        Stok stok = new Stok.StokBuilder()
                .setId(0)
                .setMiktar(50)
                .setYemekId(1)
                .setIcecekId(2)
                .setTatliId(3)
                .setTedarikciId(4)
                .build();

        when(jdbcTemplate.update(
                eq("INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?, ?, ?, ?, ?)"),
                eq(50), eq(1), eq(2), eq(3), eq(4)
        )).thenReturn(1);

        String result = stokService.createStok(stok);

        assertEquals("Stok başarıyla eklendi.", result);
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?, ?, ?, ?, ?)"),
                eq(50), eq(1), eq(2), eq(3), eq(4)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getAllStoklar_shouldQueryAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM stok"), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Stok> result = stokService.getAllStoklar();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM stok"), any(RowMapper.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getStokById_shouldQueryById() {
        Stok expected = new Stok.StokBuilder()
                .setId(1).setMiktar(10).setYemekId(1).setIcecekId(0).setTatliId(0).setTedarikciId(2)
                .build();

        when(jdbcTemplate.queryForObject(eq("SELECT * FROM stok WHERE id = ?"), any(RowMapper.class), eq(1)))
                .thenReturn(expected);

        Stok result = stokService.getStokById(1);

        assertEquals(expected, result);
        verify(jdbcTemplate, times(1)).queryForObject(eq("SELECT * FROM stok WHERE id = ?"), any(RowMapper.class), eq(1));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateStok_rowsPositive_shouldReturnSuccessMessage() {
        Stok stok = new Stok.StokBuilder()
                .setId(999)
                .setMiktar(99)
                .setYemekId(7)
                .setIcecekId(8)
                .setTatliId(9)
                .setTedarikciId(10)
                .build();

        when(jdbcTemplate.update(
                eq("UPDATE stok SET miktar = ?, yemek_id = ?, icecek_id = ?, tatli_id = ?, tedarikci_id = ? WHERE id = ?"),
                eq(99), eq(7), eq(8), eq(9), eq(10), eq(5)
        )).thenReturn(1);

        String result = stokService.updateStok(5, stok);

        assertEquals("Stok başarıyla güncellendi.", result);
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE stok SET miktar = ?, yemek_id = ?, icecek_id = ?, tatli_id = ?, tedarikci_id = ? WHERE id = ?"),
                eq(99), eq(7), eq(8), eq(9), eq(10), eq(5)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteStok_rowsPositive_shouldReturnSuccessMessage() {
        when(jdbcTemplate.update(eq("DELETE FROM stok WHERE id = ?"), eq(3))).thenReturn(1);

        String result = stokService.deleteStok(3);

        assertEquals("Stok başarıyla silindi!", result);
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM stok WHERE id = ?"), eq(3));
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
