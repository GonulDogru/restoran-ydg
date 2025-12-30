package com.restoran.facade;

import com.restoran.decorator.*;
import com.restoran.model.Menu;
import com.restoran.repository.MenuService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MenuFacade {

    private final MenuService menuService;

    public MenuFacade(MenuService menuService) {
        this.menuService = menuService;
    }

    // --- Veritabanı İşlemleri ---
    public List<Menu> getAllMenuler() { return menuService.getAllMenuler(); }
    public Menu getMenuById(int id) { return menuService.getMenuById(id); }
    public void createMenu(Menu menu) { menuService.createMenu(menu); }
    public void updateMenu(int id, Menu menu) { menuService.updateMenu(id, menu); }
    public void deleteMenu(int id) { menuService.deleteMenu(id); }

    // --- Dekorasyon İşlemi (Mevcut kodunuz korundu) ---
    public Yemek ozelSiparisOlustur(String anaYemekIsmi, double temelFiyat, boolean peynirOlsun, boolean sosOlsun) {
        Yemek siparis = new AnaYemek(anaYemekIsmi, temelFiyat);
        if (peynirOlsun) siparis = new EkstraPeynir(siparis);
        if (sosOlsun) siparis = new EkstraSos(siparis);
        return siparis;
    }
}