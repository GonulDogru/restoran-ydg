package com.restoran.repository;

import com.restoran.model.Icecek;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IcecekService {

    // 1. Constructor Injection (Güvenli ve Test Edilebilir Yapı)
    private final JdbcTemplate jdbcTemplate;

    public IcecekService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Icecek nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Icecek> icecekRowMapper = (rs, rowNum) -> {
        return new Icecek.IcecekBuilder()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setMenuId(rs.getInt("menu_id"))
                .build();
    };

    public String createIcecek(Icecek icecek) {
        String sql = "INSERT INTO icecek (name, menu_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                icecek.getName(),
                icecek.getMenuId());

        return (rows > 0) ? "İçecek başarıyla eklendi." : "İçecek eklenemedi.";
    }

    public List<Icecek> getAllIcecekler() {
        String sql = "SELECT * FROM icecek";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, icecekRowMapper);
    }

    public Icecek getIcecekById(int id) {
        String sql = "SELECT * FROM icecek WHERE id = ?";
        List<Icecek> results = jdbcTemplate.query(sql, icecekRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateIcecek(int id, Icecek icecek) {
        String sql = "UPDATE icecek SET name = ?, menu_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                icecek.getName(),
                icecek.getMenuId(),
                id);

        return (rows > 0) ? "İçecek başarıyla güncellendi." : "İçecek bulunamadı!";
    }

    public String deleteIcecek(int id) {
        String sql = "DELETE FROM icecek WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "İçecek başarıyla silindi!" : "İçecek bulunamadı!";
    }
}