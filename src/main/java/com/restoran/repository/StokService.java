package com.restoran.repository;

import com.restoran.model.Stok;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StokService {

    // 1. Constructor Injection (Güvenli ve Modern Yöntem)
    private final JdbcTemplate jdbcTemplate;

    public StokService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper: Veritabanı satırını Stok nesnesine dönüştürür.
    // Nesne immutable olduğu için Builder kullanıyoruz.
    private final RowMapper<Stok> stokRowMapper = (rs, rowNum) -> {
        return new Stok.StokBuilder()
                .setId(rs.getInt("id"))
                .setMiktar(rs.getInt("miktar"))
                .setYemekId(rs.getInt("yemek_id"))
                .setIcecekId(rs.getInt("icecek_id"))
                .setTatliId(rs.getInt("tatli_id"))
                .setTedarikciId(rs.getInt("tedarikci_id"))
                .build();
    };

    public String createStok(Stok stok) {
        String sql = "INSERT INTO stok (miktar, yemek_id, icecek_id, tatli_id, tedarikci_id) VALUES (?, ?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql,
                stok.getMiktar(),
                stok.getYemekId(),
                stok.getIcecekId(),
                stok.getTatliId(),
                stok.getTedarikciId());

        return (rows > 0) ? "Stok başarıyla eklendi." : "Stok eklenemedi.";
    }

    public List<Stok> getAllStoklar() {
        String sql = "SELECT * FROM stok";
        // Tanımladığımız mapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, stokRowMapper);
    }

    public Stok getStokById(int id) {
        String sql = "SELECT * FROM stok WHERE id = ?";
        List<Stok> results = jdbcTemplate.query(sql, stokRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateStok(int id, Stok stok) {
        String sql = "UPDATE stok SET miktar = ?, yemek_id = ?, icecek_id = ?, tatli_id = ?, tedarikci_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                stok.getMiktar(),
                stok.getYemekId(),
                stok.getIcecekId(),
                stok.getTatliId(),
                stok.getTedarikciId(),
                id);

        return (rows > 0) ? "Stok başarıyla güncellendi." : "Stok bulunamadı!";
    }

    public String deleteStok(int id) {
        String sql = "DELETE FROM stok WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Stok başarıyla silindi!" : "Stok bulunamadı!";
    }
}