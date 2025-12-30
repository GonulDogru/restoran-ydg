package com.restoran.facade;

import com.restoran.model.Superadmin; // Güncel model paketi
import com.restoran.repository.SuperadminService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuperadminFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    // Field injection yerine final değişken ve kurucu metod kullanılır.
    private final SuperadminService superadminService;

    public SuperadminFacade(SuperadminService superadminService) {
        this.superadminService = superadminService;
    }

    public String createSuperadmin(Superadmin superadmin) {
        // Facade katmanı, kullanıcı adı ve şifre kurallarını kontrol etmek için idealdir.
        if (superadmin.getUsername() == null || superadmin.getPassword().length() < 6) {
            return "Hata: Geçersiz kullanıcı adı veya zayıf şifre.";
        }
        return superadminService.createSuperadmin(superadmin);
    }

    public List<Superadmin> getAllSuperadminler() {
        return superadminService.getAllSuperadminler();
    }

    public Superadmin getSuperadminById(int id) {
        return superadminService.getSuperadminById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Superadmin nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateSuperadmin(int id, Superadmin yeniVeriler) {
        Superadmin guncellenecekAdmin = new Superadmin.SuperadminBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setUsername(yeniVeriler.getUsername())
                .setPassword(yeniVeriler.getPassword())
                .build();

        return superadminService.updateSuperadmin(id, guncellenecekAdmin);
    }

    public String deleteSuperadmin(int id) {
        return superadminService.deleteSuperadmin(id);
    }
}