package com.restoran.facade;

import com.restoran.model.Rezervasyon; // Güncel model paketi
import com.restoran.repository.RezervasyonService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RezervasyonFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final RezervasyonService rezervasyonService;

    public RezervasyonFacade(RezervasyonService rezervasyonService) {
        this.rezervasyonService = rezervasyonService;
    }

    public String createRezervasyon(Rezervasyon rezervasyon) {
        // Facade, iş kuralı eklemek için uygun yerdir.
        // Örn: if(rezervasyon.getSaat() == null) return "Saat boş olamaz";
        return rezervasyonService.createRezervasyon(rezervasyon);
    }

    public List<Rezervasyon> getAllRezervasyonlar() {
        return rezervasyonService.getAllRezervasyonlar();
    }

    public Rezervasyon getRezervasyonById(int id) {
        return rezervasyonService.getRezervasyonById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Rezervasyon nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateRezervasyon(int id, Rezervasyon yeniVeriler) {
        Rezervasyon guncellenecekRezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setTarih(yeniVeriler.getTarih())
                .setSaat(yeniVeriler.getSaat())
                .setUserId(yeniVeriler.getUserId())
                .setMasaId(yeniVeriler.getMasaId())
                .build();

        return rezervasyonService.updateRezervasyon(id, guncellenecekRezervasyon);
    }

    public String deleteRezervasyon(int id) {
        return rezervasyonService.deleteRezervasyon(id);
    }
}