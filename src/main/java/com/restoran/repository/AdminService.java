package com.restoran.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.restoran.model.Admin;

@Service
public class AdminService {

    // 1. Constructor Injection (Best Practice)
    private final JdbcTemplate jdbcTemplate;

    public AdminService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2. RowMapper (Veritabanı satırını Admin nesnesine dönüştüren harita)
    // Kod tekrarını önlemek için bunu bir kez tanımlayıp her yerde kullanacağız.
    private final RowMapper<Admin> adminRowMapper = (rs, rowNum) -> {
        return new Admin.AdminBuilder()
                .setId(rs.getInt("id"))
                .setUsername(rs.getString("username"))
                .setPassword(rs.getString("password"))
                .setSuperadminId(rs.getInt("superadmin_id"))
                .build();
    };

    public String createAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username, password, superadmin_id) VALUES (?, ?, ?)";

        // Getter metodları ile verileri alıyoruz
        int rows = jdbcTemplate.update(sql,
                admin.getUsername(),
                admin.getPassword(),
                admin.getSuperadminId());

        return (rows > 0) ? "Admin başarıyla eklendi." : "Admin eklenemedi.";
    }

    public List<Admin> getAllAdminler() {
        String sql = "SELECT * FROM admin";
        // Yukarıda tanımladığımız adminRowMapper'ı kullanıyoruz
        return jdbcTemplate.query(sql, adminRowMapper);
    }

    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
        // query metodu liste döner, biz ilk elemanı alırız
        List<Admin> results = jdbcTemplate.query(sql, adminRowMapper, id);

        return results.isEmpty() ? null : results.get(0);
    }

    public String updateAdmin(int id, Admin admin) {
        // Not: ID kontrolü veritabanı sorgusunda (WHERE id = ?) yapılır.
        String sql = "UPDATE admin SET username = ?, password = ?, superadmin_id = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql,
                admin.getUsername(),
                admin.getPassword(),
                admin.getSuperadminId(),
                id); // ID parametresi burada kullanılır

        return (rows > 0) ? "Admin başarıyla güncellendi." : "Admin bulunamadı!";
    }

    public String deleteAdmin(int id) {
        String sql = "DELETE FROM admin WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return (rows > 0) ? "Admin başarıyla silindi!" : "Admin bulunamadı!";
    }
}