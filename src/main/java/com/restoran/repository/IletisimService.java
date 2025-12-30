package com.restoran.repository;

import com.restoran.model.Iletisim;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IletisimService {

    // 1. Constructor Injection (Modern Spring Yaklaşımı)
    private final JdbcTemplate jdbcTemplate;

    public IletisimService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Builder ile nesneye çevirir.
    // Nesne immutable olduğu için setter yerine Builder kullanıyoruz.
    private final RowMapper<Iletisim> iletisimRowMapper = (rs, rowNum) -> {
        return new Iletisim.IletisimBuilder()
                .setId(rs.getInt("id"))
                .setUserId(rs.getInt("user_id"))
                .setRestaurantId(rs.getInt("restaurant_id"))
                .setMesaj(rs.getString("mesaj"))
                .build();
    };

    public String createIletisim(Iletisim iletisim) {
        String sql = "INSERT INTO iletisim (user_id, restaurant_id, mesaj) VALUES (?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                iletisim.getUserId(),
                iletisim.getRestaurantId(),
                iletisim.getMesaj());

        return (rows > 0) ? "İletişim mesajı başarıyla eklendi." : "İletişim mesajı eklenemedi.";
    }

    public List<Iletisim> getAllIletisimler() {
        String sql = "SELECT * FROM iletisim";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, iletisimRowMapper);
    }

    public Iletisim getIletisimById(int id) {
        String sql = "SELECT * FROM iletisim WHERE id = ?";
        List<Iletisim> results = jdbcTemplate.query(sql, iletisimRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateIletisim(int id, Iletisim iletisim) {
        String sql = "UPDATE iletisim SET user_id = ?, restaurant_id = ?, mesaj = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                iletisim.getUserId(),
                iletisim.getRestaurantId(),
                iletisim.getMesaj(),
                id);

        return (rows > 0) ? "İletişim mesajı başarıyla güncellendi." : "İletişim mesajı bulunamadı!";
    }

    public String deleteIletisim(int id) {
        String sql = "DELETE FROM iletisim WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "İletişim mesajı başarıyla silindi!" : "İletişim mesajı bulunamadı!";
    }
}