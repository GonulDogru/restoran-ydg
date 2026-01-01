package com.restoran.facade;

import com.restoran.decorator.Yemek;
import com.restoran.model.Menu;
import com.restoran.repository.MenuService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuFacadeUnitTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuFacade menuFacade;

    @Test
    void getAllMenuler_shouldDelegateToService() {
        List<Menu> expected = List.of(
                new Menu.MenuBuilder().setId(1).setName("Menu1").setRestaurantId(10).build()
        );
        when(menuService.getAllMenuler()).thenReturn(expected);

        List<Menu> result = menuFacade.getAllMenuler();

        assertEquals(expected, result);
        verify(menuService, times(1)).getAllMenuler();
        verifyNoMoreInteractions(menuService);
    }

    @Test
    void getMenuById_shouldDelegateToService() {
        Menu expected = new Menu.MenuBuilder().setId(5).setName("MenuX").setRestaurantId(2).build();
        when(menuService.getMenuById(5)).thenReturn(expected);

        Menu result = menuFacade.getMenuById(5);

        assertEquals(expected, result);
        verify(menuService, times(1)).getMenuById(5);
        verifyNoMoreInteractions(menuService);
    }

    @Test
    void createMenu_shouldDelegateToService() {
        Menu menu = new Menu.MenuBuilder().setId(0).setName("Yeni Menu").setRestaurantId(1).build();

        menuFacade.createMenu(menu);

        verify(menuService, times(1)).createMenu(menu);
        verifyNoMoreInteractions(menuService);
    }

    @Test
    void updateMenu_shouldDelegateToService() {
        int id = 7;
        Menu menu = new Menu.MenuBuilder().setId(id).setName("Guncel Menu").setRestaurantId(3).build();

        menuFacade.updateMenu(id, menu);

        verify(menuService, times(1)).updateMenu(id, menu);
        verifyNoMoreInteractions(menuService);
    }

    @Test
    void deleteMenu_shouldDelegateToService() {
        menuFacade.deleteMenu(9);

        verify(menuService, times(1)).deleteMenu(9);
        verifyNoMoreInteractions(menuService);
    }

    @Test
    void ozelSiparisOlustur_whenNoExtras_shouldReturnBaseFood() {
        Yemek siparis = menuFacade.ozelSiparisOlustur("Kebap", 100.0, false, false);

        assertNotNull(siparis);
        assertEquals("Kebap", siparis.getAciklama());
        assertEquals(100.0, siparis.getFiyat(), 0.0001);
    }

    @Test
    void ozelSiparisOlustur_whenCheeseAndSauce_shouldApplyDecorators() {
        Yemek siparis = menuFacade.ozelSiparisOlustur("Kebap", 100.0, true, true);

        assertNotNull(siparis);

        // Açıklama sırası: AnaYemek + Ekstra Peynir + Özel Sos
        String aciklama = siparis.getAciklama();
        assertTrue(aciklama.contains("Kebap"));
        assertTrue(aciklama.contains("Ekstra Peynir"));
        assertTrue(aciklama.contains("Özel Sos"));

        // Fiyat: temel + peynir(15) + sos(10)
        assertEquals(125.0, siparis.getFiyat(), 0.0001);
    }
}
