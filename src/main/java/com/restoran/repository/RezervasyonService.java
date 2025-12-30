package com.restoran.repository;

import com.restoran.model.Rezervasyon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RezervasyonService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public RezervasyonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- YARDIMCI METOTLAR (Thread-Safe Tarih/Saat İşlemleri) ---
    // SimpleDateFormat static olmamalıdır. Her işlemde yenisi oluşturulur.

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Geçersiz tarih formatı: " + dateStr, e);
        }
    }

    private Time parseTime(String timeStr) {
        try {
            // "HH:mm" formatını parse edip SQL Time objesine çeviriyoruz
            Date date = new SimpleDateFormat("HH:mm").parse(timeStr);
            return new Time(date.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Geçersiz saat formatı (Örn: 19:30 olmalı): " + timeStr, e);
        }
    }

    private String formatDate(java.sql.Date date) {
        if (date == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    // Veritabanındaki Time objesini String'e (HH:mm) çevirir
    private String formatTime(Time time) {
        if (time == null) return null;
        // time.toString() genellikle HH:mm:ss döner, biz HH:mm istiyorsak:
        return new SimpleDateFormat("HH:mm").format(time);
    }

    // 2. RowMapper: Veritabanı satırını Rezervasyon nesnesine dönüştürür.
    // Immutable Builder kullanıyoruz.
    private final RowMapper<Rezervasyon> rezervasyonRowMapper = (rs, rowNum) -> {
        return new Rezervasyon.RezervasyonBuilder()
                .setId(rs.getInt("id"))
                .setTarih(formatDate(rs.getDate("tarih"))) // Date -> String
                .setSaat(formatTime(rs.getTime("saat")))   // Time -> String
                .setUserId(rs.getInt("user_id"))
                .setMasaId(rs.getInt("masa_id"))
                .build();
    };

    public String createRezervasyon(Rezervasyon rezervasyon) {
        String sql = "INSERT INTO rezervasyon (tarih, saat, user_id, masa_id) VALUES (?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                parseDate(rezervasyon.getTarih()), // String -> SQL Date
                parseTime(rezervasyon.getSaat()),  // String -> SQL Time
                rezervasyon.getUserId(),
                rezervasyon.getMasaId());

        return (rows > 0) ? "Rezervasyon başarıyla eklendi." : "Rezervasyon eklenemedi.";
    }

    public List<Rezervasyon> getAllRezervasyonlar() {
        String sql = "SELECT * FROM rezervasyon";
        return jdbcTemplate.query(sql, rezervasyonRowMapper);
    }

    public Rezervasyon getRezervasyonById(int id) {
        String sql = "SELECT * FROM rezervasyon WHERE id = ?";
        List<Rezervasyon> results = jdbcTemplate.query(sql, rezervasyonRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateRezervasyon(int id, Rezervasyon rezervasyon) {
        String sql = "UPDATE rezervasyon SET tarih = ?, saat = ?, user_id = ?, masa_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                parseDate(rezervasyon.getTarih()),
                parseTime(rezervasyon.getSaat()), // Time.valueOf yerine kendi metodumuzu kullandık
                rezervasyon.getUserId(),
                rezervasyon.getMasaId(),
                id);

        return (rows > 0) ? "Rezervasyon başarıyla güncellendi." : "Rezervasyon bulunamadı!";
    }

    public String deleteRezervasyon(int id) {
        String sql = "DELETE FROM rezervasyon WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Rezervasyon başarıyla silindi!" : "Rezervasyon bulunamadı!";
    }
}