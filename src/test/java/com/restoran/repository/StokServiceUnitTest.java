package com.restoran.repository;

import com.restoran.model.Stok;
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
class StokServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StokService stokService;

    @Test
    void createStok_shouldInsertAndReturnSuccessMessage_whenRowsPositive() {
        Stok stok = new Stok.StokBuilder()
                .setId(0)
                .setMiktar(10)
                .setYemekId(1)
                .setIcecekId(2)
                .setTatliId(3)
                .setTedarikciId(4)
                .build();

        when(jdbcTemplate.update(
                eq("INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?, ?, ?, ?, ?)"),
                eq(10), eq(1), eq(2), eq(3), eq(4)
        )).thenReturn(1);

        String msg = stokService.createStok(stok);

        assertEquals("Stok başarıyla eklendi.", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?, ?, ?, ?, ?)"),
                eq(10), eq(1), eq(2), eq(3), eq(4)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getAllStoklar_shouldQueryTable() {
        when(jdbcTemplate.query(
                eq("SELECT * FROM stok"),
                ArgumentMatchers.<RowMapper<Stok>>any()
        )).thenReturn(List.of());

        List<Stok> list = stokService.getAllStoklar();

        assertNotNull(list);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM stok"),
                ArgumentMatchers.<RowMapper<Stok>>any()
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getStokById_shouldReturnFirstElement_whenFound() {
        int id = 5;
        Stok expected = new Stok.StokBuilder()
                .setId(id)
                .setMiktar(20)
                .setYemekId(1)
                .setIcecekId(2)
                .setTatliId(3)
                .setTedarikciId(4)
                .build();

        // DİKKAT: Service query(...) kullanıyor.
        when(jdbcTemplate.query(
                eq("SELECT * FROM stok WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Stok>>any(),
                eq(id)
        )).thenReturn(List.of(expected));

        Stok result = stokService.getStokById(id);

        assertSame(expected, result);
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM stok WHERE id = ?"),
                ArgumentMatchers.<RowMapper<Stok>>any(),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateStok_shouldExecuteUpdateAndReturnSuccess_whenRowsPositive() {
        int id = 7;
        Stok stok = new Stok.StokBuilder()
                .setId(id)
                .setMiktar(30)
                .setYemekId(11)
                .setIcecekId(12)
                .setTatliId(13)
                .setTedarikciId(14)
                .build();

        when(jdbcTemplate.update(
                eq("UPDATE stok SET miktar = ?, yemek_id = ?, icecek_id = ?, tatli_id = ?, tedarikci_id = ? WHERE id = ?"),
                eq(30), eq(11), eq(12), eq(13), eq(14), eq(id)
        )).thenReturn(1);

        String msg = stokService.updateStok(id, stok);

        assertEquals("Stok başarıyla güncellendi.", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE stok SET miktar = ?, yemek_id = ?, icecek_id = ?, tatli_id = ?, tedarikci_id = ? WHERE id = ?"),
                eq(30), eq(11), eq(12), eq(13), eq(14), eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteStok_shouldExecuteDeleteAndReturnSuccess_whenRowsPositive() {
        int id = 4;

        when(jdbcTemplate.update(
                eq("DELETE FROM stok WHERE id = ?"),
                eq(id)
        )).thenReturn(1);

        String msg = stokService.deleteStok(id);

        assertEquals("Stok başarıyla silindi!", msg);
        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM stok WHERE id = ?"),
                eq(id)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
