package com.restoran.controller;

import com.restoran.model.Geribildirim;
import com.restoran.facade.GeribildirimFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/geribildirim")
public class WebGeribildirimController {

    private final GeribildirimFacade geribildirimFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebGeribildirimController(GeribildirimFacade geribildirimFacade) {
        this.geribildirimFacade = geribildirimFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listGeribildirim(Model model) {
        List<Geribildirim> geribildirimler = geribildirimFacade.getAllGeribildirimler();
        model.addAttribute("geribildirimler", geribildirimler);

        model.addAttribute("page", "geribildirim");
        model.addAttribute("content", "geribildirim/list");
        model.addAttribute("pageTitle", "Geribildirim Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("geribildirimForm", new GeribildirimForm());

        model.addAttribute("page", "geribildirim");
        model.addAttribute("content", "geribildirim/create");
        model.addAttribute("pageTitle", "Yeni Geribildirim Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createGeribildirim(@ModelAttribute("geribildirimForm") GeribildirimForm geribildirimForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Geribildirim yeniGeribildirim = geribildirimForm.toGeribildirim();

        geribildirimFacade.createGeribildirim(yeniGeribildirim);
        return "redirect:/geribildirim/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Geribildirim existing = geribildirimFacade.getGeribildirimById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        GeribildirimForm form = new GeribildirimForm();
        form.setId(existing.getId());
        form.setYorum(existing.getYorum());
        form.setUserId(existing.getUserId());

        model.addAttribute("geribildirimForm", form); // "geribildirim" yerine "geribildirimForm"

        model.addAttribute("page", "geribildirim");
        model.addAttribute("content", "geribildirim/update");
        model.addAttribute("pageTitle", "Geribildirim Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateGeribildirim(@PathVariable int id, @ModelAttribute("geribildirimForm") GeribildirimForm geribildirimForm) {
        // ID'yi garanti altına alıyoruz
        geribildirimForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Geribildirim guncellenecekGeribildirim = geribildirimForm.toGeribildirim();
        geribildirimFacade.updateGeribildirim(id, guncellenecekGeribildirim);

        return "redirect:/geribildirim/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteGeribildirim(@PathVariable int id) {
        geribildirimFacade.deleteGeribildirim(id);
        return "redirect:/geribildirim/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class GeribildirimForm {
        private int id;
        private String yorum;
        private int userId;

        // Getter ve Setter'lar
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getYorum() { return yorum; }
        public void setYorum(String yorum) { this.yorum = yorum; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        // Form nesnesini Immutable Geribildirim modeline dönüştürür
        public Geribildirim toGeribildirim() {
            return new Geribildirim.GeribildirimBuilder()
                    .setId(this.id)
                    .setYorum(this.yorum)
                    .setUserId(this.userId)
                    .build();
        }
    }
}