package com.restoran.controller;

import com.restoran.model.Restaurant;
import com.restoran.facade.RestaurantFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class WebRestaurantController {

    private final RestaurantFacade restaurantFacade;

    // 1. Constructor Injection (Güvenli Yöntem)
    public WebRestaurantController(RestaurantFacade restaurantFacade) {
        this.restaurantFacade = restaurantFacade;
    }

    // --- LİSTELEME ---
    @GetMapping("/list")
    public String listRestaurant(Model model) {
        List<Restaurant> restaurants = restaurantFacade.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);

        model.addAttribute("page", "restaurant");
        model.addAttribute("content", "restaurant/list");
        model.addAttribute("pageTitle", "Restaurant Listesi");
        return "index";
    }

    // --- EKLEME SAYFASI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Form bağlama işlemi için DTO (Form sınıfı) gönderiyoruz
        model.addAttribute("restaurantForm", new RestaurantForm());

        model.addAttribute("page", "restaurant");
        model.addAttribute("content", "restaurant/create");
        model.addAttribute("pageTitle", "Yeni Restaurant Oluştur");
        return "index";
    }

    // --- KAYDETME İŞLEMİ ---
    @PostMapping("/save")
    public String createRestaurant(@ModelAttribute("restaurantForm") RestaurantForm restaurantForm) {
        // Form verisini Immutable Model'e çeviriyoruz
        Restaurant yeniRestaurant = restaurantForm.toRestaurant();

        restaurantFacade.createRestaurant(yeniRestaurant);
        return "redirect:/restaurant/list";
    }

    // --- DÜZENLEME SAYFASI ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Restaurant existing = restaurantFacade.getRestaurantById(id);

        // Mevcut Immutable nesneyi, Form sınıfına (Mutable) aktarıyoruz
        RestaurantForm form = new RestaurantForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setAddress(existing.getAddress());

        model.addAttribute("restaurantForm", form); // "restaurant" yerine "restaurantForm"

        model.addAttribute("page", "restaurant");
        model.addAttribute("content", "restaurant/update");
        model.addAttribute("pageTitle", "Restaurant Güncelle");
        return "index";
    }

    // --- GÜNCELLEME İŞLEMİ ---
    @PostMapping("/update/{id}")
    public String updateRestaurant(@PathVariable int id, @ModelAttribute("restaurantForm") RestaurantForm restaurantForm) {
        // ID'yi garanti altına alıyoruz
        restaurantForm.setId(id);

        // Form verisini modele çevirip güncelliyoruz
        Restaurant guncellenecekRestaurant = restaurantForm.toRestaurant();
        restaurantFacade.updateRestaurant(id, guncellenecekRestaurant);

        return "redirect:/restaurant/list";
    }

    // --- SİLME İŞLEMİ ---
    @GetMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable int id) {
        restaurantFacade.deleteRestaurant(id);
        return "redirect:/restaurant/list";
    }

    // =========================================================================
    // INNER CLASS: Form Binding için Geçici Sınıf (DTO)
    // =========================================================================
    public static class RestaurantForm {
        private int id;
        private String name;
        private String address;

        // Getter ve Setter'lar (Spring Form Binding için şarttır)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        // Form nesnesini Immutable Restaurant modeline dönüştürür
        public Restaurant toRestaurant() {
            return new Restaurant.RestaurantBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setAddress(this.address)
                    .build();
        }
    }
}