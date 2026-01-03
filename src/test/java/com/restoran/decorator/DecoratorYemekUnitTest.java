package com.restoran.decorator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorYemekUnitTest {

    @Test
    void anaYemek_shouldReturnBaseDescriptionAndPrice() {
        Yemek yemek = new AnaYemek("Kebap", 100.0);

        assertEquals("Kebap", yemek.getAciklama());
        assertEquals(100.0, yemek.getFiyat(), 0.0001);
    }

    @Test
    void ekstraPeynir_shouldAddPriceAndAppendDescription() {
        Yemek yemek = new EkstraPeynir(new AnaYemek("Kebap", 100.0));

        assertTrue(yemek.getAciklama().contains("Kebap"), yemek.getAciklama());
        assertTrue(yemek.getAciklama().contains("Ekstra Peynir"), yemek.getAciklama());
        assertEquals(115.0, yemek.getFiyat(), 0.0001);
    }

    @Test
    void ekstraSos_shouldAddPriceAndAppendDescription() {
        Yemek yemek = new EkstraSos(new AnaYemek("Kebap", 100.0));

        assertTrue(yemek.getAciklama().contains("Kebap"), yemek.getAciklama());
        assertTrue(yemek.getAciklama().contains("Özel Sos"), yemek.getAciklama());
        assertEquals(110.0, yemek.getFiyat(), 0.0001);
    }

    @Test
    void combinedDecorators_shouldAccumulatePriceAndDescription() {
        Yemek yemek = new EkstraSos(new EkstraPeynir(new AnaYemek("Kebap", 100.0)));

        String desc = yemek.getAciklama();
        assertTrue(desc.contains("Kebap"), desc);
        assertTrue(desc.contains("Ekstra Peynir"), desc);
        assertTrue(desc.contains("Özel Sos"), desc);

        assertEquals(125.0, yemek.getFiyat(), 0.0001);
    }
}
