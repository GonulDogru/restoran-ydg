package com.restoran.repository;

import com.restoran.model.Tedarikci;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TedarikciService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public TedarikciService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Tedarikci nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Tedarikci> tedarikciRowMapper = (rs, rowNum) -> {
        return new Tedarikci.TedarikciBuilder()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setPhone(rs.getString("phone"))
                .setRestaurantId(rs.getInt("restaurant_id"))
                .build();
    };

    public String createTedarikci(Tedarikci tedarikci) {
        String sql = "INSERT INTO tedarikci (name, phone, restaurant_id) VALUES (?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                tedarikci.getName(),
                tedarikci.getPhone(),
                tedarikci.getRestaurantId());

        return (rows > 0) ? "Tedarikçi başarıyla eklendi." : "Tedarikçi eklenemedi.";
    }

    public List<Tedarikci> getAllTedarikciler() {
        String sql = "SELECT * FROM tedarikci";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, tedarikciRowMapper);
    }

    public Tedarikci getTedarikciById(int id) {
        String sql = "SELECT * FROM tedarikci WHERE id = ?";
        List<Tedarikci> results = jdbcTemplate.query(sql, tedarikciRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateTedarikci(int id, Tedarikci tedarikci) {
        String sql = "UPDATE tedarikci SET name = ?, phone = ?, restaurant_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                tedarikci.getName(),
                tedarikci.getPhone(),
                tedarikci.getRestaurantId(),
                id);

        return (rows > 0) ? "Tedarikçi başarıyla güncellendi." : "Tedarikçi bulunamadı!";
    }

    public String deleteTedarikci(int id) {
        String sql = "DELETE FROM tedarikci WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Tedarikçi başarıyla silindi!" : "Tedarikçi bulunamadı!";
    }
}