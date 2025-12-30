package com.restoran.facade;

import com.restoran.model.Garson; // Builder yapısına sahip yeni sınıf
import com.restoran.repository.GarsonService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GarsonFacade {

    // 1. Constructor Injection (Best Practice)
    // Field Injection (@Autowired) yerine final değişken ve kurucu metod kullanıyoruz.
    private final GarsonService garsonService;

    public GarsonFacade(GarsonService garsonService) {
        this.garsonService = garsonService;
    }

    public String createGarson(Garson garson) {
        // İsteğe bağlı: Burada garson verisi için validasyon yapılabilir.
        return garsonService.createGarson(garson);
    }

    public List<Garson> getAllGarsonlar() {
        return garsonService.getAllGarsonlar();
    }

    public Garson getGarsonById(int id) {
        return garsonService.getGarsonById(id);
    }

    /**
     * Güncelleme İşlemi:
     * Garson nesnesi immutable olduğu için (setter yok),
     * parametre olarak gelen ID'yi ve yeni verileri kullanarak
     * yeni bir nesne inşa edip servise gönderiyoruz.
     */
    public String updateGarson(int id, Garson yeniVeriler) {
        Garson guncellenecekGarson = new Garson.GarsonBuilder()
                .setId(id) // Güncellenecek ID sabit kalmalı
                .setName(yeniVeriler.getName())
                .setCalisanId(yeniVeriler.getCalisanId())
                .build();

        return garsonService.updateGarson(id, guncellenecekGarson);
    }

    public String deleteGarson(int id) {
        return garsonService.deleteGarson(id);
    }
}