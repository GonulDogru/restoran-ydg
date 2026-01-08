package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersCreateScenarioTest extends BaseSeleniumTest {

    @Test
    void usersCreateScenario_shouldCreateUserAndAppearInList() {
        String name = "SeleniumUser_" + System.currentTimeMillis();
        String email = "sel_" + System.currentTimeMillis() + "@test.com";
        String password = "123456";

        driver.get(baseUrl() + "/users/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("username")
        )).sendKeys(name);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/users/list");

        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + email + "')]]");
        assertTrue(wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy)).isDisplayed());
    }
}
