package com.restoran.controller;

import com.restoran.facade.YemekFacade;
import com.restoran.model.Yemek;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/yemek")
public class WebYemekController {

    private final YemekFacade yemekFacade;

    public WebYemekController(YemekFacade yemekFacade) {
        this.yemekFacade = yemekFacade;
    }

    @GetMapping("/list")
    public String listYemek(Model model) {
        model.addAttribute("yemekler", yemekFacade.getAllYemekler());
        model.addAttribute("page", "yemek");
        model.addAttribute("content", "yemek/list");
        model.addAttribute("pageTitle", "Yemek Listesi");
        return "index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("yemekForm", new YemekForm());
        model.addAttribute("page", "yemek");
        model.addAttribute("content", "yemek/create");
        return "index";
    }

    @PostMapping("/save")
    public String saveYemek(@ModelAttribute("yemekForm") YemekForm form) {
        yemekFacade.createYemek(form.toYemek());
        return "redirect:/yemek/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Yemek mevcut = yemekFacade.getYemekById(id);
        YemekForm form = new YemekForm();
        form.setId(mevcut.getId());
        form.setName(mevcut.getName());
        form.setMenuId(mevcut.getMenuId());

        model.addAttribute("yemekForm", form);
        model.addAttribute("page", "yemek");
        model.addAttribute("content", "yemek/update");
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateYemek(@PathVariable int id, @ModelAttribute("yemekForm") YemekForm form) {
        yemekFacade.updateYemek(id, form.toYemek());
        return "redirect:/yemek/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteYemek(@PathVariable int id) {
        yemekFacade.deleteYemek(id);
        return "redirect:/yemek/list";
    }

    // DTO Form Class
    public static class YemekForm {
        private int id;
        private String name;
        private int menuId;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getMenuId() { return menuId; }
        public void setMenuId(int menuId) { this.menuId = menuId; }

        public Yemek toYemek() {
            return new Yemek.YemekBuilder()
                    .setId(this.id)
                    .setName(this.name)
                    .setMenuId(this.menuId)
                    .build();
        }
    }
}