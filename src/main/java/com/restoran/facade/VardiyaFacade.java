package com.restoran.facade;

import com.restoran.model.Vardiya; // Güncel model paketi
import com.restoran.repository.VardiyaService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VardiyaFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final VardiyaService vardiyaService;

    public VardiyaFacade(VardiyaService vardiyaService) {
        this.vardiyaService = vardiyaService;
    }

    public String createVardiya(Vardiya vardiya) {
        // İsterseniz vardiya saatlerinin formatını kontrol edebilirsiniz.
        if (vardiya.getSaatler() == null || vardiya.getSaatler().isEmpty()) {
            return "Hata: Vardiya saatleri belirtilmelidir.";
        }
        return vardiyaService.createVardiya(vardiya);
    }

    public List<Vardiya> getAllVardiyalar() {
        return vardiyaService.getAllVardiyalar();
    }

    public Vardiya getVardiyaById(int id) {
        return vardiyaService.getVardiyaById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Vardiya nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateVardiya(int id, Vardiya yeniVeriler) {
        Vardiya guncellenecekVardiya = new Vardiya.VardiyaBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setSaatler(yeniVeriler.getSaatler())
                .setCalisanId(yeniVeriler.getCalisanId())
                .build();

        return vardiyaService.updateVardiya(id, guncellenecekVardiya);
    }

    public String deleteVardiya(int id) {
        return vardiyaService.deleteVardiya(id);
    }
}