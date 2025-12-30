package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void scenario_createRestaurant_andSeeInList() {
        String name = "Selenium Restaurant " + System.currentTimeMillis();
        String address = "Selenium Adres 1";

        driver.get(baseUrl() + "/restaurant/list");
        driver.findElement(By.linkText("Yeni Restaurant Oluştur")).click();

        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys(address);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String page = driver.getPageSource();
        assertTrue(page.contains(name), "Liste sayfasinda yeni restoran adi gorunmeli");
    }
}
