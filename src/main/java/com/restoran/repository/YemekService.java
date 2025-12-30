package com.restoran.repository;

import com.restoran.model.Yemek;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class YemekService {

    private final JdbcTemplate jdbcTemplate;

    public YemekService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Yemek> yemekRowMapper = (rs, rowNum) -> new Yemek.YemekBuilder()
            .setId(rs.getInt("id"))
            .setName(rs.getString("name"))
            .setMenuId(rs.getInt("menu_id"))
            .build();

    public List<Yemek> getAllYemekler() {
        return jdbcTemplate.query("SELECT * FROM yemek", yemekRowMapper);
    }

    public void createYemek(Yemek yemek) {
        jdbcTemplate.update("INSERT INTO yemek (name, menu_id) VALUES (?, ?)",
                yemek.getName(), yemek.getMenuId());
    }

    public Yemek getYemekById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM yemek WHERE id = ?", yemekRowMapper, id);
    }

    public void updateYemek(int id, Yemek yemek) {
        jdbcTemplate.update("UPDATE yemek SET name = ?, menu_id = ? WHERE id = ?",
                yemek.getName(), yemek.getMenuId(), id);
    }

    public void deleteYemek(int id) {
        jdbcTemplate.update("DELETE FROM yemek WHERE id = ?", id);
    }
}