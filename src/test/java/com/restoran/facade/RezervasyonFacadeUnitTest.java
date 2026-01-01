package com.restoran.facade;

import com.restoran.model.Rezervasyon;
import com.restoran.repository.RezervasyonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RezervasyonFacadeUnitTest {

    @Mock
    private RezervasyonService rezervasyonService;

    @InjectMocks
    private RezervasyonFacade rezervasyonFacade;

    @Test
    void getAllRezervasyonlar_shouldDelegateToService() {
        when(rezervasyonService.getAllRezervasyonlar()).thenReturn(List.of());

        List<Rezervasyon> result = rezervasyonFacade.getAllRezervasyonlar();

        assertNotNull(result);
        verify(rezervasyonService, times(1)).getAllRezervasyonlar();
        verifyNoMoreInteractions(rezervasyonService);
    }

    @Test
    void createRezervasyon_shouldDelegateToService() {
        Rezervasyon rezervasyon = new Rezervasyon.RezervasyonBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setSaat("19:30")
                .setUserId(1)
                .setMasaId(2)
                .build();

        when(rezervasyonService.createRezervasyon(rezervasyon)).thenReturn("Rezervasyon başarıyla eklendi.");

        String result = rezervasyonFacade.createRezervasyon(rezervasyon);

        assertEquals("Rezervasyon başarıyla eklendi.", result);
        verify(rezervasyonService, times(1)).createRezervasyon(rezervasyon);
        verifyNoMoreInteractions(rezervasyonService);
    }

    @Test
    void getRezervasyonById_shouldDelegateToService() {
        Rezervasyon expected = new Rezervasyon.RezervasyonBuilder()
                .setId(5)
                .setTarih("2026-01-01")
                .setSaat("20:00")
                .setUserId(3)
                .setMasaId(4)
                .build();

        when(rezervasyonService.getRezervasyonById(5)).thenReturn(expected);

        Rezervasyon result = rezervasyonFacade.getRezervasyonById(5);

        assertEquals(expected, result);
        verify(rezervasyonService, times(1)).getRezervasyonById(5);
        verifyNoMoreInteractions(rezervasyonService);
    }

    @Test
    void updateRezervasyon_shouldOverrideIdAndPassBuiltEntityToService() {
        int idFromUrl = 10;
        Rezervasyon yeniVeriler = new Rezervasyon.RezervasyonBuilder()
                .setId(999)
                .setTarih("2026-01-02")
                .setSaat("21:15")
                .setUserId(7)
                .setMasaId(8)
                .build();

        when(rezervasyonService.updateRezervasyon(eq(idFromUrl), any(Rezervasyon.class)))
                .thenReturn("Rezervasyon başarıyla güncellendi.");

        String result = rezervasyonFacade.updateRezervasyon(idFromUrl, yeniVeriler);

        assertEquals("Rezervasyon başarıyla güncellendi.", result);

        ArgumentCaptor<Rezervasyon> captor = ArgumentCaptor.forClass(Rezervasyon.class);
        verify(rezervasyonService, times(1)).updateRezervasyon(eq(idFromUrl), captor.capture());
        Rezervasyon passed = captor.getValue();

        assertEquals(idFromUrl, passed.getId());
        assertEquals("2026-01-02", passed.getTarih());
        assertEquals("21:15", passed.getSaat());
        assertEquals(7, passed.getUserId());
        assertEquals(8, passed.getMasaId());
    }

    @Test
    void deleteRezervasyon_shouldDelegateToService() {
        when(rezervasyonService.deleteRezervasyon(3)).thenReturn("Rezervasyon başarıyla silindi!");

        String result = rezervasyonFacade.deleteRezervasyon(3);

        assertEquals("Rezervasyon başarıyla silindi!", result);
        verify(rezervasyonService, times(1)).deleteRezervasyon(3);
        verifyNoMoreInteractions(rezervasyonService);
    }
}
