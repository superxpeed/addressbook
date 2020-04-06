package com.addressbook.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginLogoutWithRolesTest {

    @Test
    public void stage1_loginLogoutWithRoles() throws InterruptedException {
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
        // Check if user name is right
        Assert.assertEquals(userName.getText(), "user");
        // Click user info button to check roles
        userName.click();
        // Locate first role
        WebElement userRole = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[2]/div/div/div[2]/span")));
        /// This user should have only one role called USER, check that
        Assert.assertEquals(userRole.getText(), "USER");
        // Close modal with user roles
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div[1]/button")).click();
        // Wait for modal window to close
        Thread.sleep(300);
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button"))).click();
        // Login as admin
        loginInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"login\"]")));
        loginInput.sendKeys("admin");
        passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));
        passwordInput.sendKeys("adminPass");
        loginButton = driver.findElement(By.xpath("//*[@id=\"application\"]/div/div/div[2]/div[2]/button"));
        loginButton.click();
        userName = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/div/button")));
        Assert.assertEquals(userName.getText(), "admin");
        userName.click();
        userRole = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[2]/div/div/div[2]/span")));
        Assert.assertEquals(userRole.getText(), "USER");
        WebElement adminRole = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div[2]/span[2]"));
        // Admin should also have second role called ADMIN, check that
        Assert.assertEquals(adminRole.getText(), "ADMIN");
        // Close modal with user roles
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div[1]/button")).click();
        // Wait for modal window to close
        Thread.sleep(300);
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button"))).click();
        Thread.sleep(300);
        driver.quit();
    }
}
