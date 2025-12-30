package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RestaurantDeleteScenarioTest extends BaseSeleniumTest {

    @Test
    void scenario_deleteRestaurant_andNotSeeInList() {
        String name = "Selenium Restaurant " + System.currentTimeMillis();

        // Create
        driver.get(baseUrl() + "/restaurant/new");
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys("Adres");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Delete from list
        driver.get(baseUrl() + "/restaurant/list");
        WebElement deleteLink = driver.findElement(By.xpath(
                "//tr[td[contains(normalize-space(.), '" + name + "')]]//a[contains(@href,'/restaurant/delete/')]"
        ));
        deleteLink.click();

        // If browser shows confirm alert, accept it (defensive)
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException ignored) {
        }

        // Verify not present
        driver.get(baseUrl() + "/restaurant/list");
        String page = driver.getPageSource();
        assertFalse(page.contains(name), "Silinen restoran listede gorunmemeli");
    }
}
