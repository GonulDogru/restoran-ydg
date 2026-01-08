package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void menuCreateScenario_shouldCreateMenuAndAppearInList() {
        String restName = "Rest_" + System.currentTimeMillis();
        long restId = createRestaurant(restName);

        String menuName = "Menu_" + System.currentTimeMillis();
        driver.get(baseUrl() + "/menu/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(menuName);
        driver.findElement(By.id("restaurantId")).sendKeys(String.valueOf(restId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/menu/list");

        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + menuName + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
