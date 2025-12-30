package com.restoran.controller;

import com.restoran.model.Stok;
import com.restoran.facade.StokFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stok")
public class WebStokController {

    private final StokFacade stokFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebStokController(StokFacade stokFacade) {
        this.stokFacade = stokFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listStok(Model model) {
        List<Stok> stoklar = stokFacade.getAllStoklar();
        model.addAttribute("stoklar", stoklar);

        model.addAttribute("page", "stok");
        model.addAttribute("content", "stok/list");
        model.addAttribute("pageTitle", "Stok Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("stokForm", new StokForm());

        model.addAttribute("page", "stok");
        model.addAttribute("content", "stok/create");
        model.addAttribute("pageTitle", "Yeni Stok Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createStok(@ModelAttribute("stokForm") StokForm stokForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Stok yeniStok = stokForm.toStok();

        stokFacade.createStok(yeniStok);
        return "redirect:/stok/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Stok existing = stokFacade.getStokById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        StokForm form = new StokForm();
        form.setId(existing.getId());
        form.setMiktar(existing.getMiktar());
        form.setYemekId(existing.getYemekId());
        form.setIcecekId(existing.getIcecekId());
        form.setTatliId(existing.getTatliId());
        form.setTedarikciId(existing.getTedarikciId());

        model.addAttribute("stokForm", form); // "stok" yerine "stokForm"

        model.addAttribute("page", "stok");
        model.addAttribute("content", "stok/update");
        model.addAttribute("pageTitle", "Stok Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateStok(@PathVariable int id, @ModelAttribute("stokForm") StokForm stokForm) {
        // ID'yi garanti altına alıyoruz
        stokForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Stok guncellenecekStok = stokForm.toStok();
        stokFacade.updateStok(id, guncellenecekStok);

        return "redirect:/stok/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteStok(@PathVariable int id) {
        stokFacade.deleteStok(id);
        return "redirect:/stok/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class StokForm {
        private int id;
        private int miktar;
        private int yemekId;
        private int icecekId;
        private int tatliId;
        private int tedarikciId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getMiktar() { return miktar; }
        public void setMiktar(int miktar) { this.miktar = miktar; }

        public int getYemekId() { return yemekId; }
        public void setYemekId(int yemekId) { this.yemekId = yemekId; }

        public int getIcecekId() { return icecekId; }
        public void setIcecekId(int icecekId) { this.icecekId = icecekId; }

        public int getTatliId() { return tatliId; }
        public void setTatliId(int tatliId) { this.tatliId = tatliId; }

        public int getTedarikciId() { return tedarikciId; }
        public void setTedarikciId(int tedarikciId) { this.tedarikciId = tedarikciId; }

        // Form nesnesini Immutable Stok modeline dönüştürür
        public Stok toStok() {
            return new Stok.StokBuilder()
                    .setId(this.id)
                    .setMiktar(this.miktar)
                    .setYemekId(this.yemekId)
                    .setIcecekId(this.icecekId)
                    .setTatliId(this.tatliId)
                    .setTedarikciId(this.tedarikciId)
                    .build();
        }
    }
}