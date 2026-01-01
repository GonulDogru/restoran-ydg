package com.restoran.facade;

import com.restoran.model.Siparis;
import com.restoran.repository.SiparisService;
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
class SiparisFacadeUnitTest {

    @Mock
    private SiparisService siparisService;

    @InjectMocks
    private SiparisFacade siparisFacade;

    @Test
    void getAllSiparisler_shouldDelegateToService() {
        when(siparisService.getAllSiparisler()).thenReturn(List.of());

        List<Siparis> result = siparisFacade.getAllSiparisler();

        assertNotNull(result);
        verify(siparisService, times(1)).getAllSiparisler();
        verifyNoMoreInteractions(siparisService);
    }

    @Test
    void createSiparis_shouldDelegateToService() {
        Siparis siparis = new Siparis.SiparisBuilder()
                .setId(0)
                .setTarih("2026-01-01")
                .setAmount(150.0f)
                .setUserId(1)
                .setMasaId(2)
                .build();

        when(siparisService.createSiparis(siparis)).thenReturn("Sipariş başarıyla eklendi.");

        String result = siparisFacade.createSiparis(siparis);

        assertEquals("Sipariş başarıyla eklendi.", result);
        verify(siparisService, times(1)).createSiparis(siparis);
        verifyNoMoreInteractions(siparisService);
    }

    @Test
    void getSiparisById_shouldDelegateToService() {
        Siparis expected = new Siparis.SiparisBuilder()
                .setId(5)
                .setTarih("2026-01-01")
                .setAmount(10.0f)
                .setUserId(3)
                .setMasaId(4)
                .build();

        when(siparisService.getSiparisById(5)).thenReturn(expected);

        Siparis result = siparisFacade.getSiparisById(5);

        assertEquals(expected, result);
        verify(siparisService, times(1)).getSiparisById(5);
        verifyNoMoreInteractions(siparisService);
    }

    @Test
    void updateSiparis_shouldOverrideIdAndPassBuiltEntityToService() {
        int idFromUrl = 10;
        Siparis yeniVeriler = new Siparis.SiparisBuilder()
                .setId(999) // facade URL'den gelen id'yi kullanmalı
                .setTarih("2026-01-02")
                .setAmount(250.5f)
                .setUserId(7)
                .setMasaId(8)
                .build();

        when(siparisService.updateSiparis(eq(idFromUrl), any(Siparis.class)))
                .thenReturn("Sipariş başarıyla güncellendi.");

        String result = siparisFacade.updateSiparis(idFromUrl, yeniVeriler);

        assertEquals("Sipariş başarıyla güncellendi.", result);

        ArgumentCaptor<Siparis> captor = ArgumentCaptor.forClass(Siparis.class);
        verify(siparisService, times(1)).updateSiparis(eq(idFromUrl), captor.capture());
        Siparis passed = captor.getValue();

        assertEquals(idFromUrl, passed.getId());
        assertEquals("2026-01-02", passed.getTarih());
        assertEquals(250.5f, passed.getAmount());
        assertEquals(7, passed.getUserId());
        assertEquals(8, passed.getMasaId());
    }

    @Test
    void deleteSiparis_shouldDelegateToService() {
        when(siparisService.deleteSiparis(3)).thenReturn("Sipariş başarıyla silindi!");

        String result = siparisFacade.deleteSiparis(3);

        assertEquals("Sipariş başarıyla silindi!", result);
        verify(siparisService, times(1)).deleteSiparis(3);
        verifyNoMoreInteractions(siparisService);
    }
}
