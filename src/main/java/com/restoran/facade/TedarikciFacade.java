package com.restoran.facade;

import com.restoran.model.Tedarikci; // Güncel model paketi
import com.restoran.repository.TedarikciService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TedarikciFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final TedarikciService tedarikciService;

    public TedarikciFacade(TedarikciService tedarikciService) {
        this.tedarikciService = tedarikciService;
    }

    public String createTedarikci(Tedarikci tedarikci) {
        // İsterseniz burada telefon numarası formatı kontrolü yapabilirsiniz.
        return tedarikciService.createTedarikci(tedarikci);
    }

    public List<Tedarikci> getAllTedarikciler() {
        return tedarikciService.getAllTedarikciler();
    }

    public Tedarikci getTedarikciById(int id) {
        return tedarikciService.getTedarikciById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Tedarikci nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateTedarikci(int id, Tedarikci yeniVeriler) {
        Tedarikci guncellenecekTedarikci = new Tedarikci.TedarikciBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setName(yeniVeriler.getName())
                .setPhone(yeniVeriler.getPhone())
                .setRestaurantId(yeniVeriler.getRestaurantId())
                .build();

        return tedarikciService.updateTedarikci(id, guncellenecekTedarikci);
    }

    public String deleteTedarikci(int id) {
        return tedarikciService.deleteTedarikci(id);
    }
}