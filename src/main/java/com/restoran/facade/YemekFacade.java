package com.restoran.facade;

import com.restoran.model.Yemek;
import com.restoran.repository.YemekService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class YemekFacade {

    private final YemekService yemekService;

    public YemekFacade(YemekService yemekService) {
        this.yemekService = yemekService;
    }

    public List<Yemek> getAllYemekler() { return yemekService.getAllYemekler(); }
    public Yemek getYemekById(int id) { return yemekService.getYemekById(id); }
    public void createYemek(Yemek yemek) { yemekService.createYemek(yemek); }
    public void updateYemek(int id, Yemek yemek) { yemekService.updateYemek(id, yemek); }
    public void deleteYemek(int id) { yemekService.deleteYemek(id); }
}