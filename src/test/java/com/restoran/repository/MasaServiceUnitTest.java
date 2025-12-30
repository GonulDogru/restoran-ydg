package com.restoran.repository;

import com.restoran.model.Masa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasaServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MasaService masaService;

    @Test
    void save_shouldExecuteInsertWithExpectedSqlAndParams() {
        Masa masa = new Masa();
        masa.setNumara(5);
        masa.setKapasite(2);
        masa.setRestaurantId(7);

        masaService.save(masa);

        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO masa (numara, kapasite, restaurant_id) VALUES (?, ?, ?)"),
                eq(5), eq(2), eq(7)
        );
    }

    @Test
    void findAll_shouldQueryTable() {
        when(jdbcTemplate.query(eq("SELECT * FROM masa"), ArgumentMatchers.<RowMapper<Masa>>any()))
                .thenReturn(java.util.List.of());

        masaService.findAll();

        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM masa"), ArgumentMatchers.<RowMapper<Masa>>any());
    }
}
