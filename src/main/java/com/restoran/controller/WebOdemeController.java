package com.restoran.controller;

import com.restoran.facade.OdemeFacade;
import com.restoran.model.Odeme;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/odeme")
public class WebOdemeController {

    private final OdemeFacade odemeFacade;

    public WebOdemeController(OdemeFacade odemeFacade) {
        this.odemeFacade = odemeFacade;
    }

    // 1. LİSTELEME
    @GetMapping("/list")
    public String listOdemeler(Model model) {
        List<Odeme> odemeler = odemeFacade.getAllOdemeler();
        model.addAttribute("odemeler", odemeler);
        model.addAttribute("page", "odeme");
        model.addAttribute("content", "odeme/list"); // templates/odeme/list.html
        model.addAttribute("pageTitle", "Ödeme Listesi");
        return "index";
    }

    // 2. YENİ EKLEME SAYFASI (FORM)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("odemeForm", new OdemeForm()); // Boş form nesnesi
        model.addAttribute("page", "odeme");
        model.addAttribute("content", "odeme/create"); // templates/odeme/create.html
        model.addAttribute("pageTitle", "Yeni Ödeme Kaydı");
        return "index";
    }

    // 3. KAYDETME İŞLEMİ
    @PostMapping("/save")
    public String saveOdeme(@ModelAttribute("odemeForm") OdemeForm form) {
        Odeme yeniOdeme = form.toOdeme();
        odemeFacade.createOdeme(yeniOdeme);
        return "redirect:/odeme/list";
    }

    // 4. DÜZENLEME SAYFASI (FORMU DOLDURMA)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Odeme mevcut = odemeFacade.getOdemeById(id);

        // Model nesnesini Form DTO'suna çeviriyoruz
        OdemeForm form = new OdemeForm();
        form.setId(mevcut.getId());
        form.setMasaId(mevcut.getMasaId());
        form.setTutar(mevcut.getTutar());
        form.setOdemeYontemi(mevcut.getOdemeYontemi());

        model.addAttribute("odemeForm", form);
        model.addAttribute("page", "odeme");
        model.addAttribute("content", "odeme/update"); // templates/odeme/update.html
        model.addAttribute("pageTitle", "Ödeme Güncelle");
        return "index";
    }

    // 5. GÜNCELLEME İŞLEMİ
    @PostMapping("/update/{id}")
    public String updateOdeme(@PathVariable int id, @ModelAttribute("odemeForm") OdemeForm form) {
        form.setId(id); // URL'den gelen ID'yi forma set ediyoruz
        odemeFacade.updateOdeme(id, form.toOdeme());
        return "redirect:/odeme/list";
    }

    // 6. SİLME İŞLEMİ
    @GetMapping("/delete/{id}")
    public String deleteOdeme(@PathVariable int id) {
        odemeFacade.deleteOdeme(id);
        return "redirect:/odeme/list";
    }

    // =========================================================================
    // INNER CLASS: OdemeForm (DTO) - Form verilerini taşımak için
    // =========================================================================
    public static class OdemeForm {
        private int id;
        private int masaId;
        private double tutar;
        private String odemeYontemi;

        // Getter & Setter'lar (Thymeleaf için zorunludur)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getMasaId() { return masaId; }
        public void setMasaId(int masaId) { this.masaId = masaId; }
        public double getTutar() { return tutar; }
        public void setTutar(double tutar) { this.tutar = tutar; }
        public String getOdemeYontemi() { return odemeYontemi; }
        public void setOdemeYontemi(String odemeYontemi) { this.odemeYontemi = odemeYontemi; }

        // Formu asıl Odeme modeline çeviren metot
        public Odeme toOdeme() {
            return new Odeme.OdemeBuilder()
                    .setId(this.id)
                    .setMasaId(this.masaId)
                    .setTutar(this.tutar)
                    .setOdemeYontemi(this.odemeYontemi)
                    .build();
        }
    }
}