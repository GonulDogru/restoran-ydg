package com.restoran.facade;

import com.restoran.model.Tatli;
import com.restoran.repository.TatliService;
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
class TatliFacadeUnitTest {

    @Mock
    private TatliService tatliService;

    @InjectMocks
    private TatliFacade tatliFacade;

    @Test
    void createTatli_shouldDelegateToService() {
        Tatli tatli = new Tatli.TatliBuilder().setId(0).setName("Künefe").setMenuId(1).build();
        when(tatliService.createTatli(tatli)).thenReturn("Tatlı başarıyla eklendi.");

        String result = tatliFacade.createTatli(tatli);

        assertEquals("Tatlı başarıyla eklendi.", result);
        verify(tatliService, times(1)).createTatli(tatli);
        verifyNoMoreInteractions(tatliService);
    }

    @Test
    void getAllTatlilar_shouldDelegateToService() {
        when(tatliService.getAllTatlilar()).thenReturn(List.of());

        List<Tatli> result = tatliFacade.getAllTatlilar();

        assertNotNull(result);
        verify(tatliService, times(1)).getAllTatlilar();
        verifyNoMoreInteractions(tatliService);
    }

    @Test
    void getTatliById_shouldDelegateToService() {
        Tatli expected = new Tatli.TatliBuilder().setId(1).setName("Baklava").setMenuId(2).build();
        when(tatliService.getTatliById(1)).thenReturn(expected);

        Tatli result = tatliFacade.getTatliById(1);

        assertEquals(expected, result);
        verify(tatliService, times(1)).getTatliById(1);
        verifyNoMoreInteractions(tatliService);
    }

    @Test
    void updateTatli_shouldOverrideIdWithParam_andDelegateToService() {
        int idFromUrl = 10;

        Tatli yeniVeriler = new Tatli.TatliBuilder()
                .setId(999) // facade bunu dikkate almamalı
                .setName("Sütlaç")
                .setMenuId(7)
                .build();

        when(tatliService.updateTatli(eq(idFromUrl), any(Tatli.class)))
                .thenReturn("Tatlı başarıyla güncellendi.");

        String result = tatliFacade.updateTatli(idFromUrl, yeniVeriler);

        assertEquals("Tatlı başarıyla güncellendi.", result);

        ArgumentCaptor<Tatli> captor = ArgumentCaptor.forClass(Tatli.class);
        verify(tatliService, times(1)).updateTatli(eq(idFromUrl), captor.capture());

        Tatli passed = captor.getValue();
        assertEquals(idFromUrl, passed.getId());
        assertEquals("Sütlaç", passed.getName());
        assertEquals(7, passed.getMenuId());

        verifyNoMoreInteractions(tatliService);
    }

    @Test
    void deleteTatli_shouldDelegateToService() {
        when(tatliService.deleteTatli(3)).thenReturn("Tatlı başarıyla silindi!");

        String result = tatliFacade.deleteTatli(3);

        assertEquals("Tatlı başarıyla silindi!", result);
        verify(tatliService, times(1)).deleteTatli(3);
        verifyNoMoreInteractions(tatliService);
    }
}
