package com.restoran.facade;

import com.restoran.model.Yemek;
import com.restoran.repository.YemekService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YemekFacadeUnitTest {

    @Mock
    private YemekService yemekService;

    @InjectMocks
    private YemekFacade yemekFacade;

    @Test
    void getAllYemekler_shouldDelegateToService() {
        when(yemekService.getAllYemekler()).thenReturn(List.of());

        List<Yemek> result = yemekFacade.getAllYemekler();

        assertNotNull(result);
        verify(yemekService, times(1)).getAllYemekler();
        verifyNoMoreInteractions(yemekService);
    }

    @Test
    void getYemekById_shouldDelegateToService() {
        Yemek expected = new Yemek.YemekBuilder().setId(1).setName("Adana").setMenuId(2).build();
        when(yemekService.getYemekById(1)).thenReturn(expected);

        Yemek result = yemekFacade.getYemekById(1);

        assertEquals(expected, result);
        verify(yemekService, times(1)).getYemekById(1);
        verifyNoMoreInteractions(yemekService);
    }

    @Test
    void createYemek_shouldDelegateToService() {
        Yemek yemek = new Yemek.YemekBuilder().setId(0).setName("Lahmacun").setMenuId(1).build();

        yemekFacade.createYemek(yemek);

        verify(yemekService, times(1)).createYemek(yemek);
        verifyNoMoreInteractions(yemekService);
    }

    @Test
    void updateYemek_shouldDelegateToService() {
        int id = 7;
        Yemek yemek = new Yemek.YemekBuilder().setId(id).setName("Guncel").setMenuId(3).build();

        yemekFacade.updateYemek(id, yemek);

        verify(yemekService, times(1)).updateYemek(id, yemek);
        verifyNoMoreInteractions(yemekService);
    }

    @Test
    void deleteYemek_shouldDelegateToService() {
        yemekFacade.deleteYemek(9);

        verify(yemekService, times(1)).deleteYemek(9);
        verifyNoMoreInteractions(yemekService);
    }
}
