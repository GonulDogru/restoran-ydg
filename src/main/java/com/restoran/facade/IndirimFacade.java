package com.restoran.facade;

import com.restoran.model.Indirim; // Güncel model paketi
import com.restoran.repository.IndirimService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndirimFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final IndirimService indirimService;

    public IndirimFacade(IndirimService indirimService) {
        this.indirimService = indirimService;
    }

    public String createIndirim(Indirim indirim) {
        // İsterseniz burada tutar kontrolü yapabilirsiniz (örn: negatif olamaz)
        return indirimService.createIndirim(indirim);
    }

    public List<Indirim> getAllIndirimler() {
        return indirimService.getAllIndirimler();
    }

    public Indirim getIndirimById(int id) {
        return indirimService.getIndirimById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Indirim nesnesi immutable olduğu için (setter yok),
     * parametre olarak gelen ID'yi koruyarak ve yeni verileri kullanarak
     * Builder aracılığıyla yeni bir nesne oluşturuyoruz.
     */
    public String updateIndirim(int id, Indirim yeniVeriler) {
        Indirim guncellenecekIndirim = new Indirim.IndirimBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setAmount(yeniVeriler.getAmount())
                .setKullaniciId(yeniVeriler.getKullaniciId())
                .build();

        return indirimService.updateIndirim(id, guncellenecekIndirim);
    }

    public String deleteIndirim(int id) {
        return indirimService.deleteIndirim(id);
    }
}