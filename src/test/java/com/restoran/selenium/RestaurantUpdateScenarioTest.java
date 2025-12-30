package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantUpdateScenarioTest extends BaseSeleniumTest {

    @Test
    void scenario_updateRestaurant_addressChangesInList() {
        String name = "Selenium Restaurant " + System.currentTimeMillis();
        String address1 = "Adres 1";
        String address2 = "Adres 2";

        // Create
        driver.get(baseUrl() + "/restaurant/new");
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys(address1);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Go list & click edit for that row
        driver.get(baseUrl() + "/restaurant/list");

        WebElement editLink = driver.findElement(By.xpath(
                "//tr[td[contains(normalize-space(.), '" + name + "')]]//a[contains(@href,'/restaurant/edit/')]"
        ));
        editLink.click();

        // Update
        WebElement addressInput = driver.findElement(By.id("address"));
        addressInput.clear();
        addressInput.sendKeys(address2);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify
        driver.get(baseUrl() + "/restaurant/list");
        String page = driver.getPageSource();
        assertTrue(page.contains(address2), "Guncellenen adres listede gorunmeli");
    }
}
