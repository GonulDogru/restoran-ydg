package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IcecekCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void icecekCreateScenario_shouldCreateIcecekAndAppearInList() {
        String restName = "Rest_" + System.currentTimeMillis();
        long restId = createRestaurant(restName);

        String menuName = "Menu_" + System.currentTimeMillis();
        long menuId = createMenu(menuName, restId);

        String icecekName = "Icecek_" + System.currentTimeMillis();

        driver.get(baseUrl() + "/icecek/new");
        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(icecekName);
        driver.findElement(By.id("price")).sendKeys("25");
        driver.findElement(By.id("menuId")).sendKeys(String.valueOf(menuId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/icecek/list");

        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + icecekName + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
