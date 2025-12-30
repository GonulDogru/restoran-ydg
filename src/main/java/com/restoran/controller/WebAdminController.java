package com.restoran.controller;

import com.restoran.model.Admin;
import com.restoran.facade.AdminFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class WebAdminController {

    private final AdminFacade adminFacade;

    // 1. Constructor Injection
    public WebAdminController(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listAdmin(Model model) {
        List<Admin> adminler = adminFacade.getAllAdminler();
        model.addAttribute("adminler", adminler);

        // Thymeleaf Fragment Ayarları
        model.addAttribute("page", "admin");
        model.addAttribute("content", "admin/list");
        model.addAttribute("pageTitle", "Admin Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form için boş bir AdminForm gönderiyoruz (Model değil, Form sınıfı)
        model.addAttribute("adminForm", new AdminForm());

        model.addAttribute("page", "admin");
        model.addAttribute("content", "admin/create");
        model.addAttribute("pageTitle", "Yeni Admin Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createAdmin(@ModelAttribute("adminForm") AdminForm adminForm) {
        // Formdan gelen mutable (değişebilir) veriyi, Immutable (değişmez) modele çeviriyoruz
        Admin yeniAdmin = adminForm.toAdmin();

        adminFacade.createAdmin(yeniAdmin);
        return "redirect:/admin/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Admin existing = adminFacade.getAdminById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) çevirip sayfaya gönderiyoruz
        AdminForm form = new AdminForm();
        form.setId(existing.getId());
        form.setUsername(existing.getUsername());
        form.setPassword(existing.getPassword());
        form.setSuperadminId(existing.getSuperadminId());

        model.addAttribute("adminForm", form); // "admin" değil "adminForm" olarak gönderiyoruz

        model.addAttribute("page", "admin");
        model.addAttribute("content", "admin/update");
        model.addAttribute("pageTitle", "Admin Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable int id, @ModelAttribute("adminForm") AdminForm adminForm) {
        // ID'yi path'den alıp forma ekliyoruz (Garanti olsun diye)
        adminForm.setId(id);

        // Form verisini Model'e çevirip Facade'a gönderiyoruz
        Admin guncellenecekAdmin = adminForm.toAdmin();
        adminFacade.updateAdmin(id, guncellenecekAdmin);

        return "redirect:/admin/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable int id) {
        adminFacade.deleteAdmin(id);
        return "redirect:/admin/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // Modelimiz Immutable olduğu için, HTML formu ile Model arasında köprü kurar.
    // =========================================================================
    public static class AdminForm {
        private int id;
        private String username;
        private String password;
        private int superadminId;

        // Getter ve Setter'lar (Spring Form Binding için gereklidir)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public int getSuperadminId() { return superadminId; }
        public void setSuperadminId(int superadminId) { this.superadminId = superadminId; }

        // Form verisini gerçek Immutable Modele dönüştüren metot
        public Admin toAdmin() {
            return new Admin.AdminBuilder()
                    .setId(this.id)
                    .setUsername(this.username)
                    .setPassword(this.password)
                    .setSuperadminId(this.superadminId)
                    .build();
        }
    }
}