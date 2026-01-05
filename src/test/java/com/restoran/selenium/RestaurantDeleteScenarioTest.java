package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RestaurantDeleteScenarioTest extends BaseSeleniumTest {

    @Test
    void scenario_deleteRestaurant_andNotSeeInList() {
        String name = "Selenium Restaurant " + System.currentTimeMillis();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1) Create
        driver.get(baseUrl() + "/restaurant/new");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys("Adres");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 2) Go list
        driver.get(baseUrl() + "/restaurant/list");

        // 3) Find the row that contains the created restaurant name
        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + name + "')]]");
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(rowBy));

        // 4) Click delete button (opens Bootstrap modal)
        WebElement deleteBtn = row.findElement(
                By.xpath(".//button[contains(@data-delete-url,'/restaurant/delete/')]")
        );
        wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();

        // 5) Confirm delete in modal (href is set by JS on modal show)
        By confirmBy = By.cssSelector("#deleteConfirmBtn");
        wait.until(ExpectedConditions.elementToBeClickable(confirmBy)).click();

        // 6) Verify not present
        driver.get(baseUrl() + "/restaurant/list");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(rowBy));
        assertFalse(driver.getPageSource().contains(name), "Silinen restoran listede gorunmemeli");
    }
}
