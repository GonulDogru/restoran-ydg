package com.restoran.repository;

import com.restoran.model.Garson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarsonService {

    // 1. Constructor Injection (Güvenli ve Test Edilebilir)
    private final JdbcTemplate jdbcTemplate;

    public GarsonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanından gelen satırı Garson nesnesine çevirir.
    // Garson sınıfı immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Garson> garsonRowMapper = (rs, rowNum) -> {
        return new Garson.GarsonBuilder()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setCalisanId(rs.getInt("calisan_id"))
                .build();
    };

    public String createGarson(Garson garson) {
        String sql = "INSERT INTO garson (name, calisan_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                garson.getName(),
                garson.getCalisanId());

        return (rows > 0) ? "Garson başarıyla eklendi." : "Garson eklenemedi.";
    }

    public List<Garson> getAllGarsonlar() {
        String sql = "SELECT * FROM garson";
        return jdbcTemplate.query(sql, garsonRowMapper);
    }

    public Garson getGarsonById(int id) {
        String sql = "SELECT * FROM garson WHERE id = ?";
        List<Garson> results = jdbcTemplate.query(sql, garsonRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateGarson(int id, Garson garson) {
        String sql = "UPDATE garson SET name = ?, calisan_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                garson.getName(),
                garson.getCalisanId(),
                id);

        return (rows > 0) ? "Garson başarıyla güncellendi." : "Garson bulunamadı!";
    }

    public String deleteGarson(int id) {
        String sql = "DELETE FROM garson WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Garson başarıyla silindi!" : "Garson bulunamadı!";
    }
}