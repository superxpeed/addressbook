package com.addressbook.test

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.Assert
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.awt.Robot
import java.awt.event.KeyEvent
import java.time.Duration

class LoginLogoutWithRolesTest {

    @Test
    fun stage1_loginLogoutWithRoles() {
        // Initialize Selenium driver
        val driver = WebDriverManager.chromedriver().create()
        // Initialize wait driver
        val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(20))
        // Dismiss certificate choice dialog
        Thread {
            try {
                Thread.sleep(1_000)
                val robot = Robot()
                robot.keyPress(KeyEvent.VK_ESCAPE)
                robot.keyRelease(KeyEvent.VK_ESCAPE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        // Open login page
        driver.get("http://localhost:9000")
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r1:\"]")))
        // Locate login input field
        var loginInput = driver.findElement(By.xpath("//*[@id=\"login\"]"))
        // Locate password input field
        var passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"))
        // Locate login button
        var loginButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[2]/div/button"))
        // Enter login for ordinary user
        loginInput.sendKeys("user")
        // Enter password
        passwordInput.sendKeys("userPass")
        // Click login
        loginButton.click()
        // Locate user info button on navigation bar
        var userName = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/div/div/button")))
        // Check if user name is right
        Assert.assertEquals(userName.text, "USER")
        // Click user info button to check roles
        userName.click()
        // Locate first role
        var userRole = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]/div/div[1]/div/span")))
        /// This user should have only one role called USER, check that
        Assert.assertEquals(userRole.text, "USER")
        // Close modal with user roles
        Thread {
            try {
                val robot = Robot()
                robot.keyPress(KeyEvent.VK_ESCAPE)
                robot.keyRelease(KeyEvent.VK_ESCAPE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        // Wait for modal window to close
        Thread.sleep(300)
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/button"))).click()
        // Login as admin
        loginInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"login\"]")))
        loginInput.sendKeys("admin")
        passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"))
        passwordInput.sendKeys("adminPass")
        loginButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[2]/div/button"))
        loginButton.click()
        userName = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/div/div/button")))
        Assert.assertEquals(userName.text, "ADMIN")
        userName.click()
        userRole = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]/div/div[1]/div/span")))
        Assert.assertEquals(userRole.text, "USER")
        val adminRole = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[1]/div[2]/span"))
        // Admin should also have second role called ADMIN, check that
        Assert.assertEquals(adminRole.text, "ADMIN")
        // Close modal with user roles
        Thread {
            try {
                val robot = Robot()
                robot.keyPress(KeyEvent.VK_ESCAPE)
                robot.keyRelease(KeyEvent.VK_ESCAPE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        // Wait for modal window to close
        Thread.sleep(300)
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/button"))).click()
        Thread.sleep(300)
        driver.close()
        driver.quit()
    }
}
