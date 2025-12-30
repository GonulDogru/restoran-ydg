package com.restoran.controller;

import com.restoran.model.Iletisim;
import com.restoran.facade.IletisimFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/iletisim")
public class WebIletisimController {

    private final IletisimFacade iletisimFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebIletisimController(IletisimFacade iletisimFacade) {
        this.iletisimFacade = iletisimFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listIletisim(Model model) {
        List<Iletisim> iletisimler = iletisimFacade.getAllIletisimler();
        model.addAttribute("iletisimler", iletisimler);

        model.addAttribute("page", "iletisim");
        model.addAttribute("content", "iletisim/list");
        model.addAttribute("pageTitle", "İletişim Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("iletisimForm", new IletisimForm());

        model.addAttribute("page", "iletisim");
        model.addAttribute("content", "iletisim/create");
        model.addAttribute("pageTitle", "Yeni İletişim Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createIletisim(@ModelAttribute("iletisimForm") IletisimForm iletisimForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Iletisim yeniIletisim = iletisimForm.toIletisim();

        iletisimFacade.createIletisim(yeniIletisim);
        return "redirect:/iletisim/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Iletisim existing = iletisimFacade.getIletisimById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        IletisimForm form = new IletisimForm();
        form.setId(existing.getId());
        form.setUserId(existing.getUserId());
        form.setRestaurantId(existing.getRestaurantId());
        form.setMesaj(existing.getMesaj());

        model.addAttribute("iletisimForm", form); // "iletisim" yerine "iletisimForm"

        model.addAttribute("page", "iletisim");
        model.addAttribute("content", "iletisim/update");
        model.addAttribute("pageTitle", "İletişim Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateIletisim(@PathVariable int id, @ModelAttribute("iletisimForm") IletisimForm iletisimForm) {
        // ID'yi garanti altına alıyoruz
        iletisimForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Iletisim guncellenecekIletisim = iletisimForm.toIletisim();
        iletisimFacade.updateIletisim(id, guncellenecekIletisim);

        return "redirect:/iletisim/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteIletisim(@PathVariable int id) {
        iletisimFacade.deleteIletisim(id);
        return "redirect:/iletisim/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class IletisimForm {
        private int id;
        private int userId;
        private int restaurantId;
        private String mesaj;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public int getRestaurantId() { return restaurantId; }
        public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

        public String getMesaj() { return mesaj; }
        public void setMesaj(String mesaj) { this.mesaj = mesaj; }

        // Form nesnesini Immutable Iletisim modeline dönüştürür
        public Iletisim toIletisim() {
            return new Iletisim.IletisimBuilder()
                    .setId(this.id)
                    .setUserId(this.userId)
                    .setRestaurantId(this.restaurantId)
                    .setMesaj(this.mesaj)
                    .build();
        }
    }
}