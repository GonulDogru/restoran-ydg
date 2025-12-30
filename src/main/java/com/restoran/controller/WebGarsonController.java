package com.restoran.controller;

import com.restoran.model.Garson;
import com.restoran.facade.GarsonFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/garson")
public class WebGarsonController {

    private final GarsonFacade garsonFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebGarsonController(GarsonFacade garsonFacade) {
        this.garsonFacade = garsonFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listGarson(Model model) {
        List<Garson> garsonlar = garsonFacade.getAllGarsonlar();
        model.addAttribute("garsonlar", garsonlar);

        model.addAttribute("page", "garson");
        model.addAttribute("content", "garson/list");
        model.addAttribute("pageTitle", "Garson Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("garsonForm", new GarsonForm());

        model.addAttribute("page", "garson");
        model.addAttribute("content", "garson/create");
        model.addAttribute("pageTitle", "Yeni Garson Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createGarson(@ModelAttribute("garsonForm") GarsonForm garsonForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Garson yeniGarson = garsonForm.toGarson();

        garsonFacade.createGarson(yeniGarson);
        return "redirect:/garson/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Garson existing = garsonFacade.getGarsonById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) map ediyoruz
        GarsonForm form = new GarsonForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setCalisanId(existing.getCalisanId());

        model.addAttribute("garsonForm", form); // "garson" yerine "garsonForm"

        model.addAttribute("page", "garson");
        model.addAttribute("content", "garson/update");
        model.addAttribute("pageTitle", "Garson Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateGarson(@PathVariable int id, @ModelAttribute("garsonForm") GarsonForm garsonForm) {
        // ID'yi garanti altına alıyoruz
        garsonForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Garson guncellenecekGarson = garsonForm.toGarson();
        garsonFacade.updateGarson(id, guncellenecekGarson);

        return "redirect:/garson/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteGarson(@PathVariable int id) {
        garsonFacade.deleteGarson(id);
        return "redirect:/garson/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class GarsonForm {
        private int id;
        private String name;
        private int calisanId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getCalisanId() { return calisanId; }
        public void setCalisanId(int calisanId) { this.calisanId = calisanId; }

        // Form nesnesini Immutable Garson modeline dönüştürür
        public Garson toGarson() {
            return new Garson.GarsonBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setCalisanId(this.calisanId)
                    .build();
        }
    }
}