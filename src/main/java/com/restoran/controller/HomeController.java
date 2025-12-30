package com.restoran.controller;

import com.restoran.facade.MasaFacade;
import com.restoran.facade.SiparisFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/* Gelişmiş Dashboard Örneği */
@Controller
public class HomeController {

    private final SiparisFacade siparisFacade;
    private final MasaFacade masaFacade;

    // Facade'ları Constructor ile enjekte ediyoruz
    public HomeController(SiparisFacade siparisFacade, MasaFacade masaFacade) {
        this.siparisFacade = siparisFacade;
        this.masaFacade = masaFacade;
    }

    @GetMapping({"/", "/home"})
    public String showHomePage(Model model) {
        model.addAttribute("content", "home");
        model.addAttribute("page", "home");
        model.addAttribute("pageTitle", "Gösterge Paneli");

        // Dashboard kartları için veri gönderimi:
        // Not: Bu listelerin size() metodunu alarak sayıları gösterebilirsiniz.
        model.addAttribute("toplamSiparis", siparisFacade.getAllSiparisler().size());
        model.addAttribute("toplamMasa", masaFacade.getAllMasalar().size());

        return "index";
    }
}