package com.restoran.controller;

import com.restoran.model.Superadmin;
import com.restoran.facade.SuperadminFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/superadmin")
public class WebSuperadminController {

    private final SuperadminFacade superadminFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebSuperadminController(SuperadminFacade superadminFacade) {
        this.superadminFacade = superadminFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listSuperadmin(Model model) {
        List<Superadmin> superadminler = superadminFacade.getAllSuperadminler();
        model.addAttribute("superadminler", superadminler);

        model.addAttribute("page", "superadmin");
        model.addAttribute("content", "superadmin/list");
        model.addAttribute("pageTitle", "Superadmin Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("superadminForm", new SuperadminForm());

        model.addAttribute("page", "superadmin");
        model.addAttribute("content", "superadmin/create");
        model.addAttribute("pageTitle", "Yeni Superadmin Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createSuperadmin(@ModelAttribute("superadminForm") SuperadminForm superadminForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Superadmin yeniSuperadmin = superadminForm.toSuperadmin();

        superadminFacade.createSuperadmin(yeniSuperadmin);
        return "redirect:/superadmin/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Superadmin existing = superadminFacade.getSuperadminById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        SuperadminForm form = new SuperadminForm();
        form.setId(existing.getId());
        form.setUsername(existing.getUsername());
        form.setPassword(existing.getPassword());

        model.addAttribute("superadminForm", form); // "superadmin" yerine "superadminForm"

        model.addAttribute("page", "superadmin");
        model.addAttribute("content", "superadmin/update");
        model.addAttribute("pageTitle", "Superadmin Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateSuperadmin(@PathVariable int id, @ModelAttribute("superadminForm") SuperadminForm superadminForm) {
        // ID'yi garanti altına alıyoruz
        superadminForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Superadmin guncellenecekSuperadmin = superadminForm.toSuperadmin();
        superadminFacade.updateSuperadmin(id, guncellenecekSuperadmin);

        return "redirect:/superadmin/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteSuperadmin(@PathVariable int id) {
        superadminFacade.deleteSuperadmin(id);
        return "redirect:/superadmin/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class SuperadminForm {
        private int id;
        private String username;
        private String password;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        // Form nesnesini Immutable Superadmin modeline dönüştürür
        public Superadmin toSuperadmin() {
            return new Superadmin.SuperadminBuilder()
                    .setId(this.id)
                    .setUsername(this.username)
                    .setPassword(this.password)
                    .build();
        }
    }
}