package com.restoran.facade;

import com.restoran.model.Icecek;
import com.restoran.repository.IcecekService;
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
class IcecekFacadeUnitTest {

    @Mock
    private IcecekService icecekService;

    @InjectMocks
    private IcecekFacade icecekFacade;

    @Test
    void createIcecek_shouldDelegateToService() {
        Icecek icecek = new Icecek.IcecekBuilder().setId(0).setName("Kola").setMenuId(1).build();
        when(icecekService.createIcecek(icecek)).thenReturn("İçecek başarıyla eklendi.");

        String result = icecekFacade.createIcecek(icecek);

        assertEquals("İçecek başarıyla eklendi.", result);
        verify(icecekService, times(1)).createIcecek(icecek);
        verifyNoMoreInteractions(icecekService);
    }

    @Test
    void getAllIcecekler_shouldDelegateToService() {
        when(icecekService.getAllIcecekler()).thenReturn(List.of());

        List<Icecek> result = icecekFacade.getAllIcecekler();

        assertNotNull(result);
        verify(icecekService, times(1)).getAllIcecekler();
        verifyNoMoreInteractions(icecekService);
    }

    @Test
    void getIcecekById_shouldDelegateToService() {
        Icecek expected = new Icecek.IcecekBuilder().setId(1).setName("Ayran").setMenuId(2).build();
        when(icecekService.getIcecekById(1)).thenReturn(expected);

        Icecek result = icecekFacade.getIcecekById(1);

        assertEquals(expected, result);
        verify(icecekService, times(1)).getIcecekById(1);
        verifyNoMoreInteractions(icecekService);
    }

    @Test
    void updateIcecek_shouldOverrideIdWithParam_andDelegateToService() {
        int idFromUrl = 10;

        Icecek yeniVeriler = new Icecek.IcecekBuilder()
                .setId(999) // facade bunu dikkate almamalı
                .setName("Fanta")
                .setMenuId(7)
                .build();

        when(icecekService.updateIcecek(eq(idFromUrl), any(Icecek.class)))
                .thenReturn("İçecek başarıyla güncellendi.");

        String result = icecekFacade.updateIcecek(idFromUrl, yeniVeriler);

        assertEquals("İçecek başarıyla güncellendi.", result);

        ArgumentCaptor<Icecek> captor = ArgumentCaptor.forClass(Icecek.class);
        verify(icecekService, times(1)).updateIcecek(eq(idFromUrl), captor.capture());

        Icecek passed = captor.getValue();
        assertEquals(idFromUrl, passed.getId());
        assertEquals("Fanta", passed.getName());
        assertEquals(7, passed.getMenuId());

        verifyNoMoreInteractions(icecekService);
    }

    @Test
    void deleteIcecek_shouldDelegateToService() {
        when(icecekService.deleteIcecek(3)).thenReturn("İçecek başarıyla silindi!");

        String result = icecekFacade.deleteIcecek(3);

        assertEquals("İçecek başarıyla silindi!", result);
        verify(icecekService, times(1)).deleteIcecek(3);
        verifyNoMoreInteractions(icecekService);
    }
}
