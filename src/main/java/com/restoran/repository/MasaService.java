package com.restoran.repository;

import com.restoran.model.Masa;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MasaService {

    private final JdbcTemplate jdbcTemplate;

    public MasaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Masa> masaRowMapper = (rs, rowNum) -> {
        Masa masa = new Masa();
        masa.setId(rs.getInt("id"));
        masa.setNumara(rs.getInt("numara"));
        masa.setKapasite(rs.getInt("kapasite"));
        masa.setRestaurantId(rs.getInt("restaurant_id"));
        return masa;
    };

    public List<Masa> findAll() {
        return jdbcTemplate.query("SELECT * FROM masa", masaRowMapper);
    }

    public Masa findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM masa WHERE id = ?", masaRowMapper, id);
    }

    public void save(Masa masa) {
        jdbcTemplate.update("INSERT INTO masa (numara, kapasite, restaurant_id) VALUES (?, ?, ?)",
                masa.getNumara(), masa.getKapasite(), masa.getRestaurantId());
    }

    public void update(int id, Masa masa) {
        jdbcTemplate.update("UPDATE masa SET numara = ?, kapasite = ?, restaurant_id = ? WHERE id = ?",
                masa.getNumara(), masa.getKapasite(), masa.getRestaurantId(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM masa WHERE id = ?", id);
    }
}