package com.restoran.repository;

import com.restoran.model.Superadmin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperadminService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public SuperadminService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Superadmin nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Superadmin> superadminRowMapper = (rs, rowNum) -> {
        return new Superadmin.SuperadminBuilder()
                .setId(rs.getInt("id"))
                .setUsername(rs.getString("username"))
                .setPassword(rs.getString("password"))
                .build();
    };

    public String createSuperadmin(Superadmin superadmin) {
        String sql = "INSERT INTO superadmin (username, password) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                superadmin.getUsername(),
                superadmin.getPassword());

        return (rows > 0) ? "Superadmin başarıyla eklendi." : "Superadmin eklenemedi.";
    }

    public List<Superadmin> getAllSuperadminler() {
        String sql = "SELECT * FROM superadmin";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, superadminRowMapper);
    }

    public Superadmin getSuperadminById(int id) {
        String sql = "SELECT * FROM superadmin WHERE id = ?";
        List<Superadmin> results = jdbcTemplate.query(sql, superadminRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateSuperadmin(int id, Superadmin superadmin) {
        String sql = "UPDATE superadmin SET username = ?, password = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                superadmin.getUsername(),
                superadmin.getPassword(),
                id);

        return (rows > 0) ? "Superadmin başarıyla güncellendi." : "Superadmin bulunamadı!";
    }

    public String deleteSuperadmin(int id) {
        String sql = "DELETE FROM superadmin WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Superadmin başarıyla silindi!" : "Superadmin bulunamadı!";
    }
}