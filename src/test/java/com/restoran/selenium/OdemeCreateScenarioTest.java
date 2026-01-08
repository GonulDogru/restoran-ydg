package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OdemeCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void odemeCreateScenario_shouldCreateOdemeAndAppearInList() {
        String restName = "Rest_" + System.currentTimeMillis();
        long restId = createRestaurant(restName);

        String masaName = "Masa_" + System.currentTimeMillis();
        long masaId = createMasa(masaName, restId);

        driver.get(baseUrl() + "/odeme/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("amount"))).sendKeys("100");
        driver.findElement(By.id("masaId")).sendKeys(String.valueOf(masaId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/odeme/list");

        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + masaId + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
