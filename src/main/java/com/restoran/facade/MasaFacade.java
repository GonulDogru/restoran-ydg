package com.restoran.facade;

import com.restoran.model.Masa;
import com.restoran.repository.MasaService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MasaFacade {

    private final MasaService masaService;

    public MasaFacade(MasaService masaService) {
        this.masaService = masaService;
    }

    public List<Masa> getAllMasalar() { return masaService.findAll(); }
    public Masa getMasaById(int id) { return masaService.findById(id); }
    public void createMasa(Masa masa) { masaService.save(masa); }
    public void updateMasa(int id, Masa masa) { masaService.update(id, masa); }
    public void deleteMasa(int id) { masaService.delete(id); }
}