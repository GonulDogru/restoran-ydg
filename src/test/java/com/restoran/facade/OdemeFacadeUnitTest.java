package com.restoran.facade;

import com.restoran.model.Odeme;
import com.restoran.repository.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OdemeFacadeUnitTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private OdemeFacade odemeFacade;

    @Test
    void getAllOdemeler_shouldDelegateToRestaurantService() {
        List<Odeme> expected = List.of(
                new Odeme.OdemeBuilder().setId(1).setMasaId(10).setTutar(100.0).setOdemeYontemi("Nakit").build()
        );
        when(restaurantService.getAllOdemeler()).thenReturn(expected);

        List<Odeme> result = odemeFacade.getAllOdemeler();

        assertEquals(expected, result);
        verify(restaurantService, times(1)).getAllOdemeler();
        verifyNoMoreInteractions(restaurantService);
    }

    @Test
    void getOdemeById_shouldDelegateToRestaurantService() {
        Odeme expected = new Odeme.OdemeBuilder().setId(5).setMasaId(2).setTutar(75.5).setOdemeYontemi("Kredi Kartı").build();
        when(restaurantService.getOdemeById(5)).thenReturn(expected);

        Odeme result = odemeFacade.getOdemeById(5);

        assertEquals(expected, result);
        verify(restaurantService, times(1)).getOdemeById(5);
        verifyNoMoreInteractions(restaurantService);
    }

    @Test
    void saveOdeme_shouldDelegateToRestaurantService() {
        Odeme odeme = new Odeme.OdemeBuilder().setId(0).setMasaId(3).setTutar(120.0).setOdemeYontemi("Nakit").build();

        odemeFacade.createOdeme(odeme);

        verify(restaurantService, times(1)).saveOdeme(odeme);
        verifyNoMoreInteractions(restaurantService);
    }

    @Test
    void updateOdeme_shouldDelegateToRestaurantService() {
        int id = 7;
        Odeme odeme = new Odeme.OdemeBuilder().setId(id).setMasaId(8).setTutar(250.0).setOdemeYontemi("Kredi Kartı").build();

        odemeFacade.updateOdeme(id, odeme);

        verify(restaurantService, times(1)).updateOdeme(id, odeme);
        verifyNoMoreInteractions(restaurantService);
    }

    @Test
    void deleteOdeme_shouldDelegateToRestaurantService() {
        odemeFacade.deleteOdeme(9);

        verify(restaurantService, times(1)).deleteOdeme(9);
        verifyNoMoreInteractions(restaurantService);
    }
}
