package com.restoran.facade;

import com.restoran.model.Stok; // Güncel model paketi
import com.restoran.repository.StokService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StokFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final StokService stokService;

    public StokFacade(StokService stokService) {
        this.stokService = stokService;
    }

    public String createStok(Stok stok) {
        // İsterseniz stok miktarı negatif mi diye kontrol edebilirsiniz.
        if (stok.getMiktar() < 0) {
            return "Hata: Stok miktarı 0'dan küçük olamaz.";
        }
        return stokService.createStok(stok);
    }

    public List<Stok> getAllStoklar() {
        return stokService.getAllStoklar();
    }

    public Stok getStokById(int id) {
        return stokService.getStokById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Stok nesnesi immutable (değişmez) olduğu için,
     * parametre olarak gelen ID'yi koruyarak ve 'yeniVeriler' nesnesinden
     * bilgileri alarak Builder ile yeni bir nesne inşa ediyoruz.
     */
    public String updateStok(int id, Stok yeniVeriler) {
        Stok guncellenecekStok = new Stok.StokBuilder()
                .setId(id) // ID dışarıdan (URL'den) gelir, sabittir.
                .setMiktar(yeniVeriler.getMiktar())
                .setYemekId(yeniVeriler.getYemekId())
                .setIcecekId(yeniVeriler.getIcecekId())
                .setTatliId(yeniVeriler.getTatliId())
                .setTedarikciId(yeniVeriler.getTedarikciId())
                .build();

        return stokService.updateStok(id, guncellenecekStok);
    }

    public String deleteStok(int id) {
        return stokService.deleteStok(id);
    }
}