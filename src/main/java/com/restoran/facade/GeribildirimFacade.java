package com.restoran.facade;

import com.restoran.model.Geribildirim; // Builder yapısına sahip yeni sınıf
import com.restoran.repository.GeribildirimService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeribildirimFacade {

    // 1. Field Injection yerine Constructor Injection kullanımı
    private final GeribildirimService geribildirimService;

    public GeribildirimFacade(GeribildirimService geribildirimService) {
        this.geribildirimService = geribildirimService;
    }

    public String createGeribildirim(Geribildirim geribildirim) {
        // İsteğe bağlı validasyon: Yorum boşsa servise gitmeden reddet
        if (geribildirim.getYorum() == null || geribildirim.getYorum().trim().isEmpty()) {
            return "Hata: Yorum içeriği boş olamaz.";
        }
        return geribildirimService.createGeribildirim(geribildirim);
    }

    public List<Geribildirim> getAllGeribildirimler() {
        return geribildirimService.getAllGeribildirimler();
    }

    public Geribildirim getGeribildirimById(int id) {
        return geribildirimService.getGeribildirimById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Geribildirim nesnesi immutable (değişmez) olduğu için,
     * gelen verileri kullanarak yeni bir kopya oluşturuyoruz.
     */
    public String updateGeribildirim(int id, Geribildirim yeniVeriler) {
        // Builder ile ID'yi sabitleyip, diğer alanları güncelleyerek nesne oluştur
        Geribildirim guncellenecekYorum = new Geribildirim.GeribildirimBuilder()
                .setId(id) // ID parametreden gelir
                .setYorum(yeniVeriler.getYorum())
                .setUserId(yeniVeriler.getUserId())
                .build();

        return geribildirimService.updateGeribildirim(id, guncellenecekYorum);
    }

    public String deleteGeribildirim(int id) {
        return geribildirimService.deleteGeribildirim(id);
    }
}