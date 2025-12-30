package com.restoran.facade;

import com.restoran.model.Kullanici; // Yeni sınıf ismi

import com.restoran.repository.KullaniciService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KullaniciFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final KullaniciService kullaniciService;

    public KullaniciFacade(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }

    public String createKullanici(Kullanici kullanici) {
        // İsterseniz burada şifre uzunluğu veya kullanıcı adı formatı kontrolü yapabilirsiniz.
        if (kullanici.getUsername() == null || kullanici.getUsername().isEmpty()) {
            return "Hata: Kullanıcı adı zorunludur.";
        }
        return kullaniciService.createKullanici(kullanici);
    }

    public List<Kullanici> getAllKullanicilar() {
        return kullaniciService.getAllKullanicilar();
    }

    public Kullanici getKullaniciById(int id) {
        return kullaniciService.getKullaniciById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Kullanici nesnesi immutable (değişmez) olduğu için setter metodu yoktur.
     * Parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateKullanici(int id, Kullanici yeniVeriler) {
        Kullanici guncellenecekKullanici = new Kullanici.KullaniciBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setUsername(yeniVeriler.getUsername())
                .setPassword(yeniVeriler.getPassword())
                .setAdres(yeniVeriler.getAdres())
                .setTelefon(yeniVeriler.getTelefon())
                .build();

        return kullaniciService.updateKullanici(id, guncellenecekKullanici);
    }

    public String deleteKullanici(int id) {
        return kullaniciService.deleteKullanici(id);
    }
}