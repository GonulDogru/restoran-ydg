package com.restoran.repository;

import com.restoran.model.Menu;
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
class MenuServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MenuService menuService;

    @Test
    void getAllMenuler_shouldQueryAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM menu"), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Menu> result = menuService.getAllMenuler();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM menu"), any(RowMapper.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void createMenu_shouldInsert() {
        Menu menu = new Menu.MenuBuilder()
                .setId(0)
                .setName("Menu-1")
                .setRestaurantId(10)
                .build();

        menuService.createMenu(menu);

        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO menu (name, restaurant_id) VALUES (?, ?)"),
                eq("Menu-1"),
                eq(10)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void getMenuById_shouldQueryById() {
        Menu expected = new Menu.MenuBuilder()
                .setId(5)
                .setName("Menu-X")
                .setRestaurantId(2)
                .build();

        when(jdbcTemplate.queryForObject(eq("SELECT * FROM menu WHERE id = ?"), any(RowMapper.class), eq(5)))
                .thenReturn(expected);

        Menu result = menuService.getMenuById(5);

        assertEquals(expected, result);
        verify(jdbcTemplate, times(1)).queryForObject(eq("SELECT * FROM menu WHERE id = ?"), any(RowMapper.class), eq(5));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void updateMenu_shouldUpdateById() {
        Menu menu = new Menu.MenuBuilder()
                .setId(999) // service id paramını kullanır
                .setName("Menu-Updated")
                .setRestaurantId(77)
                .build();

        menuService.updateMenu(12, menu);

        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE menu SET name = ?, restaurant_id = ? WHERE id = ?"),
                eq("Menu-Updated"),
                eq(77),
                eq(12)
        );
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void deleteMenu_shouldDeleteById() {
        menuService.deleteMenu(9);

        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM menu WHERE id = ?"), eq(9));
        verifyNoMoreInteractions(jdbcTemplate);
    }
}
