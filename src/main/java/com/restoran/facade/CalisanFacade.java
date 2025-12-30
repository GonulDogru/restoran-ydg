package com.restoran.facade;

import com.restoran.model.Calisan; // Builder yapısının olduğu paket
import com.restoran.repository.CalisanService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalisanFacade {

    // 1. Field Injection (@Autowired) yerine Constructor Injection kullanılır.
    // Bu, nesnenin test edilebilirliğini ve güvenliğini artırır.
    private final CalisanService calisanService;

    public CalisanFacade(CalisanService calisanService) {
        this.calisanService = calisanService;
    }

    public String createCalisan(Calisan calisan) {
        // İsterseniz burada basit validasyonlar yapabilirsiniz.
        // Örn: if(calisan.getName() == null) return "İsim boş olamaz";
        return calisanService.createCalisan(calisan);
    }

    public List<Calisan> getAllCalisanlar() {
        return calisanService.getAllCalisanlar();
    }

    public Calisan getCalisanById(int id) {
        return calisanService.getCalisanById(id);
    }

    /**
     * Güncelleme işlemi:
     * Calisan nesnesi artık Immutable (Değişmez) olduğu için setter metodu kullanamayız.
     * Facade katmanı, gelen yeni veriyi ve ID'yi alıp Builder ile yeni bir nesne oluşturur
     * ve servise bu hazırlanmış nesneyi gönderir.
     */
    public String updateCalisan(int id, Calisan yeniVeriler) {
        // ID parametresini koruyarak yeni bir nesne inşa ediyoruz
        Calisan guncellenecekCalisan = new Calisan.CalisanBuilder()
                .setId(id) // ID dışarıdan parametre olarak gelir
                .setName(yeniVeriler.getName())
                .setRestaurantId(yeniVeriler.getRestaurantId())
                .build();

        return calisanService.updateCalisan(id, guncellenecekCalisan);
    }

    public String deleteCalisan(int id) {
        return calisanService.deleteCalisan(id);
    }
}