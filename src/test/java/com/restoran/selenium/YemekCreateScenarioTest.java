package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YemekCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void yemekCreateScenario_shouldCreateYemekAndAppearInList() {
        String restName = "Rest_" + System.currentTimeMillis();
        long restId = createRestaurant(restName);

        String menuName = "Menu_" + System.currentTimeMillis();
        long menuId = createMenu(menuName, restId);

        String yemekName = "Yemek_" + System.currentTimeMillis();

        driver.get(baseUrl() + "/yemek/new");
        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(yemekName);

        driver.findElement(By.id("menuId")).sendKeys(String.valueOf(menuId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/yemek/list");

        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + yemekName + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
