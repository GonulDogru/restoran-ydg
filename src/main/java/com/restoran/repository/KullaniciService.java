package com.restoran.repository;

import com.restoran.model.Kullanici; // Eski Users sınıfı artık Kullanici oldu
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KullaniciService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public KullaniciService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanından gelen satırı Kullanici nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Kullanici> kullaniciRowMapper = (rs, rowNum) -> {
        return new Kullanici.KullaniciBuilder()
                .setId(rs.getInt("id"))
                .setUsername(rs.getString("username"))
                .setPassword(rs.getString("password"))
                .setAdres(rs.getString("adres"))
                .setTelefon(rs.getString("telefon"))
                .build();
    };

    public String createKullanici(Kullanici kullanici) {
        // Not: Veritabanı tablosunun adı kodunuzda 'users' olarak görünüyor.
        // Eğer tablo adını da değiştirdiyseniz aşağıyı 'INSERT INTO kullanici...' yapmalısınız.
        String sql = "INSERT INTO kullanici (username, password, adres, telefon) VALUES (?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                kullanici.getUsername(),
                kullanici.getPassword(),
                kullanici.getAdres(),
                kullanici.getTelefon());

        return (rows > 0) ? "Kullanıcı başarıyla eklendi." : "Kullanıcı eklenemedi.";
    }

    public List<Kullanici> getAllKullanicilar() {
        String sql = "SELECT * FROM kullanici";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, kullaniciRowMapper);
    }

    public Kullanici getKullaniciById(int id) {
        String sql = "SELECT * FROM kullanici WHERE id = ?";
        List<Kullanici> results = jdbcTemplate.query(sql, kullaniciRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateKullanici(int id, Kullanici kullanici) {
        String sql = "UPDATE kullanici SET username = ?, password = ?, adres = ?, telefon = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                kullanici.getUsername(),
                kullanici.getPassword(),
                kullanici.getAdres(),
                kullanici.getTelefon(),
                id);

        return (rows > 0) ? "Kullanıcı başarıyla güncellendi." : "Kullanıcı bulunamadı!";
    }

    public String deleteKullanici(int id) {
        String sql = "DELETE FROM kullanici WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Kullanıcı başarıyla silindi!" : "Kullanıcı bulunamadı!";
    }
}