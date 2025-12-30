package com.restoran.controller;

import com.restoran.facade.MenuFacade;
import com.restoran.model.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class WebMenuController {

    private final MenuFacade menuFacade;

    public WebMenuController(MenuFacade menuFacade) {
        this.menuFacade = menuFacade;
    }

    // 1. LİSTELEME
    @GetMapping("/list")
    public String listMenus(Model model) {
        List<Menu> menuler = menuFacade.getAllMenuler();
        model.addAttribute("menuler", menuler);
        model.addAttribute("page", "menu");
        model.addAttribute("content", "menu/list");
        model.addAttribute("pageTitle", "Menü Listesi");
        return "index";
    }

    // 2. YENİ EKLEME SAYFASI
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("menuForm", new MenuForm());
        model.addAttribute("page", "menu");
        model.addAttribute("content", "menu/create");
        model.addAttribute("pageTitle", "Yeni Menü Ekle");
        return "index";
    }

    // 3. KAYDETME
    @PostMapping("/save")
    public String saveMenu(@ModelAttribute("menuForm") MenuForm form) {
        menuFacade.createMenu(form.toMenu());
        return "redirect:/menu/list";
    }

    // 4. DÜZENLEME SAYFASI
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Menu mevcut = menuFacade.getMenuById(id);
        MenuForm form = new MenuForm();
        form.setId(mevcut.getId());
        form.setName(mevcut.getName());
        form.setRestaurantId(mevcut.getRestaurantId());

        model.addAttribute("menuForm", form);
        model.addAttribute("page", "menu");
        model.addAttribute("content", "menu/update");
        return "index";
    }

    // 5. GÜNCELLEME
    @PostMapping("/update/{id}")
    public String updateMenu(@PathVariable int id, @ModelAttribute("menuForm") MenuForm form) {
        menuFacade.updateMenu(id, form.toMenu());
        return "redirect:/menu/list";
    }

    // 6. SİLME
    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable int id) {
        menuFacade.deleteMenu(id);
        return "redirect:/menu/list";
    }

    // --- DTO Form Sınıfı ---
    public static class MenuForm {
        private int id;
        private String name;
        private int restaurantId;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getRestaurantId() { return restaurantId; }
        public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

        public Menu toMenu() {
            return new Menu.MenuBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setRestaurantId(this.restaurantId)
                    .build();
        }
    }
}