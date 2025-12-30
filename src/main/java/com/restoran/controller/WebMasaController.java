package com.restoran.controller;

import com.restoran.facade.MasaFacade;
import com.restoran.model.Masa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/masa") // Artık /api/masalar değil, tarayıcı için /masa
public class WebMasaController {

    private final MasaFacade masaFacade;

    public WebMasaController(MasaFacade masaFacade) {
        this.masaFacade = masaFacade;
    }

    @GetMapping("/list")
    public String listMasalar(Model model) {
        model.addAttribute("masalar", masaFacade.getAllMasalar());
        model.addAttribute("content", "masa/list");
        model.addAttribute("page", "masa");
        return "index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("masa", new Masa());
        model.addAttribute("content", "masa/create");
        return "index";
    }

    @PostMapping("/save")
    public String saveMasa(@ModelAttribute("masa") Masa masa) {
        masaFacade.createMasa(masa);
        return "redirect:/masa/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("masa", masaFacade.getMasaById(id));
        model.addAttribute("content", "masa/update");
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateMasa(@PathVariable int id, @ModelAttribute("masa") Masa masa) {
        masaFacade.updateMasa(id, masa);
        return "redirect:/masa/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMasa(@PathVariable int id) {
        masaFacade.deleteMasa(id);
        return "redirect:/masa/list";
    }
}