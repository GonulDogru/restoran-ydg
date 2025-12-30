package com.restoran.controller;

import com.restoran.model.Kullanici;
import com.restoran.facade.KullaniciFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class WebKullaniciController {

    private final KullaniciFacade kullaniciFacade;

    public WebKullaniciController(KullaniciFacade kullaniciFacade) {
        this.kullaniciFacade = kullaniciFacade;
    }

    @GetMapping("/list")
    public String listKullanici(Model model) {
        List<Kullanici> kullanicilar = kullaniciFacade.getAllKullanicilar();
        model.addAttribute("kullanicilar", kullanicilar);
        model.addAttribute("page", "kullanici");
        model.addAttribute("content", "users/list");
        model.addAttribute("pageTitle", "Kullanıcı Listesi");
        return "index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("kullaniciForm", new KullaniciForm());
        model.addAttribute("page", "kullanici");
        model.addAttribute("content", "users/create");
        model.addAttribute("pageTitle", "Yeni Kullanıcı Oluştur");
        return "index";
    }

    @PostMapping("/save")
    public String createKullanici(@ModelAttribute("kullaniciForm") KullaniciForm kullaniciForm) {
        kullaniciFacade.createKullanici(kullaniciForm.toKullanici());
        return "redirect:/users/list"; // redirect yolu düzeltildi
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Kullanici existing = kullaniciFacade.getKullaniciById(id);

        KullaniciForm form = new KullaniciForm();
        form.setId(existing.getId());
        form.setUsername(existing.getUsername());
        form.setPassword(existing.getPassword());
        form.setAdres(existing.getAdres()); // Eşleşme sağlandı
        form.setTelefon(existing.getTelefon()); // Eşleşme sağlandı

        model.addAttribute("kullaniciForm", form);
        model.addAttribute("page", "kullanici");
        model.addAttribute("content", "users/update");
        model.addAttribute("pageTitle", "Kullanıcı Güncelle");
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateKullanici(@PathVariable int id, @ModelAttribute("kullaniciForm") KullaniciForm kullaniciForm) {
        kullaniciForm.setId(id);
        kullaniciFacade.updateKullanici(id, kullaniciForm.toKullanici());
        return "redirect:/users/list"; // redirect yolu düzeltildi
    }

    @GetMapping("/delete/{id}")
    public String deleteKullanici(@PathVariable int id) {
        kullaniciFacade.deleteKullanici(id);
        return "redirect:/users/list";
    }

    // =========================================================================
    // INNER CLASS: DTO (Değişken isimleri HTML ile uyumlu hale getirildi)
    // =========================================================================
    public static class KullaniciForm {
        private int id;
        private String username;
        private String password;
        private String adres;   // address -> adres oldu
        private String telefon; // phone -> telefon oldu

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getAdres() { return adres; }
        public void setAdres(String adres) { this.adres = adres; }

        public String getTelefon() { return telefon; }
        public void setTelefon(String telefon) { this.telefon = telefon; }

        public Kullanici toKullanici() {
            return new Kullanici.KullaniciBuilder()
                    .setId(this.id)
                    .setUsername(this.username)
                    .setPassword(this.password)
                    .setAdres(this.adres)
                    .setTelefon(this.telefon)
                    .build();
        }
    }
}