package com.restoran.repository;

import com.restoran.model.Restaurant;
import com.restoran.model.Odeme; // Ödeme modelini import etmeyi unutmayın
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- ROW MAPPERS (Veritabanından Nesneye Dönüştürücüler) ---

    private final RowMapper<Restaurant> restaurantRowMapper = (rs, rowNum) ->
            new Restaurant.RestaurantBuilder()
                    .setId(rs.getInt("id"))
                    .setName(rs.getString("name"))
                    .setAddress(rs.getString("adres")) // 'address' yerine 'adres'
                    .build();

    private final RowMapper<Odeme> odemeRowMapper = (rs, rowNum) ->
            new Odeme.OdemeBuilder()
                    .setId(rs.getInt("id"))
                    .setMasaId(rs.getInt("masa_id"))
                    .setTutar(rs.getDouble("tutar"))
                    .setOdemeYontemi(rs.getString("odeme_yontemi"))
                    .build();

    // --- RESTAURANT METOTLARI ---

    public List<Restaurant> getAllRestaurants() {
        return jdbcTemplate.query("SELECT * FROM restaurant", restaurantRowMapper);
    }

    public String createRestaurant(Restaurant restaurant) {
        String sql = "INSERT INTO restaurant (name, adres) VALUES (?, ?)";
        int rows = jdbcTemplate.update(sql, restaurant.getName(), restaurant.getAddress());
        return (rows > 0) ? "Başarılı" : "Hata";
    }

    // --- ÖDEME (ODEME) METOTLARI (Facade'deki hataları çözen kısım) ---

    public List<Odeme> getAllOdemeler() {
        String sql = "SELECT * FROM odeme";
        return jdbcTemplate.query(sql, odemeRowMapper);
    }

    public Odeme getOdemeById(int id) {
        String sql = "SELECT * FROM odeme WHERE id = ?";
        List<Odeme> results = jdbcTemplate.query(sql, odemeRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public void updateOdeme(int id, Odeme odeme) {
        String sql = "UPDATE odeme SET masa_id = ?, tutar = ?, odeme_yontemi = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                odeme.getMasaId(),
                odeme.getTutar(),
                odeme.getOdemeYontemi(),
                id);
    }

    public void saveOdeme(Odeme odeme) {
        String sql = "INSERT INTO odeme (masa_id, tutar, odeme_yontemi) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                odeme.getMasaId(),
                odeme.getTutar(),
                odeme.getOdemeYontemi());
    }

    public void deleteOdeme(int id) {
        String sql = "DELETE FROM odeme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // --- DİĞER RESTAURANT METOTLARI ---

    public Restaurant getRestaurantById(int id) {
        String sql = "SELECT * FROM restaurant WHERE id = ?";
        List<Restaurant> results = jdbcTemplate.query(sql, restaurantRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public String updateRestaurant(int id, Restaurant restaurant) {
        String sql = "UPDATE restaurant SET name = ?, adres = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, restaurant.getName(), restaurant.getAddress(), id);
        return (rows > 0) ? "Güncellendi" : "Hata";
    }

    public String deleteRestaurant(int id) {
        String sql = "DELETE FROM restaurant WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Silindi" : "Hata";
    }
}