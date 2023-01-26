package com.addressbook.test

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddingAndEditingEntitiesTest {

    @Test
    fun stage1_addAndEditOrganization() {
        // Initialize Selenium driver
        val driver = WebDriverManager.chromedriver().create()
        // Initialize wait driver
        val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(20))
        // Open login page
        driver.get("http://localhost:9000")
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r1:\"]")))
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
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/div/div/button")))
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
        // Generate test name
        var name = "Test " + Math.random() + " Name"
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
        var firstRow = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[3]"))
        // Check if it was found
        Assert.assertEquals(name, firstRow.text)
        // Refresh page and reread data
        driver.navigate().refresh()
        Thread.sleep(2000)
        // Again find our new record
        // Twice sort table by "Last updated" field
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        firstRow = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[3]"))
        Assert.assertEquals(name, firstRow.text)
        // Select first row
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[1]/span/input")).click()
        Thread.sleep(300)
        // Generate new name for test record
        name = "Test " + Math.random() + " Name"
        // Update previously created record
        val nameTextInput = driver.findElement(By.xpath("//*[@id=\"name\"]"))
        // Clear value
        for (v in nameTextInput.getAttribute("value")) {
            nameTextInput.sendKeys(Keys.BACK_SPACE)
        }
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys(name)
        Thread.sleep(300)
        // Save it
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[1]/div/button")).click()
        Thread.sleep(2_000)
        // Check if name in the table was changed
        firstRow = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[3]/button/div"))
        Assert.assertEquals(name, firstRow.text)
        // Reload page
        driver.navigate().refresh()
        Thread.sleep(2_000)
        // Again find our new record
        // Twice sort table by "Last updated" field
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        firstRow = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[3]"))
        Assert.assertEquals(name, firstRow.text)
        // Wait until all notifications disappear
        Thread.sleep(10_000)
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/button[2]"))).click()
        Thread.sleep(300)
        driver.close()
        driver.quit()
    }

    @Test
    fun stage1_addAndEditPerson() {
        // Initialize Selenium driver
        val driver = WebDriverManager.chromedriver().create()
        // Initialize wait driver
        val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(20))
        // Open login page
        driver.get("http://localhost:9000")
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r1:\"]")))
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
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/div/div/button")))
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
        // Twice sort table by "Last updated" field
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        // Select first row
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[1]/span/input")).click()
        Thread.sleep(300)
        // Click second tab - Persons
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables\"]/div/div/button[2]"))).click()
        Thread.sleep(2_000)
        // Click create person button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/button[1]"))).click()
        Thread.sleep(1_000)
        // Click third tab - New person
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables\"]/div/div/button[3]"))).click()
        Thread.sleep(1_000)
        driver.findElement(By.xpath("//*[@id=\"firstName\"]")).sendKeys("First name")
        driver.findElement(By.xpath("//*[@id=\"lastName\"]")).sendKeys("Last name")
        driver.findElement(By.xpath("//*[@id=\"salary\"]")).sendKeys("10000")
        driver.findElement(By.xpath("//*[@id=\"currency\"]")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"menu-currency\"]/div[3]/ul/li[25]")).click()
        Thread.sleep(300)
        val rteContainer = driver.findElement(By.id("mui-rte-container"))
        val rteLabel = rteContainer.findElement(By.xpath("//*[contains(text(), \"Resume\")]"))
        val actions = Actions(driver)
        actions.moveToElement(rteLabel)
        actions.click(rteLabel)
        actions.perform()
        val rte_input = WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class=\"notranslate public-DraftEditor-content\" and @role=\"textbox\"]")))
        rte_input.sendKeys("First point", Keys.ENTER, "Second point")
        Thread.sleep(300)
        // Add contact
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/button")).click()
        Thread.sleep(300)
        // New contact header
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[1]")).click()
        Thread.sleep(300)
        //Enter mobile phone
        var firstContactParentBody = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[1]"))
        firstContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).sendKeys("8-999-999-99-99")
        Thread.sleep(300)
        firstContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).sendKeys("Mobile phone")
        Thread.sleep(300)
        // Add contact
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/button")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[2]")).click()
        Thread.sleep(300)
        var secondContactParentBody = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[2]"))
        Thread.sleep(300)
        secondContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")).click()
        driver.findElement(By.xpath("//*[@id=\"menu-type\"]/div[3]/ul/li[2]")).click()
        Thread.sleep(300)
        secondContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).sendKeys("8-888-888-88-88")
        Thread.sleep(300)
        secondContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).sendKeys("Home phone")
        Thread.sleep(300)
        // Add contact
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/button")).click()
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[3]")).click()
        Thread.sleep(300)
        var thirdContactParentBody = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[3]"))
        Thread.sleep(300)
        thirdContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")).click()
        driver.findElement(By.xpath("//*[@id=\"menu-type\"]/div[3]/ul/li[3]")).click()
        Thread.sleep(300)
        thirdContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).sendKeys("NY, Fifth avenue")
        Thread.sleep(300)
        thirdContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).sendKeys("Home address")
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[1]/button")).click()
        Thread.sleep(2_000)
        driver.navigate().refresh()
        Thread.sleep(2_000)
        // Click first tab - Organizations
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables\"]/div/div/button"))).click()
        // Twice sort table by "Last updated" field
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/thead/tr/th[7]/div[1]/div[1]/div")).click()
        Thread.sleep(300)
        // Select first row
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-1\"]/div/div[2]/div/div[2]/table/tbody/tr[1]/td[1]/span/input")).click()
        Thread.sleep(300)
        // Click second tab - Persons
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables\"]/div/div/button[2]"))).click()
        Thread.sleep(2_000)
        Assert.assertEquals("First name", driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-2\"]/div/div/div/div[2]/table/tbody/tr/td[4]/button/div")).text)
        Assert.assertEquals("Last name", driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-2\"]/div/div/div/div[2]/table/tbody/tr/td[5]/button/div")).text)
        Assert.assertEquals("First point Second point", driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-2\"]/div/div/div/div[2]/table/tbody/tr/td[6]/button/div")).text)
        Assert.assertEquals("10000 GBP", driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-2\"]/div/div/div/div[2]/table/tbody/tr/td[7]/button/div")).text)
        // Select top row
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-2\"]/div/div/div/div[2]/table/tbody/tr/td[1]/span/input")).click()
        // Click third tab
        driver.findElement(By.xpath("//*[@id=\"tables\"]/div/div/button[3]")).click()
        Assert.assertEquals("First name", driver.findElement(By.xpath("//*[@id=\"firstName\"]")).getAttribute("value"))
        Assert.assertEquals("Last name", driver.findElement(By.xpath("//*[@id=\"lastName\"]")).getAttribute("value"))
        Assert.assertEquals("10000", driver.findElement(By.xpath("//*[@id=\"salary\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("First point", driver.findElement(By.xpath("//*[@id=\"mui-rte-editor-container\"]/div/div/div/div/div[1]/div/span/span")).text)
        Thread.sleep(300)
        Assert.assertEquals("Second point", driver.findElement(By.xpath("//*[@id=\"mui-rte-editor-container\"]/div/div/div/div/div[2]/div/span/span")).text)
        Thread.sleep(300)
        // Click contact header to expand
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[1]")).click()
        Thread.sleep(300)
        firstContactParentBody = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[1]"))
        Assert.assertEquals("8-999-999-99-99", firstContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Mobile phone", firstContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Mobile phone", firstContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")).text)
        // Click contact header to expand
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[2]")).click()
        Thread.sleep(300)
        secondContactParentBody = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[2]"))
        Assert.assertEquals("8-888-888-88-88", secondContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Home phone", secondContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Home phone", secondContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")).text)
        // Click contact header to expand
        driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[3]")).click()
        Thread.sleep(300)
        thirdContactParentBody = driver.findElement(By.xpath("//*[@id=\"simple-tabpanel-3\"]/div/div/div[2]/div/div[3]"))
        Assert.assertEquals("NY, Fifth avenue", thirdContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Home address", thirdContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Address", thirdContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")).text)
        // Wait until all notifications disappear
        Thread.sleep(10_000)
        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/header/div/div/button[2]"))).click()
        Thread.sleep(300)
        driver.close()
        driver.quit()
    }

}
