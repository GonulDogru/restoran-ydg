package com.restoran.repository;

import com.restoran.model.Calisan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalisanService {

    // 1. Constructor Injection (Önerilen Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public CalisanService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Calisan nesnesine dönüştürür.
    // Calisan sınıfı immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Calisan> calisanRowMapper = (rs, rowNum) -> {
        return new Calisan.CalisanBuilder()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setRestaurantId(rs.getInt("restaurant_id"))
                .build();
    };

    public String createCalisan(Calisan calisan) {
        String sql = "INSERT INTO calisan (name, restaurant_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                calisan.getName(),
                calisan.getRestaurantId());

        return (rows > 0) ? "Çalışan başarıyla eklendi." : "Çalışan eklenemedi.";
    }

    public List<Calisan> getAllCalisanlar() {
        String sql = "SELECT * FROM calisan";
        // Tekrar kod yazmak yerine yukarıdaki mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, calisanRowMapper);
    }

    public Calisan getCalisanById(int id) {
        String sql = "SELECT * FROM calisan WHERE id = ?";
        List<Calisan> results = jdbcTemplate.query(sql, calisanRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateCalisan(int id, Calisan calisan) {
        String sql = "UPDATE calisan SET name = ?, restaurant_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                calisan.getName(),
                calisan.getRestaurantId(),
                id); // WHERE koşulu için ID

        return (rows > 0) ? "Çalışan başarıyla güncellendi." : "Çalışan bulunamadı!";
    }

    public String deleteCalisan(int id) {
        String sql = "DELETE FROM calisan WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Çalışan başarıyla silindi!" : "Çalışan bulunamadı!";
    }
}