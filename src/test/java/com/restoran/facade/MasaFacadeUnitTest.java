package com.restoran.facade;

import com.restoran.model.Masa;
import com.restoran.repository.MasaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasaFacadeUnitTest {

    @Mock
    private MasaService masaService;

    @InjectMocks
    private MasaFacade masaFacade;

    @Test
    void createMasa_shouldDelegateToService() {
        Masa masa = new Masa();
        masa.setNumara(10);
        masa.setKapasite(4);
        masa.setRestaurantId(1);

        masaFacade.createMasa(masa);

        verify(masaService, times(1)).save(masa);
    }
}
