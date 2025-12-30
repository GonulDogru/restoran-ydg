package com.restoran.controller;

import com.restoran.model.Vardiya;
import com.restoran.facade.VardiyaFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vardiya")
public class WebVardiyaController {

    private final VardiyaFacade vardiyaFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebVardiyaController(VardiyaFacade vardiyaFacade) {
        this.vardiyaFacade = vardiyaFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listVardiya(Model model) {
        List<Vardiya> vardiyalar = vardiyaFacade.getAllVardiyalar();
        model.addAttribute("vardiyalar", vardiyalar);

        model.addAttribute("page", "vardiya");
        model.addAttribute("content", "vardiya/list");
        model.addAttribute("pageTitle", "Vardiya Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("vardiyaForm", new VardiyaForm());

        model.addAttribute("page", "vardiya");
        model.addAttribute("content", "vardiya/create");
        model.addAttribute("pageTitle", "Yeni Vardiya Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createVardiya(@ModelAttribute("vardiyaForm") VardiyaForm vardiyaForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Vardiya yeniVardiya = vardiyaForm.toVardiya();

        vardiyaFacade.createVardiya(yeniVardiya);
        return "redirect:/vardiya/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Vardiya existing = vardiyaFacade.getVardiyaById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        VardiyaForm form = new VardiyaForm();
        form.setId(existing.getId());
        form.setSaatler(existing.getSaatler());
        form.setCalisanId(existing.getCalisanId());

        model.addAttribute("vardiyaForm", form); // "vardiya" yerine "vardiyaForm"

        model.addAttribute("page", "vardiya");
        model.addAttribute("content", "vardiya/update");
        model.addAttribute("pageTitle", "Vardiya Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateVardiya(@PathVariable int id, @ModelAttribute("vardiyaForm") VardiyaForm vardiyaForm) {
        // ID'yi garanti altına alıyoruz
        vardiyaForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Vardiya guncellenecekVardiya = vardiyaForm.toVardiya();
        vardiyaFacade.updateVardiya(id, guncellenecekVardiya);

        return "redirect:/vardiya/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteVardiya(@PathVariable int id) {
        vardiyaFacade.deleteVardiya(id);
        return "redirect:/vardiya/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class VardiyaForm {
        private int id;
        private String saatler;
        private int calisanId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getSaatler() { return saatler; }
        public void setSaatler(String saatler) { this.saatler = saatler; }

        public int getCalisanId() { return calisanId; }
        public void setCalisanId(int calisanId) { this.calisanId = calisanId; }

        // Form nesnesini Immutable Vardiya modeline dönüştürür
        public Vardiya toVardiya() {
            return new Vardiya.VardiyaBuilder()
                    .setId(this.id)
                    .setSaatler(this.saatler)
                    .setCalisanId(this.calisanId)
                    .build();
        }
    }
}