package com.restoran.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public abstract class BaseSeleniumTest {

    protected WebDriver driver;

    protected String baseUrl() {
        String env = System.getenv("APP_BASE_URL");
        return (env == null || env.isBlank()) ? "http://localhost:8091" : env;
    }

    protected WebDriverWait wait15() {
        return new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * UI uzerinden restoran olusturur ve listeden ID bilgisini geri dondurur.
     */
    protected long createRestaurant(String name) {
        driver.get(baseUrl() + "/restaurant/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys("Selenium Address");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/restaurant/list");
        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + name + "')]]");
        WebElement row = wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy));
        String idText = row.findElement(By.xpath("./td[1]")).getText().trim();
        return Long.parseLong(idText);
    }

    /**
     * UI uzerinden menu olusturur ve listeden ID bilgisini geri dondurur.
     */
    protected long createMenu(String name, long restaurantId) {
        driver.get(baseUrl() + "/menu/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(name);
        driver.findElement(By.id("restaurantId")).sendKeys(String.valueOf(restaurantId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/menu/list");
        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + name + "')]]");
        WebElement row = wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy));
        String idText = row.findElement(By.xpath("./td[1]")).getText().trim();
        return Long.parseLong(idText);
    }

    /**
     * UI uzerinden masa olusturur ve listeden ID bilgisini geri dondurur.
     */
    protected long createMasa(String name, long restaurantId) {
        driver.get(baseUrl() + "/masa/new");

        // Masa numarası sayısal olmalı, name parametresi string gelse de sayısal bir değere çeviriyoruz veya direkt kullanıyoruz
        // Ancak testlerde genellikle "Masa_..." gibi string gönderiliyor, bunu sayıya çevirmek gerekebilir.
        // Basitlik adına, name parametresini sayısal bir string olarak varsayacağız veya hashcode kullanacağız.
        String masaNumara = String.valueOf(Math.abs(name.hashCode()) % 1000);

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("numara"))).sendKeys(masaNumara);
        driver.findElement(By.id("kapasite")).sendKeys("4");
        driver.findElement(By.id("restaurantId")).sendKeys(String.valueOf(restaurantId));
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl() + "/masa/list");
        By rowBy = By.xpath("//tr[td[contains(normalize-space(.), '" + masaNumara + "')]]");
        WebElement row = wait15().until(ExpectedConditions.presenceOfElementLocated(rowBy));
        String idText = row.findElement(By.xpath("./td[1]")).getText().trim();
        return Long.parseLong(idText);
    }

    /**
     * UI uzerinden user olusturur.
     */
    protected void createUser(String username, String password) {
        driver.get(baseUrl() + "/users/new");

        wait15().until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("adres")).sendKeys("Selenium Adres");
        driver.findElement(By.id("telefon")).sendKeys("5551234567");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @BeforeEach
    void setUpDriver() throws Exception {
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1400,900");

        if (remoteUrl == null || remoteUrl.isBlank()) {
            remoteUrl = "http://localhost:4444/wd/hub";
        }

        driver = new RemoteWebDriver(new URL(remoteUrl), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterEach
    void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
