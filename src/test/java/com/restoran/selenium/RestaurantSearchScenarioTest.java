package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestaurantSearchScenarioTest extends BaseSeleniumTest {

    @Test
    void restaurantSearchScenario_shouldFilterListByName() {
        String unique = "SeleniumRest_" + System.currentTimeMillis();

        // önce restoran oluştur
        createRestaurant(unique);

        // listeye git ve arama yap
        driver.get(baseUrl() + "/restaurant/list");

        WebElement search = wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));
        search.clear();
        search.sendKeys(unique);

        // filtre sonrası tek satır kalmalı ve isim görünmeli
        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + unique + "')]]");
        WebElement row = wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy));
        assertTrue(row.isDisplayed());

        int rows = driver.findElements(By.cssSelector("tbody tr")).size();
        assertEquals(1, rows);
    }
}
