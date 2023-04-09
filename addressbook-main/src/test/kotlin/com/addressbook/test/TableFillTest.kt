package com.addressbook.test

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TableFillTest {

    @Test
    fun stage1_fillOrganizationTable() {
        WebDriverManager.chromedriver().setup()
        // Initialize Selenium driver
        val options = ChromeOptions()
        options.addArguments("--remote-allow-origins=*")
        options.addArguments("--ignore-urlfetcher-cert-requests")
        val driver = ChromeDriver(options)
        // Initialize wait driver
        val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(20))
        // Open login page
        driver.get("https://localhost:9000")
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"login\"]")))
        // Locate login input field
        val loginInput = driver.findElement(By.xpath("//*[@id=\"login\"]"))
        // Locate password input field
        val passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"))
        // Locate login button
        val loginButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[2]/div/button"))
        // Enter login for ordinary user
        loginInput.sendKeys("user")
        // Enter password
        passwordInput.sendKeys("userPass")
        // Click login
        loginButton.click()
        Thread.sleep(500)
        // Locate user info button on navigation bar
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/header/div/div/button[1]")))
        Thread.sleep(500)
        // First level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        Thread.sleep(500)
        // Second level 1 tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a[1]"))).click()
        Thread.sleep(500)
        // Third level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        Thread.sleep(500)
        // Last level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        Thread.sleep(500)
        // Click first tab - Organizations
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables\"]/div/div/button"))).click()
        Thread.sleep(500)
        var name: String
        for (i in 0..100) {
            // Generate test name
            name = "Test " + Math.random() + " Name"
            // Enter data to create new organization record
            driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys(name)
            driver.findElement(By.xpath("//*[@id=\"street\"]")).sendKeys("Test street")
            driver.findElement(By.xpath("//*[@id=\"zip\"]")).sendKeys("Test zip")
            // Choose Private type of organization
            driver.findElement(By.xpath("//*[@id=\"mui-component-select-type\"]")).click()
            driver.findElement(By.xpath("//*[@id=\"menu-type\"]/div[3]/ul/li[2]")).click()
            // Click Create organization button
            driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[1]/div/button")).click()
            Thread.sleep(300)
            // Twice sort table by "Last updated" field
            driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
            Thread.sleep(300)
            driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
            Thread.sleep(300)
            // Find name of first record
            driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]")).click()
        }

        // Wait until all notifications disappear
        Thread.sleep(10_000)
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div[1]/header/div/div/button[5]"))).click()
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]/div/div[2]/button[2]"))).click()
        Thread.sleep(300)
        driver.quit()
    }
}