package com.restoran.facade;

import com.restoran.model.Teslimat; // Güncel model paketi
import com.restoran.repository.TeslimatService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeslimatFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final TeslimatService teslimatService;

    public TeslimatFacade(TeslimatService teslimatService) {
        this.teslimatService = teslimatService;
    }

    public String createTeslimat(Teslimat teslimat) {
        // İsterseniz burada adres veya sipariş ID kontrolü yapabilirsiniz.
        if (teslimat.getAddress() == null || teslimat.getAddress().isEmpty()) {
            return "Hata: Teslimat adresi boş olamaz.";
        }
        return teslimatService.createTeslimat(teslimat);
    }

    public List<Teslimat> getAllTeslimatlar() {
        return teslimatService.getAllTeslimatlar();
    }

    public Teslimat getTeslimatById(int id) {
        return teslimatService.getTeslimatById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Teslimat nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateTeslimat(int id, Teslimat yeniVeriler) {
        Teslimat guncellenecekTeslimat = new Teslimat.TeslimatBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setAddress(yeniVeriler.getAddress())
                .setDurum(yeniVeriler.getDurum())
                .setRestaurantId(yeniVeriler.getRestaurantId())
                .setSiparisId(yeniVeriler.getSiparisId())
                .build();

        return teslimatService.updateTeslimat(id, guncellenecekTeslimat);
    }

    public String deleteTeslimat(int id) {
        return teslimatService.deleteTeslimat(id);
    }
}