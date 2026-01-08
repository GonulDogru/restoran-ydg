package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void usersCreateScenario_shouldCreateUserAndAppearInList() {
        String name = "SeleniumUser_" + System.currentTimeMillis();
        String password = "123456";
        String adres = "Adres_" + System.currentTimeMillis();
        String telefon = "555" + (System.currentTimeMillis() % 10000000); // Basit bir telefon numarası simülasyonu

        driver.get(baseUrl() + "/users/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(name);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("adres")).sendKeys(adres);
        driver.findElement(By.id("telefon")).sendKeys(telefon);
        
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/users/list");

        // Listede oluşturulan kullanıcı adının göründüğünü doğrula
        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + name + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
