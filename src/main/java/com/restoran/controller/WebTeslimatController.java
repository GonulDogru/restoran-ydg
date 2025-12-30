package com.restoran.controller;

import com.restoran.model.Teslimat;
import com.restoran.facade.TeslimatFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teslimat")
public class WebTeslimatController {

    private final TeslimatFacade teslimatFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebTeslimatController(TeslimatFacade teslimatFacade) {
        this.teslimatFacade = teslimatFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listTeslimat(Model model) {
        List<Teslimat> teslimatlar = teslimatFacade.getAllTeslimatlar();
        model.addAttribute("teslimatlar", teslimatlar);

        model.addAttribute("page", "teslimat");
        model.addAttribute("content", "teslimat/list");
        model.addAttribute("pageTitle", "Teslimat Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("teslimatForm", new TeslimatForm());

        model.addAttribute("page", "teslimat");
        model.addAttribute("content", "teslimat/create");
        model.addAttribute("pageTitle", "Yeni Teslimat Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createTeslimat(@ModelAttribute("teslimatForm") TeslimatForm teslimatForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Teslimat yeniTeslimat = teslimatForm.toTeslimat();

        teslimatFacade.createTeslimat(yeniTeslimat);
        return "redirect:/teslimat/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Teslimat existing = teslimatFacade.getTeslimatById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        TeslimatForm form = new TeslimatForm();
        form.setId(existing.getId());
        form.setAddress(existing.getAddress());
        form.setDurum(existing.getDurum());
        form.setRestaurantId(existing.getRestaurantId());
        form.setSiparisId(existing.getSiparisId());

        model.addAttribute("teslimatForm", form); // "teslimat" yerine "teslimatForm"

        model.addAttribute("page", "teslimat");
        model.addAttribute("content", "teslimat/update");
        model.addAttribute("pageTitle", "Teslimat Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateTeslimat(@PathVariable int id, @ModelAttribute("teslimatForm") TeslimatForm teslimatForm) {
        // ID'yi garanti altına alıyoruz
        teslimatForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Teslimat guncellenecekTeslimat = teslimatForm.toTeslimat();
        teslimatFacade.updateTeslimat(id, guncellenecekTeslimat);

        return "redirect:/teslimat/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteTeslimat(@PathVariable int id) {
        teslimatFacade.deleteTeslimat(id);
        return "redirect:/teslimat/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class TeslimatForm {
        private int id;
        private String address;
        private String durum;
        private int restaurantId;
        private int siparisId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getDurum() { return durum; }
        public void setDurum(String durum) { this.durum = durum; }

        public int getRestaurantId() { return restaurantId; }
        public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

        public int getSiparisId() { return siparisId; }
        public void setSiparisId(int siparisId) { this.siparisId = siparisId; }

        // Form nesnesini Immutable Teslimat modeline dönüştürür
        public Teslimat toTeslimat() {
            return new Teslimat.TeslimatBuilder()
                    .setId(this.id)
                    .setAddress(this.address)
                    .setDurum(this.durum)
                    .setRestaurantId(this.restaurantId)
                    .setSiparisId(this.siparisId)
                    .build();
        }
    }
}