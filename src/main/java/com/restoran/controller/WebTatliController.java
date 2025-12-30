package com.restoran.controller;

import com.restoran.model.Tatli;
import com.restoran.facade.TatliFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tatli")
public class WebTatliController {

    private final TatliFacade tatliFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebTatliController(TatliFacade tatliFacade) {
        this.tatliFacade = tatliFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listTatli(Model model) {
        List<Tatli> tatlilar = tatliFacade.getAllTatlilar();
        model.addAttribute("tatlilar", tatlilar);

        model.addAttribute("page", "tatli");
        model.addAttribute("content", "tatli/list");
        model.addAttribute("pageTitle", "Tatlı Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("tatliForm", new TatliForm());

        model.addAttribute("page", "tatli");
        model.addAttribute("content", "tatli/create");
        model.addAttribute("pageTitle", "Yeni Tatlı Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createTatli(@ModelAttribute("tatliForm") TatliForm tatliForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Tatli yeniTatli = tatliForm.toTatli();

        tatliFacade.createTatli(yeniTatli);
        return "redirect:/tatli/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Tatli existing = tatliFacade.getTatliById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        TatliForm form = new TatliForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setMenuId(existing.getMenuId());

        model.addAttribute("tatliForm", form); // "tatli" yerine "tatliForm"

        model.addAttribute("page", "tatli");
        model.addAttribute("content", "tatli/update");
        model.addAttribute("pageTitle", "Tatlı Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateTatli(@PathVariable int id, @ModelAttribute("tatliForm") TatliForm tatliForm) {
        // ID'yi garanti altına alıyoruz
        tatliForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Tatli guncellenecekTatli = tatliForm.toTatli();
        tatliFacade.updateTatli(id, guncellenecekTatli);

        return "redirect:/tatli/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteTatli(@PathVariable int id) {
        tatliFacade.deleteTatli(id);
        return "redirect:/tatli/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class TatliForm {
        private int id;
        private String name;
        private int menuId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getMenuId() { return menuId; }
        public void setMenuId(int menuId) { this.menuId = menuId; }

        // Form nesnesini Immutable Tatli modeline dönüştürür
        public Tatli toTatli() {
            return new Tatli.TatliBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setMenuId(this.menuId)
                    .build();
        }
    }
}