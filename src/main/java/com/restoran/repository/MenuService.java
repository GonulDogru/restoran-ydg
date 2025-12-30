package com.restoran.repository;

import com.restoran.model.Menu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {

    private final JdbcTemplate jdbcTemplate;

    public MenuService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Menu> menuRowMapper = (rs, rowNum) -> new Menu.MenuBuilder()
            .setId(rs.getInt("id"))
            .setName(rs.getString("name"))
            .setRestaurantId(rs.getInt("restaurant_id"))
            .build();

    public List<Menu> getAllMenuler() {
        return jdbcTemplate.query("SELECT * FROM menu", menuRowMapper);
    }

    public void createMenu(Menu menu) {
        jdbcTemplate.update("INSERT INTO menu (name, restaurant_id) VALUES (?, ?)",
                menu.getName(), menu.getRestaurantId());
    }

    public Menu getMenuById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM menu WHERE id = ?", menuRowMapper, id);
    }

    public void updateMenu(int id, Menu menu) {
        jdbcTemplate.update("UPDATE menu SET name = ?, restaurant_id = ? WHERE id = ?",
                menu.getName(), menu.getRestaurantId(), id);
    }

    public void deleteMenu(int id) {
        jdbcTemplate.update("DELETE FROM menu WHERE id = ?", id);
    }
}