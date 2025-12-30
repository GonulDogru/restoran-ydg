package com.restoran.facade;

import com.restoran.model.Tatli; // Güncel model paketi
import com.restoran.repository.TatliService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TatliFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final TatliService tatliService;

    public TatliFacade(TatliService tatliService) {
        this.tatliService = tatliService;
    }

    public String createTatli(Tatli tatli) {
        // İsterseniz burada tatlı adı boş mu diye kontrol edebilirsiniz.
        // if (tatli.getName() == null) return "Tatlı ismi boş olamaz";
        return tatliService.createTatli(tatli);
    }

    public List<Tatli> getAllTatlilar() {
        return tatliService.getAllTatlilar();
    }

    public Tatli getTatliById(int id) {
        return tatliService.getTatliById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Tatli nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateTatli(int id, Tatli yeniVeriler) {
        Tatli guncellenecekTatli = new Tatli.TatliBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setName(yeniVeriler.getName())
                .setMenuId(yeniVeriler.getMenuId())
                .build();

        return tatliService.updateTatli(id, guncellenecekTatli);
    }

    public String deleteTatli(int id) {
        return tatliService.deleteTatli(id);
    }
}