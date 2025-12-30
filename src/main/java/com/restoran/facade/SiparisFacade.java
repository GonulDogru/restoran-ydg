package com.restoran.facade;

import com.restoran.model.Siparis; // Güncel model paketi
import com.restoran.repository.SiparisService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SiparisFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    // Field injection (@Autowired) yerine final değişken ve kurucu metod kullanılır.
    private final SiparisService siparisService;

    public SiparisFacade(SiparisService siparisService) {
        this.siparisService = siparisService;
    }

    public String createSiparis(Siparis siparis) {
        // İsterseniz sipariş tutarı kontrolü gibi iş kuralları buraya eklenebilir.
        // if (siparis.getAmount() <= 0) return "Sipariş tutarı geçersiz.";
        return siparisService.createSiparis(siparis);
    }

    public List<Siparis> getAllSiparisler() {
        return siparisService.getAllSiparisler();
    }

    public Siparis getSiparisById(int id) {
        return siparisService.getSiparisById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Siparis nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateSiparis(int id, Siparis yeniVeriler) {
        Siparis guncellenecekSiparis = new Siparis.SiparisBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setTarih(yeniVeriler.getTarih())
                .setAmount(yeniVeriler.getAmount())
                .setUserId(yeniVeriler.getUserId())
                .setMasaId(yeniVeriler.getMasaId())
                .build();

        return siparisService.updateSiparis(id, guncellenecekSiparis);
    }

    public String deleteSiparis(int id) {
        return siparisService.deleteSiparis(id);
    }
}