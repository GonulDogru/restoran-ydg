package com.restoran.controller;

import com.restoran.model.Siparis;
import com.restoran.facade.SiparisFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/siparis")
public class WebSiparisController {

    private final SiparisFacade siparisFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebSiparisController(SiparisFacade siparisFacade) {
        this.siparisFacade = siparisFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listSiparis(Model model) {
        List<Siparis> siparisler = siparisFacade.getAllSiparisler();
        model.addAttribute("siparisler", siparisler);

        model.addAttribute("page", "siparis");
        model.addAttribute("content", "siparis/list");
        model.addAttribute("pageTitle", "Sipariş Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("siparisForm", new SiparisForm());

        model.addAttribute("page", "siparis");
        model.addAttribute("content", "siparis/create");
        model.addAttribute("pageTitle", "Yeni Sipariş Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createSiparis(@ModelAttribute("siparisForm") SiparisForm siparisForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Siparis yeniSiparis = siparisForm.toSiparis();

        siparisFacade.createSiparis(yeniSiparis);
        return "redirect:/siparis/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Siparis existing = siparisFacade.getSiparisById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        SiparisForm form = new SiparisForm();
        form.setId(existing.getId());
        form.setTarih(existing.getTarih()); // String formatında tarih
        form.setAmount(existing.getAmount());
        form.setUserId(existing.getUserId());
        form.setMasaId(existing.getMasaId());

        model.addAttribute("siparisForm", form); // "siparis" yerine "siparisForm"

        model.addAttribute("page", "siparis");
        model.addAttribute("content", "siparis/update");
        model.addAttribute("pageTitle", "Sipariş Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateSiparis(@PathVariable int id, @ModelAttribute("siparisForm") SiparisForm siparisForm) {
        // ID'yi garanti altına alıyoruz
        siparisForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Siparis guncellenecekSiparis = siparisForm.toSiparis();
        siparisFacade.updateSiparis(id, guncellenecekSiparis);

        return "redirect:/siparis/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteSiparis(@PathVariable int id) {
        siparisFacade.deleteSiparis(id);
        return "redirect:/siparis/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class SiparisForm {
        private int id;
        private String tarih; // "yyyy-MM-dd" formatında
        private float amount;
        private int userId;
        private int masaId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTarih() { return tarih; }
        public void setTarih(String tarih) { this.tarih = tarih; }

        public float getAmount() { return amount; }
        public void setAmount(float amount) { this.amount = amount; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public int getMasaId() { return masaId; }
        public void setMasaId(int masaId) { this.masaId = masaId; }

        // Form nesnesini Immutable Siparis modeline dönüştürür
        public Siparis toSiparis() {
            return new Siparis.SiparisBuilder()
                    .setId(this.id)
                    .setTarih(this.tarih)
                    .setAmount(this.amount)
                    .setUserId(this.userId)
                    .setMasaId(this.masaId)
                    .build();
        }
    }
}