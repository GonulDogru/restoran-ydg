package com.restoran.repository;

import com.restoran.model.Odeme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OdemeService {

    private final JdbcTemplate jdbcTemplate;
    // Veritabanı ve Java formatı için standart tarih formatlayıcı
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public OdemeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- YARDIMCI TARİH METOTLARI ---

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return new Date(); // Boşsa bugünü ver
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Geçersiz tarih formatı (Beklenen: yyyy-MM-dd): " + dateStr);
        }
    }

    private String formatDate(Date date) {
        if (date == null) return null;
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    // --- ROW MAPPER ---

    private final RowMapper<Odeme> odemeRowMapper = (rs, rowNum) ->
            new Odeme.OdemeBuilder()
                    .setId(rs.getInt("id"))
                    .setMasaId(rs.getInt("masa_id"))
                    .setTutar(rs.getDouble("tutar"))
                    .setOdemeYontemi(rs.getString("odeme_yontemi"))
                    .build();

    // --- CRUD METOTLARI ---

    public String createOdeme(Odeme odeme) {
        String sql = "INSERT INTO odeme (masa_id, tutar, odeme_yontemi, tarih) VALUES (?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                odeme.getMasaId(),
                odeme.getTutar(),
                odeme.getOdemeYontemi());

        return (rows > 0) ? "Ödeme başarıyla eklendi." : "Ödeme eklenemedi.";
    }

    public List<Odeme> getAllOdemeler() {
        String sql = "SELECT * FROM odeme";
        return jdbcTemplate.query(sql, odemeRowMapper);
    }

    public Odeme getOdemeById(int id) {
        String sql = "SELECT * FROM odeme WHERE id = ?";
        List<Odeme> results = jdbcTemplate.query(sql, odemeRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public String updateOdeme(int id, Odeme odeme) {
        String sql = "UPDATE odeme SET masa_id = ?, tutar = ?, odeme_yontemi = ?, tarih = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                odeme.getMasaId(),
                odeme.getTutar(),
                odeme.getOdemeYontemi(),
                id);

        return (rows > 0) ? "Ödeme başarıyla güncellendi." : "Ödeme bulunamadı!";
    }

    public String deleteOdeme(int id) {
        String sql = "DELETE FROM odeme WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Ödeme başarıyla silindi!" : "Ödeme bulunamadı!";
    }
}