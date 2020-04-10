package com.addressbook.test

import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddingAndEditingEntitiesTest {

    @Test
    fun stage1_addAndEditOrganization() {
        // Set Chrome driver location
        System.setProperty("webdriver.chrome.driver", AddingAndEditingEntitiesTest::class.java.classLoader.getResource("chromedriver.exe")?.path!!)
        // Initialize Selenium driver
        val driver: WebDriver = ChromeDriver()
        // Initialize wait driver
        val webDriverWait = WebDriverWait(driver, 20)
        // Open login page
        driver.get("http://localhost:9000")
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]")))
        // Locate login input field
        val loginInput = driver.findElement(By.xpath("//*[@id=\"login\"]"))
        // Locate password input field
        val passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"))
        // Locate login button
        val loginButton = driver.findElement(By.xpath("//*[@id=\"application\"]/div/div/div[2]/div[2]/button"))
        // Enter login for ordinary user
        loginInput.sendKeys("user")
        // Enter password
        passwordInput.sendKeys("userPass")
        // Click login
        loginButton.click()
        // Locate user info button on navigation bar
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/div/button")))
        // First level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Second level 1 tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Third level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Last level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Click first tab - Organizations
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-1\"]"))).click()
        // Generate test name
        var name = "Test " + Math.random() + " Name"
        // Enter data to create new organization record
        driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys(name)
        driver.findElement(By.xpath("//*[@id=\"street\"]")).sendKeys("Test street")
        driver.findElement(By.xpath("//*[@id=\"zip\"]")).sendKeys("Test zip")
        // Choose Private type of organization
        driver.findElement(By.xpath("//*[@id=\"organizationType\"]")).click()
        driver.findElement(By.xpath("//*[@id=\"organizationType\"]/option[2]")).click()
        // Click Create organization button
        driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[1]/div/button")).click()
        // Wait until it is saved
        Thread.sleep(2_000)
        // Find tbody of organizations table
        var tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"))
        // Find first rows
        var trs = tbody.findElements(By.xpath("./tr"))
        var foundBeforeUpdate = false
        // Find our new row
        for (tr in trs) {
            if (tr.findElements(By.xpath("./td"))[2].text == name) {
                // Clear selection to unlock this new record
                tr.click()
                foundBeforeUpdate = true
                break
            }
        }
        // Check if it was found
        Assert.assertTrue(foundBeforeUpdate)

        // Refresh page and reread data
        driver.navigate().refresh()
        Thread.sleep(2_000)

        // Again find our new record
        var foundBeforeUpdateAfterRefresh = false
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"))
        trs = tbody.findElements(By.xpath("./tr"))
        for (tr in trs) {
            if (tr.findElements(By.xpath("./td"))[2].text == name) {
                // select row
                tr.click()
                foundBeforeUpdateAfterRefresh = true
                break
            }
        }
        Assert.assertTrue(foundBeforeUpdateAfterRefresh)

        // Generate new name for test record
        name = "Test " + Math.random() + " Name"

        // Update previously created record
        driver.findElement(By.xpath("//*[@id=\"name\"]")).clear()
        driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys(name)
        // Save it
        driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[1]/div/button")).click()

        Thread.sleep(2_000)

        // Check if name in the table was changed
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"))
        trs = tbody.findElements(By.xpath("./tr"))
        var foundAfterUpdate = false
        for (tr in trs) {
            if (tr.findElements(By.xpath("./td"))[2].text == name) {
                // clear selection
                tr.click()
                foundAfterUpdate = true
                break
            }
        }
        Assert.assertTrue(foundAfterUpdate)

        // Reload page
        driver.navigate().refresh()
        Thread.sleep(2_000)

        // Find updated record
        var foundAfterUpdateAfterRefresh = false
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"))
        trs = tbody.findElements(By.xpath("./tr"))
        for (tr in trs) {
            if (tr.findElements(By.xpath("./td"))[2].text == name) {
                tr.click()
                foundAfterUpdateAfterRefresh = true
                break
            }
        }
        // Check if updated record was found
        Assert.assertTrue(foundAfterUpdateAfterRefresh)

        // Wait until all notifications disappear
        Thread.sleep(10_000)

        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button[2]"))).click()
        Thread.sleep(300)
        driver.close()
        driver.quit()
    }

    @Test
    fun stage1_addAndEditPerson() {
        // Set Chrome driver location
        System.setProperty("webdriver.chrome.driver", AddingAndEditingEntitiesTest::class.java.classLoader.getResource("chromedriver.exe")?.path!!)
        // Initialize Selenium driver
        val driver = ChromeDriver()
        // Initialize wait driver
        val webDriverWait = WebDriverWait(driver, 20)
        // Open login page
        driver.get("http://localhost:9000")
        // Wait until page is loaded
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]")))
        // Locate login input field
        val loginInput = driver.findElement(By.xpath("//*[@id=\"login\"]"))
        // Locate password input field
        val passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"))
        // Locate login button
        val loginButton = driver.findElement(By.xpath("//*[@id=\"application\"]/div/div/div[2]/div[2]/button"))
        // Enter login for ordinary user
        loginInput.sendKeys("user")
        // Enter password
        passwordInput.sendKeys("userPass")
        // Click login
        loginButton.click()
        // Locate user info button on navigation bar
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/div/button")))
        // First level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Second level 1 tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Third level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()
        // Last level tile
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/a"))).click()

        // Click first tab - Organizations
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-1\"]"))).click()
        // Find table
        var tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"))
        // Select topmost record in the table
        tbody.findElements(By.xpath("./tr"))[0].findElements(By.xpath("./td"))[2].click()
        // Click second tab - Persons
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-2\"]"))).click()
        Thread.sleep(2_000)
        // Click create person button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button[1]"))).click()
        Thread.sleep(1_000)
        // Click third tab - New person
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-3\"]"))).click()
        Thread.sleep(1_000)
        driver.findElement(By.xpath("//*[@id=\"firstName\"]")).sendKeys("First name")
        driver.findElement(By.xpath("//*[@id=\"lastName\"]")).sendKeys("Last name")
        driver.findElement(By.xpath("//*[@id=\"salary\"]")).sendKeys("10000$/m")

        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/div/div[1]/div[2]/div[1]/button/span")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/div/div[2]/div/div[2]/div")).sendKeys("First point", Keys.ENTER, "Second point")
        Thread.sleep(300)


        // Add contact
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/button")).click()
        Thread.sleep(300)
        // New contact header
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[1]/div[1]/div/a")).click()
        Thread.sleep(300)

        //Enter mobile phone
        var firstContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[1]/div[2]/div"))
        firstContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).sendKeys("8-999-999-99-99")
        Thread.sleep(300)
        firstContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).sendKeys("Mobile phone")
        Thread.sleep(300)

        // Add contact
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/button")).click()
        Thread.sleep(300)
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[2]/div[1]/div/a")).click()
        Thread.sleep(300)

        var secondContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[2]/div[2]/div"))
        Thread.sleep(300)

        var role = Select(secondContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")))
        role.selectByValue("1")
        Thread.sleep(300)
        secondContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).sendKeys("8-888-888-88-88")
        Thread.sleep(300)
        secondContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).sendKeys("Home phone")
        Thread.sleep(300)

        // Add contact
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/button")).click()

        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[3]/div[1]/div/a")).click()
        Thread.sleep(300)

        var thirdContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[3]/div[2]/div"))
        Thread.sleep(300)
        role = Select(thirdContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]")))
        role.selectByValue("2")
        Thread.sleep(300)
        thirdContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).sendKeys("NY, Fifth avenue")
        Thread.sleep(300)
        thirdContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).sendKeys("Home address")
        Thread.sleep(300)

        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/button")).click()
        Thread.sleep(2_000)

        driver.navigate().refresh()

        Thread.sleep(2_000)
        // Find table
        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"))
        // Select topmost record in the table
        tbody.findElements(By.xpath("./tr"))[0].findElements(By.xpath("./td"))[2].click()
        // Click second tab - Persons
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-2\"]"))).click()

        tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-2\"]/div/div/div[1]/div[2]/table/tbody"))
        val trs = tbody.findElements(By.xpath("./tr"))
        Thread.sleep(300)
        var targetTr: WebElement? = null
        var foundNewRecord = false
        for (tr in trs) {
            if (trs[0].findElements(By.xpath("./td"))[3].findElement(By.xpath("./div")).text == "First name") {
                tr.click()
                Thread.sleep(300)
                targetTr = tr
                foundNewRecord = true
                break
            }
        }

        Assert.assertTrue(foundNewRecord)
        val targetTds = targetTr?.findElements(By.xpath("./td"))
        Assert.assertEquals(targetTds?.get(3)?.findElement(By.xpath("./div"))?.text, "First name")
        Assert.assertEquals(targetTds?.get(4)?.findElement(By.xpath("./div"))?.text, "Last name")
        Assert.assertEquals(targetTds?.get(5)?.findElement(By.xpath("./div"))?.text, "First point Second point")
        Assert.assertEquals(targetTds?.get(6)?.findElement(By.xpath("./div"))?.text, "10000$/m")

        driver.findElement(By.xpath("//*[@id=\"tables-tab-3\"]")).click()

        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"firstName\"]")).getAttribute("value"), "First name")
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"lastName\"]")).getAttribute("value"), "Last name")
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"salary\"]")).getAttribute("value"), "10000$/m")
        Thread.sleep(300)
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/div/div[2]/div/div/div/div/ul/li[1]/div/span/span")).text, "First point")
        Thread.sleep(300)
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/div/div[2]/div/div/div/div/ul/li[2]/div/span/span")).text, "Second point")
        Thread.sleep(300)

        // Click contact header to expand
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[1]/div[1]/div/a")).click()
        Thread.sleep(300)

        firstContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[1]/div[2]/div"))
        Assert.assertEquals("8-999-999-99-99", firstContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Mobile phone", firstContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Mobile phone", Select(firstContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]"))).firstSelectedOption.text)


        // Click contact header to expand
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[2]/div[1]/div/a")).click()
        Thread.sleep(300)

        secondContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[2]/div[2]/div"))
        Assert.assertEquals("8-888-888-88-88", secondContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Home phone", secondContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Home phone", Select(secondContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]"))).firstSelectedOption.text)

        // Click contact header to expand
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[3]/div[1]/div/a")).click()
        Thread.sleep(300)

        thirdContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[3]/div[2]/div"))
        Assert.assertEquals("NY, Fifth avenue", thirdContactParentBody.findElement(By.xpath(".//*[@id=\"data\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Home address", thirdContactParentBody.findElement(By.xpath(".//*[@id=\"description\"]")).getAttribute("value"))
        Thread.sleep(300)
        Assert.assertEquals("Address", Select(thirdContactParentBody.findElement(By.xpath(".//*[@id=\"type\"]"))).firstSelectedOption.text)


        // Wait until all notifications disappear
        Thread.sleep(30_000)

        // Click logout button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button[2]"))).click()
        Thread.sleep(300)
        driver.close()
        driver.quit()
    }

}
