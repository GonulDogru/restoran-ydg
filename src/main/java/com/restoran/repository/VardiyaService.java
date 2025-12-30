package com.restoran.repository;

import com.restoran.model.Vardiya;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VardiyaService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public VardiyaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Vardiya nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Vardiya> vardiyaRowMapper = (rs, rowNum) -> {
        return new Vardiya.VardiyaBuilder()
                .setId(rs.getInt("id"))
                .setSaatler(rs.getString("saatler"))
                .setCalisanId(rs.getInt("calisan_id"))
                .build();
    };

    public String createVardiya(Vardiya vardiya) {
        String sql = "INSERT INTO vardiya (saatler, calisan_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                vardiya.getSaatler(),
                vardiya.getCalisanId());

        return (rows > 0) ? "Vardiya başarıyla eklendi." : "Vardiya eklenemedi.";
    }

    public List<Vardiya> getAllVardiyalar() {
        String sql = "SELECT * FROM vardiya";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, vardiyaRowMapper);
    }

    public Vardiya getVardiyaById(int id) {
        String sql = "SELECT * FROM vardiya WHERE id = ?";
        List<Vardiya> results = jdbcTemplate.query(sql, vardiyaRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateVardiya(int id, Vardiya vardiya) {
        String sql = "UPDATE vardiya SET saatler = ?, calisan_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                vardiya.getSaatler(),
                vardiya.getCalisanId(),
                id);

        return (rows > 0) ? "Vardiya başarıyla güncellendi." : "Vardiya bulunamadı!";
    }

    public String deleteVardiya(int id) {
        String sql = "DELETE FROM vardiya WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Vardiya başarıyla silindi!" : "Vardiya bulunamadı!";
    }
}