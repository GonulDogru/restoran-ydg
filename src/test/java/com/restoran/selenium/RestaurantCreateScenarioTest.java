package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void scenario_createRestaurant_andSeeInList() {
        String name = "Selenium Restaurant " + System.currentTimeMillis();
        String address = "Selenium Adres 1";

        driver.get(baseUrl() + "/restaurant/list");

        // linkText yerine stabil data-testid kullanıyoruz
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='restaurant-new']")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys(address);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Liste sayfasına dönüyor musunuz? Uygulama zaten redirect ediyorsa bu şart değil.
        // Yine de sağlam olsun diye listeyi tekrar açıp doğrulayabilirsiniz:
        driver.get(baseUrl() + "/restaurant/list");

        String page = driver.getPageSource();
        assertTrue(page.contains(name), "Liste sayfasinda yeni restoran adi gorunmeli");
    }
}
