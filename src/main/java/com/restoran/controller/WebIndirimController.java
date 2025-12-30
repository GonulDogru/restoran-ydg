package com.restoran.controller;

import com.restoran.model.Indirim;
import com.restoran.facade.IndirimFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/indirim")
public class WebIndirimController {

    private final IndirimFacade indirimFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebIndirimController(IndirimFacade indirimFacade) {
        this.indirimFacade = indirimFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listIndirim(Model model) {
        List<Indirim> indirimler = indirimFacade.getAllIndirimler();
        model.addAttribute("indirimler", indirimler);

        model.addAttribute("page", "indirim");
        model.addAttribute("content", "indirim/list");
        model.addAttribute("pageTitle", "İndirim Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("indirimForm", new IndirimForm());

        model.addAttribute("page", "indirim");
        model.addAttribute("content", "indirim/create");
        model.addAttribute("pageTitle", "Yeni İndirim Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createIndirim(@ModelAttribute("indirimForm") IndirimForm indirimForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Indirim yeniIndirim = indirimForm.toIndirim();

        indirimFacade.createIndirim(yeniIndirim);
        return "redirect:/indirim/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Indirim existing = indirimFacade.getIndirimById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        IndirimForm form = new IndirimForm();
        form.setId(existing.getId());
        form.setAmount(existing.getAmount());
        form.setKullaniciId(existing.getKullaniciId());

        model.addAttribute("indirimForm", form); // "indirim" yerine "indirimForm"

        model.addAttribute("page", "indirim");
        model.addAttribute("content", "indirim/update");
        model.addAttribute("pageTitle", "İndirim Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateIndirim(@PathVariable int id, @ModelAttribute("indirimForm") IndirimForm indirimForm) {
        // ID'yi garanti altına alıyoruz
        indirimForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Indirim guncellenecekIndirim = indirimForm.toIndirim();
        indirimFacade.updateIndirim(id, guncellenecekIndirim);

        return "redirect:/indirim/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteIndirim(@PathVariable int id) {
        indirimFacade.deleteIndirim(id);
        return "redirect:/indirim/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class IndirimForm {
        private int id;
        private BigDecimal amount; // Modelde BigDecimal kullandığınızı varsayıyorum
        private int kullaniciId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public int getKullaniciId() { return kullaniciId; }
        public void setKullaniciId(int kullaniciId) { this.kullaniciId = kullaniciId; }

        // Form nesnesini Immutable Indirim modeline dönüştürür
        public Indirim toIndirim() {
            return new Indirim.IndirimBuilder()
                    .setId(this.id)
                    .setAmount(this.amount)
                    .setKullaniciId(this.kullaniciId)
                    .build();
        }
    }
}