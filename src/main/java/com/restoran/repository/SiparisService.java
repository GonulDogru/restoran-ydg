package com.restoran.repository;

import com.restoran.model.Siparis;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SiparisService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public SiparisService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- YARDIMCI METOTLAR (Thread-Safe Tarih İşlemleri) ---
    // SimpleDateFormat static olmamalıdır. Her işlemde yeni instance oluşturuyoruz.

    private Date parseDate(String dateStr) {
        try {
            // String tarihi (örn: "2023-10-25") Java Date objesine çevirir
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Geçersiz tarih formatı: " + dateStr, e);
        }
    }

    private String formatDate(java.sql.Date date) {
        if (date == null) return null;
        // Veritabanından gelen Date objesini String'e çevirir
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    // 2. RowMapper: Veritabanı satırını Siparis nesnesine dönüştürür.
    // Immutable Builder kullanıyoruz.
    private final RowMapper<Siparis> siparisRowMapper = (rs, rowNum) -> {
        return new Siparis.SiparisBuilder()
                .setId(rs.getInt("id"))
                .setTarih(formatDate(rs.getDate("tarih"))) // SQL Date -> String
                .setAmount(rs.getFloat("amount"))
                .setUserId(rs.getInt("user_id"))
                .setMasaId(rs.getInt("masa_id"))
                .build();
    };

    public String createSiparis(Siparis siparis) {
        String sql = "INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                parseDate(siparis.getTarih()), // String -> Java/SQL Date
                siparis.getAmount(),
                siparis.getUserId(),
                siparis.getMasaId());

        return (rows > 0) ? "Sipariş başarıyla eklendi." : "Sipariş eklenemedi.";
    }

    public List<Siparis> getAllSiparisler() {
        String sql = "SELECT * FROM siparis";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, siparisRowMapper);
    }

    public Siparis getSiparisById(int id) {
        String sql = "SELECT * FROM siparis WHERE id = ?";
        List<Siparis> results = jdbcTemplate.query(sql, siparisRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateSiparis(int id, Siparis siparis) {
        String sql = "UPDATE siparis SET tarih = ?, amount = ?, user_id = ?, masa_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                parseDate(siparis.getTarih()),
                siparis.getAmount(),
                siparis.getUserId(),
                siparis.getMasaId(),
                id);

        return (rows > 0) ? "Sipariş başarıyla güncellendi." : "Sipariş bulunamadı!";
    }

    public String deleteSiparis(int id) {
        String sql = "DELETE FROM siparis WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Sipariş başarıyla silindi!" : "Sipariş bulunamadı!";
    }
}