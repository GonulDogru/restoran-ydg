package com.restoran.repository;

import com.restoran.model.Teslimat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeslimatService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public TeslimatService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Teslimat nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Teslimat> teslimatRowMapper = (rs, rowNum) -> {
        return new Teslimat.TeslimatBuilder()
                .setId(rs.getInt("id"))
                .setAddress(rs.getString("address"))
                .setDurum(rs.getString("durum"))
                .setRestaurantId(rs.getInt("restaurant_id"))
                .setSiparisId(rs.getInt("siparis_id"))
                .build();
    };

    public String createTeslimat(Teslimat teslimat) {
        String sql = "INSERT INTO teslimat (address, durum, restaurant_id, siparis_id) VALUES (?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                teslimat.getAddress(),
                teslimat.getDurum(),
                teslimat.getRestaurantId(),
                teslimat.getSiparisId());

        return (rows > 0) ? "Teslimat başarıyla eklendi." : "Teslimat eklenemedi.";
    }

    public List<Teslimat> getAllTeslimatlar() {
        String sql = "SELECT * FROM teslimat";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, teslimatRowMapper);
    }

    public Teslimat getTeslimatById(int id) {
        String sql = "SELECT * FROM teslimat WHERE id = ?";
        List<Teslimat> results = jdbcTemplate.query(sql, teslimatRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateTeslimat(int id, Teslimat teslimat) {
        String sql = "UPDATE teslimat SET address = ?, durum = ?, restaurant_id = ?, siparis_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                teslimat.getAddress(),
                teslimat.getDurum(),
                teslimat.getRestaurantId(),
                teslimat.getSiparisId(),
                id);

        return (rows > 0) ? "Teslimat başarıyla güncellendi." : "Teslimat bulunamadı!";
    }

    public String deleteTeslimat(int id) {
        String sql = "DELETE FROM teslimat WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Teslimat başarıyla silindi!" : "Teslimat bulunamadı!";
    }
}