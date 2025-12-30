package com.restoran.controller;

import com.restoran.model.Calisan;
import com.restoran.facade.CalisanFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/calisan")
public class WebCalisanController {

    private final CalisanFacade calisanFacade;

    // 1. Constructor Injection (En Güvenli Yöntem)
    public WebCalisanController(CalisanFacade calisanFacade) {
        this.calisanFacade = calisanFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listCalisan(Model model) {
        List<Calisan> calisanlar = calisanFacade.getAllCalisanlar();
        model.addAttribute("calisanlar", calisanlar);

        model.addAttribute("page", "calisan");
        model.addAttribute("content", "calisan/list");
        model.addAttribute("pageTitle", "Çalışan Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form için boş bir CalisanForm gönderiyoruz (Model değil, DTO)
        model.addAttribute("calisanForm", new CalisanForm());

        model.addAttribute("page", "calisan");
        model.addAttribute("content", "calisan/create");
        model.addAttribute("pageTitle", "Yeni Çalışan Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createCalisan(@ModelAttribute("calisanForm") CalisanForm calisanForm) {
        // Formdan gelen mutable (değişebilir) veriyi, Immutable Modele çeviriyoruz
        Calisan yeniCalisan = calisanForm.toCalisan();

        calisanFacade.createCalisan(yeniCalisan);
        return "redirect:/calisan/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Calisan existing = calisanFacade.getCalisanById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) çevirip sayfaya gönderiyoruz
        CalisanForm form = new CalisanForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setRestaurantId(existing.getRestaurantId());

        model.addAttribute("calisanForm", form); // "calisan" yerine "calisanForm"

        model.addAttribute("page", "calisan");
        model.addAttribute("content", "calisan/update");
        model.addAttribute("pageTitle", "Çalışan Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateCalisan(@PathVariable int id, @ModelAttribute("calisanForm") CalisanForm calisanForm) {
        // ID'yi path'den garanti altına alıyoruz
        calisanForm.setId(id);

        // Form verisini modele çevirip Facade'a iletiyoruz
        Calisan guncellenecekCalisan = calisanForm.toCalisan();
        calisanFacade.updateCalisan(id, guncellenecekCalisan);

        return "redirect:/calisan/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteCalisan(@PathVariable int id) {
        calisanFacade.deleteCalisan(id);
        return "redirect:/calisan/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // HTML form verilerini yakalayıp Immutable Calisan nesnesine dönüştürür.
    // =========================================================================
    public static class CalisanForm {
        private int id;
        private String name;
        private int restaurantId;

        // Getter ve Setter'lar (Spring Form Binding için ZORUNLUDUR)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getRestaurantId() { return restaurantId; }
        public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

        // Form verisini gerçek Immutable Modele (Builder ile) dönüştüren metot
        public Calisan toCalisan() {
            return new Calisan.CalisanBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setRestaurantId(this.restaurantId)
                    .build();
        }
    }
}