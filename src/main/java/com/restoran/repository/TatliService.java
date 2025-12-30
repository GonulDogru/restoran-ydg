package com.restoran.repository;

import com.restoran.model.Tatli;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TatliService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public TatliService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Tatli nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Tatli> tatliRowMapper = (rs, rowNum) -> {
        return new Tatli.TatliBuilder()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setMenuId(rs.getInt("menu_id"))
                .build();
    };

    public String createTatli(Tatli tatli) {
        String sql = "INSERT INTO tatli (name, menu_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                tatli.getName(),
                tatli.getMenuId());

        return (rows > 0) ? "Tatlı başarıyla eklendi." : "Tatlı eklenemedi.";
    }

    public List<Tatli> getAllTatlilar() {
        String sql = "SELECT * FROM tatli";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, tatliRowMapper);
    }

    public Tatli getTatliById(int id) {
        String sql = "SELECT * FROM tatli WHERE id = ?";
        List<Tatli> results = jdbcTemplate.query(sql, tatliRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateTatli(int id, Tatli tatli) {
        String sql = "UPDATE tatli SET name = ?, menu_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                tatli.getName(),
                tatli.getMenuId(),
                id);

        return (rows > 0) ? "Tatlı başarıyla güncellendi." : "Tatlı bulunamadı!";
    }

    public String deleteTatli(int id) {
        String sql = "DELETE FROM tatli WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Tatlı başarıyla silindi!" : "Tatlı bulunamadı!";
    }
}