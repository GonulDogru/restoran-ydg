package com.restoran.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantBuilderUnitTest {

    @Test
    void build_whenNameNull_shouldThrowIllegalStateException() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                new Restaurant.RestaurantBuilder()
                        .setId(1)
                        .setName(null)   // zorunlu alan
                        .setAddress("Malatya")
                        .build()
        );

        assertTrue(ex.getMessage().contains("Restoran adı boş olamaz"));
    }

    @Test
    void build_whenNameProvided_shouldCreateRestaurant() {
        Restaurant r = new Restaurant.RestaurantBuilder()
                .setId(1)
                .setName("CafeX")
                .setAddress("Malatya")
                .build();

        assertEquals(1, r.getId());
        assertEquals("CafeX", r.getName());
        assertEquals("Malatya", r.getAddress());
    }
}
