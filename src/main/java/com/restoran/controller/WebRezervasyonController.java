package com.restoran.controller;

import com.restoran.model.Rezervasyon;
import com.restoran.facade.RezervasyonFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rezervasyon")
public class WebRezervasyonController {

    private final RezervasyonFacade rezervasyonFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebRezervasyonController(RezervasyonFacade rezervasyonFacade) {
        this.rezervasyonFacade = rezervasyonFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listRezervasyon(Model model) {
        List<Rezervasyon> rezervasyonlar = rezervasyonFacade.getAllRezervasyonlar();
        model.addAttribute("rezervasyonlar", rezervasyonlar);

        model.addAttribute("page", "rezervasyon");
        model.addAttribute("content", "rezervasyon/list");
        model.addAttribute("pageTitle", "Rezervasyon Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("rezervasyonForm", new RezervasyonForm());

        model.addAttribute("page", "rezervasyon");
        model.addAttribute("content", "rezervasyon/create");
        model.addAttribute("pageTitle", "Yeni Rezervasyon Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createRezervasyon(@ModelAttribute("rezervasyonForm") RezervasyonForm rezervasyonForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Rezervasyon yeniRezervasyon = rezervasyonForm.toRezervasyon();

        rezervasyonFacade.createRezervasyon(yeniRezervasyon);
        return "redirect:/rezervasyon/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Rezervasyon existing = rezervasyonFacade.getRezervasyonById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        RezervasyonForm form = new RezervasyonForm();
        form.setId(existing.getId());
        form.setTarih(existing.getTarih()); // Modelde String tutulduğunu varsayıyoruz
        form.setSaat(existing.getSaat());   // Modelde String tutulduğunu varsayıyoruz
        form.setUserId(existing.getUserId());
        form.setMasaId(existing.getMasaId());

        model.addAttribute("rezervasyonForm", form); // "rezervasyon" yerine "rezervasyonForm"

        model.addAttribute("page", "rezervasyon");
        model.addAttribute("content", "rezervasyon/update");
        model.addAttribute("pageTitle", "Rezervasyon Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateRezervasyon(@PathVariable int id, @ModelAttribute("rezervasyonForm") RezervasyonForm rezervasyonForm) {
        // ID'yi garanti altına alıyoruz
        rezervasyonForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Rezervasyon guncellenecekRezervasyon = rezervasyonForm.toRezervasyon();
        rezervasyonFacade.updateRezervasyon(id, guncellenecekRezervasyon);

        return "redirect:/rezervasyon/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteRezervasyon(@PathVariable int id) {
        rezervasyonFacade.deleteRezervasyon(id);
        return "redirect:/rezervasyon/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class RezervasyonForm {
        private int id;
        private String tarih; // "yyyy-MM-dd" formatında gelir
        private String saat;  // "HH:mm" formatında gelir
        private int userId;
        private int masaId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTarih() { return tarih; }
        public void setTarih(String tarih) { this.tarih = tarih; }

        public String getSaat() { return saat; }
        public void setSaat(String saat) { this.saat = saat; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public int getMasaId() { return masaId; }
        public void setMasaId(int masaId) { this.masaId = masaId; }

        // Form nesnesini Immutable Rezervasyon modeline dönüştürür
        public Rezervasyon toRezervasyon() {
            return new Rezervasyon.RezervasyonBuilder()
                    .setId(this.id)
                    .setTarih(this.tarih)
                    .setSaat(this.saat)
                    .setUserId(this.userId)
                    .setMasaId(this.masaId)
                    .build();
        }
    }
}