package com.restoran.controller;

import com.restoran.model.Icecek;
import com.restoran.facade.IcecekFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/icecek")
public class WebIcecekController {

    private final IcecekFacade icecekFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebIcecekController(IcecekFacade icecekFacade) {
        this.icecekFacade = icecekFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listIcecek(Model model) {
        List<Icecek> icecekler = icecekFacade.getAllIcecekler();
        model.addAttribute("icecekler", icecekler);

        model.addAttribute("page", "icecek");
        model.addAttribute("content", "icecek/list");
        model.addAttribute("pageTitle", "İçecek Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("icecekForm", new IcecekForm());

        model.addAttribute("page", "icecek");
        model.addAttribute("content", "icecek/create");
        model.addAttribute("pageTitle", "Yeni İçecek Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createIcecek(@ModelAttribute("icecekForm") IcecekForm icecekForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Icecek yeniIcecek = icecekForm.toIcecek();

        icecekFacade.createIcecek(yeniIcecek);
        return "redirect:/icecek/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Icecek existing = icecekFacade.getIcecekById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        IcecekForm form = new IcecekForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setMenuId(existing.getMenuId());

        model.addAttribute("icecekForm", form); // "icecek" yerine "icecekForm"

        model.addAttribute("page", "icecek");
        model.addAttribute("content", "icecek/update");
        model.addAttribute("pageTitle", "İçecek Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateIcecek(@PathVariable int id, @ModelAttribute("icecekForm") IcecekForm icecekForm) {
        // ID'yi garanti altına alıyoruz
        icecekForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Icecek guncellenecekIcecek = icecekForm.toIcecek();
        icecekFacade.updateIcecek(id, guncellenecekIcecek);

        return "redirect:/icecek/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteIcecek(@PathVariable int id) {
        icecekFacade.deleteIcecek(id);
        return "redirect:/icecek/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class IcecekForm {
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

        // Form nesnesini Immutable Icecek modeline dönüştürür
        public Icecek toIcecek() {
            return new Icecek.IcecekBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setMenuId(this.menuId)
                    .build();
        }
    }
}