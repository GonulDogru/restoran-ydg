package com.restoran.facade;

import com.restoran.model.Icecek; // Artık 'model' paketinde
import com.restoran.repository.IcecekService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IcecekFacade {

    // 1. Güvenli Bağımlılık Enjeksiyonu (Constructor Injection)
    private final IcecekService icecekService;

    public IcecekFacade(IcecekService icecekService) {
        this.icecekService = icecekService;
    }

    public String createIcecek(Icecek icecek) {
        // İsterseniz burada içecek adı kontrolü yapabilirsiniz
        // if (icecek.getName() == null) return "İçecek adı boş olamaz";
        return icecekService.createIcecek(icecek);
    }

    public List<Icecek> getAllIcecekler() {
        return icecekService.getAllIcecekler();
    }

    public Icecek getIcecekById(int id) {
        return icecekService.getIcecekById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Icecek sınıfı immutable (değişmez) olduğu için setter metodu yoktur.
     * Bu yüzden gelen verileri alıp Builder ile yeni bir nesne oluşturuyoruz.
     */
    public String updateIcecek(int id, Icecek yeniVeriler) {
        Icecek guncellenecekIcecek = new Icecek.IcecekBuilder()
                .setId(id) // ID'yi parametreden alıp sabitliyoruz
                .setName(yeniVeriler.getName())
                .setMenuId(yeniVeriler.getMenuId())
                .build();

        return icecekService.updateIcecek(id, guncellenecekIcecek);
    }

    public String deleteIcecek(int id) {
        return icecekService.deleteIcecek(id);
    }
}