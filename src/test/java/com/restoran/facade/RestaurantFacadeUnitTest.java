package com.restoran.facade;

import com.restoran.model.Restaurant;
import com.restoran.repository.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantFacadeUnitTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantFacade restaurantFacade;

    @Test
    void updateRestaurant_shouldPassSameIdAndFieldsToService() {
        int id = 42;

        Restaurant input = new Restaurant.RestaurantBuilder()
                .setId(0) // facade id'yi URL'den alir, bunu override eder
                .setName("Test Restaurant")
                .setAddress("Adres 1")
                .build();

        when(restaurantService.updateRestaurant(eq(id), any(Restaurant.class))).thenReturn("Güncellendi");

        String result = restaurantFacade.updateRestaurant(id, input);

        assertEquals("Güncellendi", result);

        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantService, times(1)).updateRestaurant(eq(id), captor.capture());

        Restaurant passed = captor.getValue();
        assertEquals(id, passed.getId());
        assertEquals("Test Restaurant", passed.getName());
        assertEquals("Adres 1", passed.getAddress());
    }
}
