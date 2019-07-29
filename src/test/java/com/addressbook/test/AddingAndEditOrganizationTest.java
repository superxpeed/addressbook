package com.addressbook.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AddingAndEditOrganizationTest {
    @Test
    public void testAddingOrganization() throws InterruptedException {
        // Set Chrome driver location
        System.setProperty("webdriver.chrome.driver", LoginLogoutWithRolesTest.class.getClassLoader().getResource("chromedriver").getPath());
        // Initialize Selenium driver
        WebDriver driver = new ChromeDriver();
        // Initialize wait driver
        WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
        // Open login page
        driver.get("http://localhost:8080");
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]")));
        // Locate login input field
        WebElement loginInput = driver.findElement(By.xpath("//*[@id=\"login\"]"));
        // Locate password input field
        WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));
        // Locate login button
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"application\"]/div/div/div[2]/div[2]/button"));
        // Enter login for ordinary user
        loginInput.sendKeys("user");
        // Enter password
        passwordInput.sendKeys("userPass");
        // Click login
        loginButton.click();
        // Locate user info button on navigation bar
        WebElement userName = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/div/button")));
        // First level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click();
        // Second level 1 tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click();
        // Third level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click();
        // Last level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click();
        // Click first tab - Organizations
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-1\"]"))).click();
        // Generate test name
        String name = "Test " + Math.random() + " Name";
        // Enter data to create new organization record
        driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys(name);
        driver.findElement(By.xpath("//*[@id=\"street\"]")).sendKeys("Test street");
        driver.findElement(By.xpath("//*[@id=\"zip\"]")).sendKeys("Test zip");
        // Choose Private type of organization
        driver.findElement(By.xpath("//*[@id=\"type\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"type\"]/option[2]")).click();
        // Click Create organization button
        driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[1]/div/button")).click();
        // Wait until it is saved
        Thread.sleep(2_000);
        // Find tbody of organizations table
        WebElement tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"));
        // Find first rows
        List<WebElement> trs = tbody.findElements(By.xpath("./tr"));
        boolean foundBeforeUpdate = false;
        // Find our new row
        for(WebElement tr : trs){
            if(tr.findElements(By.xpath("./td")).get(2).getText().equals(name)){
                // Clear selection to unlock this new record
                tr.click();
                foundBeforeUpdate = true;
            }
        }
        // Check if it was found
        Assert.assertTrue(foundBeforeUpdate);

        // Refresh page and reread data
        driver.navigate().refresh();
        Thread.sleep(2_000);

        // Again find our new record
        boolean foundBeforeUpdateAfterRefresh = false;
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"));
        trs = tbody.findElements(By.xpath("./tr"));
        for(WebElement tr : trs){
            if(tr.findElements(By.xpath("./td")).get(2).getText().equals(name)){
                // select row
                tr.click();
                foundBeforeUpdateAfterRefresh = true;
            }
        }
        Assert.assertTrue(foundBeforeUpdateAfterRefresh);

        // Generate new name for test record
        name = "Test " + Math.random() + " Name";

        // Update previously created record
        driver.findElement(By.xpath("//*[@id=\"name\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys(name);
        // Save it
        driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[1]/div/button")).click();

        Thread.sleep(2_000);

        // Check if name in the table was changed
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"));
        trs = tbody.findElements(By.xpath("./tr"));
        boolean foundAfterUpdate = false;
        for(WebElement tr : trs){
            if(tr.findElements(By.xpath("./td")).get(2).getText().equals(name)){
                // clear selection
                tr.click();
                foundAfterUpdate = true;
            }
        }
        Assert.assertTrue(foundAfterUpdate);

        // Reload page
        driver.navigate().refresh();
        Thread.sleep(2_000);

        // Find updated record
        boolean foundAfterUpdateAfterRefresh = false;
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"));
        trs = tbody.findElements(By.xpath("./tr"));
        for(WebElement tr : trs){
            if(tr.findElements(By.xpath("./td")).get(2).getText().equals(name)){
                tr.click();
                foundAfterUpdateAfterRefresh = true;
            }
        }
        // Check if updated record was found
        Assert.assertTrue(foundAfterUpdateAfterRefresh);

        // Wait until all notifications disappear
        Thread.sleep(10_000);

        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button[2]"))).click();
        Thread.sleep(300);
        driver.quit();
    }
}
