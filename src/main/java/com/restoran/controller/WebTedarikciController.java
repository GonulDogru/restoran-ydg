package com.restoran.controller;

import com.restoran.model.Tedarikci;
import com.restoran.facade.TedarikciFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tedarikci")
public class WebTedarikciController {

    private final TedarikciFacade tedarikciFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebTedarikciController(TedarikciFacade tedarikciFacade) {
        this.tedarikciFacade = tedarikciFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listTedarikci(Model model) {
        List<Tedarikci> tedarikciler = tedarikciFacade.getAllTedarikciler();
        model.addAttribute("tedarikciler", tedarikciler);

        model.addAttribute("page", "tedarikci");
        model.addAttribute("content", "tedarikci/list");
        model.addAttribute("pageTitle", "Tedarikçi Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("tedarikciForm", new TedarikciForm());

        model.addAttribute("page", "tedarikci");
        model.addAttribute("content", "tedarikci/create");
        model.addAttribute("pageTitle", "Yeni Tedarikçi Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createTedarikci(@ModelAttribute("tedarikciForm") TedarikciForm tedarikciForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Tedarikci yeniTedarikci = tedarikciForm.toTedarikci();

        tedarikciFacade.createTedarikci(yeniTedarikci);
        return "redirect:/tedarikci/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Tedarikci existing = tedarikciFacade.getTedarikciById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        TedarikciForm form = new TedarikciForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setPhone(existing.getPhone());
        form.setRestaurantId(existing.getRestaurantId());

        model.addAttribute("tedarikciForm", form); // "tedarikci" yerine "tedarikciForm"

        model.addAttribute("page", "tedarikci");
        model.addAttribute("content", "tedarikci/update");
        model.addAttribute("pageTitle", "Tedarikçi Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateTedarikci(@PathVariable int id, @ModelAttribute("tedarikciForm") TedarikciForm tedarikciForm) {
        // ID'yi garanti altına alıyoruz
        tedarikciForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Tedarikci guncellenecekTedarikci = tedarikciForm.toTedarikci();
        tedarikciFacade.updateTedarikci(id, guncellenecekTedarikci);

        return "redirect:/tedarikci/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteTedarikci(@PathVariable int id) {
        tedarikciFacade.deleteTedarikci(id);
        return "redirect:/tedarikci/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class TedarikciForm {
        private int id;
        private String name;
        private String phone;
        private int restaurantId;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public int getRestaurantId() { return restaurantId; }
        public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

        // Form nesnesini Immutable Tedarikci modeline dönüştürür
        public Tedarikci toTedarikci() {
            return new Tedarikci.TedarikciBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setPhone(this.phone)
                    .setRestaurantId(this.restaurantId)
                    .build();
        }
    }
}