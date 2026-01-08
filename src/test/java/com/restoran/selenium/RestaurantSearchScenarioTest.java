package com.restoran.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestaurantSearchScenarioTest extends BaseSeleniumTest {

    @Test
    void restaurantSearchScenario_shouldFilterListByName() {
        String unique = "SeleniumRest_" + System.currentTimeMillis();

        // önce restoran oluştur
        createRestaurant(unique);

        // listeye git ve arama yap
        driver.get(baseUrl() + "/restaurant/list");

        WebElement search = wait15().until(
                ExpectedConditions.visibilityOfElementLocated(By.id("restaurantSearch"))
        );

        search.clear();
        search.sendKeys(unique);

        // Bazı UI'larda filtre "Enter" veya blur ile tetiklenir; güvenli tetik
        search.sendKeys(Keys.ENTER);

        // Filtre sonrası ilgili satır görünür olmalı
        By rowBy = By.xpath("//tbody//tr[td[contains(normalize-space(.), '" + unique + "')]]");
        WebElement row = wait15().until(ExpectedConditions.visibilityOfElementLocated(rowBy));
        assertTrue(row.isDisplayed());

        // Sadece GÖRÜNEN satır sayısını kontrol et (hidden satırlar sayılmasın)
        wait15().until(d -> countVisibleRows() == 1);
        assertEquals(1, countVisibleRows());
    }

    private long countVisibleRows() {
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        return rows.stream().filter(WebElement::isDisplayed).count();
    }
}
