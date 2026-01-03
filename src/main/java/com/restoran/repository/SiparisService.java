package com.restoran.repository;

import com.restoran.model.Siparis;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SiparisService {

    private static final DateTimeFormatter STRICT_DATE =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public SiparisService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- YARDIMCI METOTLAR (Thread-Safe Tarih İşlemleri) ---
    // SimpleDateFormat static olmamalıdır. Her işlemde yeni instance oluşturuyoruz.

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException("Geçersiz tarih formatı: " + dateStr + " (yyyy-MM-dd olmalı)");
        }
        try {
            // Strict parse: yyyy-MM-dd (örn: 2025-12-30). 30-12-2025 veya 2025-02-30 gibi değerleri reddeder.
            LocalDate ld = LocalDate.parse(dateStr, STRICT_DATE);
            return java.sql.Date.valueOf(ld); // java.sql.Date extends java.util.Date
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Geçersiz tarih formatı: " + dateStr + " (yyyy-MM-dd olmalı)", e);
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
                .setTarih(formatDate(rs.getDate("tarih")))
                .setAmount(rs.getFloat("amount"))
                .setUserId(rs.getInt("user_id"))
                .setMasaId(rs.getInt("masa_id"))
                .build();
    };

    // 3. CRUD İşlemleri

    public String createSiparis(Siparis siparis) {
        String sql = "INSERT INTO siparis (tarih, amount, user_id, masa_id) VALUES (?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                parseDate(siparis.getTarih()), // String -> Java/SQL Date (strict)
                siparis.getAmount(),
                siparis.getUserId(),
                siparis.getMasaId());

        return (rows > 0) ? "Sipariş başarıyla eklendi." : "Sipariş eklenemedi.";
    }

    public List<Siparis> getAllSiparisler() {
        String sql = "SELECT * FROM siparis";
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
