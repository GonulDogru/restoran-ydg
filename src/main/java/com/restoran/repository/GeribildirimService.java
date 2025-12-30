package com.restoran.repository;

import com.restoran.model.Geribildirim;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeribildirimService {

    // 1. Constructor Injection (Modern Spring Yaklaşımı)
    private final JdbcTemplate jdbcTemplate;

    public GeribildirimService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Builder ile nesneye çevirir.
    // Kod tekrarını önlemek için tek bir yerde tanımlıyoruz.
    private final RowMapper<Geribildirim> geribildirimRowMapper = (rs, rowNum) -> {
        return new Geribildirim.GeribildirimBuilder()
                .setId(rs.getInt("id"))
                .setYorum(rs.getString("yorum"))
                .setUserId(rs.getInt("user_id"))
                .build();
    };

    public String createGeribildirim(Geribildirim geribildirim) {
        String sql = "INSERT INTO geribildirim (yorum, user_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                geribildirim.getYorum(),
                geribildirim.getUserId());

        return (rows > 0) ? "Geribildirim başarıyla eklendi." : "Geribildirim eklenemedi.";
    }

    public List<Geribildirim> getAllGeribildirimler() {
        String sql = "SELECT * FROM geribildirim";
        return jdbcTemplate.query(sql, geribildirimRowMapper);
    }

    public Geribildirim getGeribildirimById(int id) {
        String sql = "SELECT * FROM geribildirim WHERE id = ?";
        List<Geribildirim> results = jdbcTemplate.query(sql, geribildirimRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateGeribildirim(int id, Geribildirim geribildirim) {
        String sql = "UPDATE geribildirim SET yorum = ?, user_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                geribildirim.getYorum(),
                geribildirim.getUserId(),
                id);

        return (rows > 0) ? "Geribildirim başarıyla güncellendi." : "Geribildirim bulunamadı!";
    }

    public String deleteGeribildirim(int id) {
        String sql = "DELETE FROM geribildirim WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Geribildirim başarıyla silindi!" : "Geribildirim bulunamadı!";
    }
}