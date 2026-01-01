package com.restoran.repository;

import com.restoran.model.Restaurant;
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
class RestaurantServiceUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void createRestaurant_whenRowsPositive_shouldReturnBasarili() {
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setId(0)
                .setName("CafeX")
                .setAddress("Malatya")
                .build();

        String sql = "INSERT INTO restaurant (name, adres) VALUES (?, ?)";
        when(jdbcTemplate.update(eq(sql), eq("CafeX"), eq("Malatya"))).thenReturn(1);

        String result = restaurantService.createRestaurant(r);

        assertEquals("Başarılı", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("CafeX"), eq("Malatya"));
    }

    @Test
    void createRestaurant_whenRowsZero_shouldReturnHata() {
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setId(0)
                .setName("CafeX")
                .setAddress("Malatya")
                .build();

        String sql = "INSERT INTO restaurant (name, adres) VALUES (?, ?)";
        when(jdbcTemplate.update(eq(sql), eq("CafeX"), eq("Malatya"))).thenReturn(0);

        String result = restaurantService.createRestaurant(r);

        assertEquals("Hata", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("CafeX"), eq("Malatya"));
    }

    @Test
    void getAllRestaurants_shouldQueryTable() {
        when(jdbcTemplate.query(eq("SELECT * FROM restaurant"), ArgumentMatchers.<RowMapper<Restaurant>>any()))
                .thenReturn(List.of());

        List<Restaurant> result = restaurantService.getAllRestaurants();

        assertNotNull(result);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM restaurant"), ArgumentMatchers.<RowMapper<Restaurant>>any());
    }

    @Test
    void getRestaurantById_whenEmpty_shouldReturnNull() {
        int id = 99;
        String sql = "SELECT * FROM restaurant WHERE id = ?";

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Restaurant>>any(), eq(id)))
                .thenReturn(List.of());

        Restaurant result = restaurantService.getRestaurantById(id);

        assertNull(result);
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Restaurant>>any(), eq(id));
    }

    @Test
    void getRestaurantById_whenFound_shouldReturnFirst() {
        int id = 1;
        String sql = "SELECT * FROM restaurant WHERE id = ?";

        Restaurant found = new Restaurant.RestaurantBuilder()
                .setId(id)
                .setName("R1")
                .setAddress("A1")
                .build();

        when(jdbcTemplate.query(eq(sql), ArgumentMatchers.<RowMapper<Restaurant>>any(), eq(id)))
                .thenReturn(List.of(found));

        Restaurant result = restaurantService.getRestaurantById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("R1", result.getName());
        assertEquals("A1", result.getAddress());
        verify(jdbcTemplate, times(1)).query(eq(sql), ArgumentMatchers.<RowMapper<Restaurant>>any(), eq(id));
    }

    @Test
    void updateRestaurant_whenRowsPositive_shouldReturnGuncellendi() {
        int id = 7;
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setId(id)
                .setName("New")
                .setAddress("NewAdres")
                .build();

        String sql = "UPDATE restaurant SET name = ?, adres = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq("New"), eq("NewAdres"), eq(id))).thenReturn(1);

        String result = restaurantService.updateRestaurant(id, r);

        assertEquals("Güncellendi", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("New"), eq("NewAdres"), eq(id));
    }

    @Test
    void updateRestaurant_whenRowsZero_shouldReturnHata() {
        int id = 7;
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setId(id)
                .setName("New")
                .setAddress("NewAdres")
                .build();

        String sql = "UPDATE restaurant SET name = ?, adres = ? WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq("New"), eq("NewAdres"), eq(id))).thenReturn(0);

        String result = restaurantService.updateRestaurant(id, r);

        assertEquals("Hata", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq("New"), eq("NewAdres"), eq(id));
    }

    @Test
    void deleteRestaurant_whenRowsPositive_shouldReturnSilindi() {
        int id = 5;
        String sql = "DELETE FROM restaurant WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(1);

        String result = restaurantService.deleteRestaurant(id);

        assertEquals("Silindi", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq(id));
    }

    @Test
    void deleteRestaurant_whenRowsZero_shouldReturnHata() {
        int id = 5;
        String sql = "DELETE FROM restaurant WHERE id = ?";
        when(jdbcTemplate.update(eq(sql), eq(id))).thenReturn(0);

        String result = restaurantService.deleteRestaurant(id);

        assertEquals("Hata", result);
        verify(jdbcTemplate, times(1)).update(eq(sql), eq(id));
    }
}
