package com.restoran.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public abstract class BaseSeleniumTest {

    protected WebDriver driver;

    protected String baseUrl() {
        String env = System.getenv("APP_BASE_URL");
        return (env == null || env.isBlank()) ? "http://localhost:8091" : env;
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
            throw new IllegalStateException(
                    "SELENIUM_REMOTE_URL tanimli degil. " +
                    "Jenkins/Docker'da selenium/standalone-chrome ile RemoteWebDriver kullanilacak sekilde tasarlandi."
            );
        }

        driver = new RemoteWebDriver(new URL(remoteUrl), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
