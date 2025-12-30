package com.restoran.facade;

import com.restoran.model.Admin;
import com.restoran.repository.AdminService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminFacade {

    private final AdminService adminService;

    // Constructor Injection (Best Practice)
    public AdminFacade(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Yeni Admin oluşturur.
     * Facade burada bir ön kontrol (validasyon) katmanı görevi görebilir.
     */
    public String createAdmin(Admin admin) {
        // Örnek Facade Mantığı: Admin adı boş mu kontrol et, sonra servise git
        if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
            return "Hata: Kullanıcı adı boş olamaz!";
        }
        return adminService.createAdmin(admin);
    }

    public List<Admin> getAllAdminler() {
        return adminService.getAllAdminler();
    }

    public Admin getAdminById(int id) {
        return adminService.getAdminById(id);
    }

    /**
     * Admin Güncelleme
     * NOT: Admin nesnesi artık Immutable (Değişmez) olduğu için Setter metodları yoktur.
     * Facade, gelen veriyi ve ID'yi alıp Builder ile yeni bir nesne oluşturup servise iletir.
     */
    public String updateAdmin(int id, Admin yeniVeriler) {
        // 1. Mevcut admin var mı kontrolü (Servis katmanında da olabilir ama Facade yönetebilir)
        Admin mevcutAdmin = adminService.getAdminById(id);
        if (mevcutAdmin == null) {
            return "Hata: Güncellenecek Admin bulunamadı.";
        }

        // 2. Builder kullanarak ID'yi koruyup yeni verilerle nesneyi yeniden oluşturuyoruz.
        // Çünkü setter metodumuz yok (admin.setUsername(...) yapamayız).
        Admin guncellenecekAdmin = new Admin.AdminBuilder()
                .setId(id) // ID değişmemeli, parametreden gelen ID kullanılır
                .setUsername(yeniVeriler.getUsername())
                .setPassword(yeniVeriler.getPassword())
                .setSuperadminId(yeniVeriler.getSuperadminId())
                .build();

        // 3. Servise yeni oluşturulan nesne gönderilir
        return adminService.updateAdmin(id, guncellenecekAdmin);
    }

    public String deleteAdmin(int id) {
        return adminService.deleteAdmin(id);
    }
}