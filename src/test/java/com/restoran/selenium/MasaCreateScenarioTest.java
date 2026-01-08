package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MasaCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void masaCreateScenario_shouldCreateMasaAndAppearInList() {
        String restName = "Rest_" + System.currentTimeMillis();
        long restId = createRestaurant(restName);

        // Masa numarası sayısal olmalı çünkü input type="number"
        String masaNumara = String.valueOf(System.currentTimeMillis() % 1000); 

        driver.get(baseUrl() + "/masa/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("numara"))).sendKeys(masaNumara);
        driver.findElement(By.id("kapasite")).sendKeys("4"); // Kapasite alanı eklendi
        driver.findElement(By.id("restaurantId")).sendKeys(String.valueOf(restId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/masa/list");

        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + masaNumara + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
