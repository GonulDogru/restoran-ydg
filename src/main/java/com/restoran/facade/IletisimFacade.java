package com.restoran.facade;

import com.restoran.model.Iletisim; // Güncel paket ismi
import com.restoran.repository.IletisimService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IletisimFacade {

    // 1. Field Injection yerine Constructor Injection kullanımı
    private final IletisimService iletisimService;

    public IletisimFacade(IletisimService iletisimService) {
        this.iletisimService = iletisimService;
    }

    public String createIletisim(Iletisim iletisim) {
        // İsteğe bağlı validasyon: Mesaj boş mu?
        if (iletisim.getMesaj() == null || iletisim.getMesaj().trim().isEmpty()) {
            return "Hata: Mesaj içeriği boş olamaz.";
        }
        return iletisimService.createIletisim(iletisim);
    }

    public List<Iletisim> getAllIletisimler() {
        return iletisimService.getAllIletisimler();
    }

    public Iletisim getIletisimById(int id) {
        return iletisimService.getIletisimById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Iletisim nesnesi immutable olduğu için, ID'yi parametreden alıp
     * diğer verileri 'yeniVeriler' nesnesinden alarak Builder ile yeni bir nesne oluşturuyoruz.
     */
    public String updateIletisim(int id, Iletisim yeniVeriler) {
        Iletisim guncellenecekMesaj = new Iletisim.IletisimBuilder()
                .setId(id) // ID sabit kalmalı (Path Variable'dan gelir)
                .setUserId(yeniVeriler.getUserId())
                .setRestaurantId(yeniVeriler.getRestaurantId())
                .setMesaj(yeniVeriler.getMesaj())
                .build();

        return iletisimService.updateIletisim(id, guncellenecekMesaj);
    }

    public String deleteIletisim(int id) {
        return iletisimService.deleteIletisim(id);
    }
}