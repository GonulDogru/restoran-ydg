package com.restoran.repository;

import com.restoran.model.Superadmin;
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
class SuperadminServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SuperadminService superadminService;

    @Test
    void createSuperadmin_whenRowsPositive_shouldReturnSuccessMessage() {
        Superadmin s = new Superadmin.SuperadminBuilder()
                .setId(0)
                .setUsername("u1")
                .setPassword("p1")
                .build();

        String sql = "INSERT INTO superadmin (username, password) VALUES (?, ?)";
        when(jdbcTemplate.update(eq(sql), eq("u1"), eq("p1"))).thenReturn(1);

        String result = superadminService.createSuperadmin(s);

        assertEquals("Superadmin başarıyla eklendi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("u1"), eq("p1"));
    }

    @Test
    void createSuperadmin_whenRowsZero_shouldReturnFailureMessage() {
        Superadmin s = new Superadmin.SuperadminBuilder()
                .setId(0)
                .setUsername("u1")
                .setPassword("p1")
                .build();

        String sql = "INSERT INTO superadmin (username, password) VALUES (?, ?)";
        when(jdbcTemplate.update(eq(sql), eq("u1"), eq("p1"))).thenReturn(0);

        String result = superadminService.createSuperadmin(s);

        assertEquals("Superadmin eklenemedi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("u1"), eq("p1"));
    }

    @Test
    void getAllSuperadminler_shouldQueryTable() {
        when(jdbcTemplate.query(eq("SELECT * FROM superadmin"), ArgumentMatchers.<RowMapper<Superadmin>>any()))
                .thenReturn(List.of());

        List<Superadmin> result = superadminService.getAllSuperadminler();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM superadmin"), ArgumentMatchers.<RowMapper<Superadmin>>any());
    }

    @Test
    void getSuperadminById_whenEmpty_shouldReturnNull() {
        int id = 99;
        String sql = "SELECT * FROM superadmin WHERE id = ?";

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Superadmin>>any(), eq(id)))
                .thenReturn(List.of());

        Superadmin result = superadminService.getSuperadminById(id);

        assertNull(result);
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Superadmin>>any(), eq(id));
    }

    @Test
    void getSuperadminById_whenFound_shouldReturnFirst() {
        int id = 1;
        String sql = "SELECT * FROM superadmin WHERE id = ?";

        Superadmin found = new Superadmin.SuperadminBuilder()
                .setId(id)
                .setUsername("admin")
                .setPassword("pass")
                .build();

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Superadmin>>any(), eq(id)))
                .thenReturn(List.of(found));

        Superadmin result = superadminService.getSuperadminById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("admin", result.getUsername());
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Superadmin>>any(), eq(id));
    }

    @Test
    void updateSuperadmin_whenRowsPositive_shouldReturnSuccessMessage() {
        int id = 7;
        Superadmin s = new Superadmin.SuperadminBuilder()
                .setId(id)
                .setUsername("u2")
                .setPassword("p2")
                .build();

        String sql = "UPDATE superadmin SET username = ?, password = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq("u2"), eq("p2"), eq(id))).thenReturn(1);

        String result = superadminService.updateSuperadmin(id, s);

        assertEquals("Superadmin başarıyla güncellendi.", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("u2"), eq("p2"), eq(id));
    }

    @Test
    void updateSuperadmin_whenRowsZero_shouldReturnNotFoundMessage() {
        int id = 7;
        Superadmin s = new Superadmin.SuperadminBuilder()
                .setId(id)
                .setUsername("u2")
                .setPassword("p2")
                .build();

        String sql = "UPDATE superadmin SET username = ?, password = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq("u2"), eq("p2"), eq(id))).thenReturn(0);

        String result = superadminService.updateSuperadmin(id, s);

        assertEquals("Superadmin bulunamadı!", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("u2"), eq("p2"), eq(id));
    }

    @Test
    void deleteSuperadmin_whenRowsPositive_shouldReturnSuccessMessage() {
        int id = 3;
        String sql = "DELETE FROM superadmin WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(1);

        String result = superadminService.deleteSuperadmin(id);

        assertEquals("Superadmin başarıyla silindi!", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq(id));
    }

    @Test
    void deleteSuperadmin_whenRowsZero_shouldReturnNotFoundMessage() {
        int id = 3;
        String sql = "DELETE FROM superadmin WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(0);

        String result = superadminService.deleteSuperadmin(id);

        assertEquals("Superadmin bulunamadı!", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq(id));
    }
}
