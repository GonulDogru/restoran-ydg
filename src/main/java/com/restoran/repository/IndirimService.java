package com.restoran.repository;

import com.restoran.model.Indirim;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndirimService {

    // 1. Constructor Injection (Güvenli ve Test Edilebilir Yapı)
    private final JdbcTemplate jdbcTemplate;

    public IndirimService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Indirim nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Indirim> indirimRowMapper = (rs, rowNum) -> {
        return new Indirim.IndirimBuilder()
                .setId(rs.getInt("id"))
                // Not: Modelinizde amount tipi BigDecimal ise getBigDecimal,
                // float ise getFloat kullanmalısınız. Mevcut kodunuza sadık kalarak BigDecimal kullandım.
                .setAmount(rs.getBigDecimal("amount"))
                .setKullaniciId(rs.getInt("kullanici_id"))
                .build();
    };

    public String createIndirim(Indirim indirim) {
        String sql = "INSERT INTO indirim (amount, kullanici_id) VALUES (?, ?)";

        int rows = jdbcTemplate.update(sql,
                indirim.getAmount(),
                indirim.getKullaniciId());

        return (rows > 0) ? "İndirim başarıyla eklendi." : "İndirim eklenemedi.";
    }

    public List<Indirim> getAllIndirimler() {
        String sql = "SELECT * FROM indirim";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, indirimRowMapper);
    }

    public Indirim getIndirimById(int id) {
        String sql = "SELECT * FROM indirim WHERE id = ?";
        List<Indirim> results = jdbcTemplate.query(sql, indirimRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateIndirim(int id, Indirim indirim) {
        String sql = "UPDATE indirim SET amount = ?, kullanici_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                indirim.getAmount(),
                indirim.getKullaniciId(),
                id);

        return (rows > 0) ? "İndirim başarıyla güncellendi." : "İndirim bulunamadı!";
    }

    public String deleteIndirim(int id) {
        String sql = "DELETE FROM indirim WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "İndirim başarıyla silindi!" : "İndirim bulunamadı!";
    }
}