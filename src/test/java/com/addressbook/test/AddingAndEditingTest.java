package com.addressbook.test;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddingAndEditingTest {

    @Test
    public void stage1_addAndEditOrganization() throws InterruptedException {
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
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/div/button")));
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
        for (WebElement tr : trs) {
            if (tr.findElements(By.xpath("./td")).get(2).getText().equals(name)) {
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
        for (WebElement tr : trs) {
            if (tr.findElements(By.xpath("./td")).get(2).getText().equals(name)) {
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
        for (WebElement tr : trs) {
            if (tr.findElements(By.xpath("./td")).get(2).getText().equals(name)) {
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
        for (WebElement tr : trs) {
            if (tr.findElements(By.xpath("./td")).get(2).getText().equals(name)) {
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

    @Test
    public void stage1_addAndEditPerson() throws InterruptedException {
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
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/div/button")));
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
        // Find table
        WebElement tbody = driver.findElement(By.xpath("//*[@id=\"tables-pane-1\"]/div[2]/div/div[1]/div[2]/table/tbody"));
        // Select topmost record in the table
        tbody.findElements(By.xpath("./tr")).get(0).findElements(By.xpath("./td")).get(2).click();
        // Click second tab - Persons
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-2\"]"))).click();
        Thread.sleep(2_000);
        // Click create person button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button[1]"))).click();
        Thread.sleep(1_000);
        // Click third tab - New person
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tables-tab-3\"]"))).click();
        Thread.sleep(1_000);
        driver.findElement(By.xpath("//*[@id=\"firstName\"]")).sendKeys("First name");
        driver.findElement(By.xpath("//*[@id=\"lastName\"]")).sendKeys("Last name");
        driver.findElement(By.xpath("//*[@id=\"salary\"]")).sendKeys("10000$/m");

        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/div/div[1]/div[2]/div[1]/button/span")).click();
        Thread.sleep(300);
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[1]/div/div[2]/div/div[2]/div")).sendKeys("First point", Keys.ENTER, "Second point");
        Thread.sleep(300);


        // Add contact
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/button")).click();
        Thread.sleep(300);
        // New contact header
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[1]/div[1]/div/a")).click();
        Thread.sleep(300);

        //Enter mobile phone
        WebElement firstContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[1]/div[2]/div"));
        firstContactParentBody.findElement(By.xpath("//*[@id=\"data\"]")).sendKeys("8-999-999-99-99");
        Thread.sleep(300);
        firstContactParentBody.findElement(By.xpath("//*[@id=\"description\"]")).sendKeys("Mobile phone");
        Thread.sleep(300);



        // Add contact
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/button")).click();
        Thread.sleep(300);
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[2]/div[1]/div/a")).click();
        Thread.sleep(300);

        WebElement secondContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[2]/div[2]/div"));
        Thread.sleep(300);
        // org.openqa.selenium.ElementNotInteractableException: element not interactable
        Select role = new Select(secondContactParentBody.findElement(By.xpath("//*[@id=\"type\"]")));
        role.selectByValue("3");
        Thread.sleep(300);
        secondContactParentBody.findElement(By.xpath("//*[@id=\"data\"]")).sendKeys("8-888-888-88-88");
        Thread.sleep(300);
        secondContactParentBody.findElement(By.xpath("//*[@id=\"description\"]")).sendKeys("Home phone");
        Thread.sleep(300);

        // Add contact
        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/button")).click();

        driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[3]/div[1]/div/a")).click();
        Thread.sleep(300);

        WebElement thirdContactParentBody = driver.findElement(By.xpath("//*[@id=\"tables-pane-3\"]/div/div[2]/div/div[3]/div[2]/div"));
        Thread.sleep(300);
        thirdContactParentBody.findElement(By.xpath("//*[@id=\"type\"]/option[3]")).click();
        Thread.sleep(300);
        thirdContactParentBody.findElement(By.xpath("//*[@id=\"data\"]")).sendKeys("NY, Fifth avenue");
        Thread.sleep(300);
        thirdContactParentBody.findElement(By.xpath("//*[@id=\"description\"]")).sendKeys("Home address");
        Thread.sleep(300);

//        // Wait until all notifications disappear
//        Thread.sleep(30_000);
//
//        // Click logout button
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"application\"]/div/div/nav/div/div/ul[2]/button[2]"))).click();
//        Thread.sleep(300);
//        driver.quit();
    }

}
