package com.cst438.controller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AssignmentControllerSystemTest {

    // TODO edit the following to give the location and file name
    // of the Chrome driver.
    //  for WinOS the file name will be chromedriver.exe
    //  for MacOS the file name will be chromedriver
    public static final String CHROME_DRIVER_FILE_LOCATION =
        "C:/chromedriver-win64/chromedriver.exe";

    //public static final String CHROME_DRIVER_FILE_LOCATION =
    //        "~/chromedriver_macOS/chromedriver";
    public static final String URL = "http://localhost:3000";

    public static final int SLEEP_DURATION = 1000; // 1 second.


    // add selenium dependency to pom.xml

    // these tests assumes that test data does NOT contain any
    // Assignments for course cst499 in 2024 Spring term.

    WebDriver driver;

    @BeforeEach
    public void setUpDriver() throws Exception {

        // set properties required by Chrome Driver
        System.setProperty(
                "webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--remote-allow-origins=*");

        // start the driver
        driver = new ChromeDriver(ops);

        driver.get(URL);
        // must have a short wait to allow time for the page to download
        Thread.sleep(SLEEP_DURATION);

    }

    @AfterEach
    public void terminateDriver() {
        if (driver != null) {
            // quit driver
            driver.close();
            driver.quit();
            driver = null;
        }
    }

    @Test
    public void systemTestAddAssignment() throws Exception {
        // assumes the date 04012028 has not passed
        // add an assignment title, date, and section number
        // verify section shows on the list of sections for Spring 2024
        // delete the section
        // verify the section is gone

        // enter 2024 and Spring then click show Assignments
        driver.findElement(By.id("year")).sendKeys("2024");
        driver.findElement(By.id("semester")).sendKeys("Spring");
        driver.findElement(By.id("search")).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that cst363 is in the list of sections
        WebElement row499 = driver.findElement(By.xpath("//tr[td='cst363']"));
        row499.findElement(By.id("8assignments")).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that db homework 1 assignment is not in the list of assignments
        // if it exists, then delete it
        // Selenium throws NoSuchElementException when the element is not found
        try {
            while (true) {
                WebElement dbhw1 = driver.findElement(By.xpath("//tr[td='db homework 3']"));
                List<WebElement> buttons = dbhw1.findElements(By.tagName("button"));
                // delete is the second button
                assertEquals(2, buttons.size());
                buttons.get(1).click();
                Thread.sleep(SLEEP_DURATION);
                // find the YES to confirm button
                List<WebElement> confirmButtons = driver
                        .findElement(By.className("react-confirm-alert-button-group"))
                        .findElements(By.tagName("button"));
                assertEquals(2, confirmButtons.size());
                confirmButtons.get(0).click();
                Thread.sleep(SLEEP_DURATION);
            }
        } catch (NoSuchElementException e) {
           // do nothing, continue with test
        }

        // find and click button to add a section
        driver.findElement(By.id("addAssignment")).click();
        Thread.sleep(SLEEP_DURATION);

        // enter data
        //  title: db homework 1,
        driver.findElement(By.id("title")).sendKeys("db homework 3");
        //  date: 04-1-2024,
        driver.findElement(By.id("date")).sendKeys("04012028");

        // click Save
        driver.findElement(By.id("save")).click();
        Thread.sleep(SLEEP_DURATION);

        String message = driver.findElement(By.id("addMessage")).getText();
        assertTrue(message.startsWith("Assignment added successfully"));

        // close the dialog
        driver.findElement(By.id("close")).click();

        // verify that new Section shows up on Sections list
        // find the row for cst499
        WebElement dbhw1 = driver.findElement(By.xpath("//tr[td='db homework 3']"));
        List<WebElement> buttons = dbhw1.findElements(By.tagName("button"));
        // delete is the second button
        assertEquals(2, buttons.size());
        buttons.get(1).click();
        Thread.sleep(SLEEP_DURATION);
        // find the YES to confirm button
        List<WebElement> confirmButtons = driver
                .findElement(By.className("react-confirm-alert-button-group"))
                .findElements(By.tagName("button"));
        assertEquals(2, confirmButtons.size());
        confirmButtons.get(0).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that Section list is now empty
        assertThrows(NoSuchElementException.class, () ->
                driver.findElement(By.xpath("//tr[td='db homework 3']")));
    }

   @Test
    public void systemTestAddSectionBadDate() throws Exception {
        // assumes the date 04012028 has not passed
        // add an assignment title 'db homework 3' and date
        // fails because assignment date is before current date
        // change value to '04012028' and try again
        // verify success
        // delete the section

        // enter 2024 and Spring then click show Assignments
        driver.findElement(By.id("year")).sendKeys("2024");
        driver.findElement(By.id("semester")).sendKeys("Spring");
        driver.findElement(By.id("search")).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that cst363 is in the list of sections
        WebElement row499 = driver.findElement(By.xpath("//tr[td='cst363']"));
        row499.findElement(By.id("8assignments")).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that db homework 1 assignment is not in the list of assignments
        // if it exists, then delete it
        // Selenium throws NoSuchElementException when the element is not found
        try {
            while (true) {
                WebElement dbhw1 = driver.findElement(By.xpath("//tr[td='db homework 3']"));
                List<WebElement> buttons = dbhw1.findElements(By.tagName("button"));
                // delete is the second button
                assertEquals(2, buttons.size());
                buttons.get(1).click();
                Thread.sleep(SLEEP_DURATION);
                // find the YES to confirm button
                List<WebElement> confirmButtons = driver
                        .findElement(By.className("react-confirm-alert-button-group"))
                        .findElements(By.tagName("button"));
                assertEquals(2, confirmButtons.size());
                confirmButtons.get(0).click();
                Thread.sleep(SLEEP_DURATION);
            }
        } catch (NoSuchElementException e) {
           // do nothing, continue with test
        }

        // find and click button to add a section
        driver.findElement(By.id("addAssignment")).click();
        Thread.sleep(SLEEP_DURATION);

        // enter data
        //  title: db homework 1,
        driver.findElement(By.id("title")).sendKeys("db homework 3");
        //  date: 04-1-2024,
        driver.findElement(By.id("date")).sendKeys("01092022");
        
        // click Save
        driver.findElement(By.id("save")).click();
        Thread.sleep(SLEEP_DURATION);
        
        WebElement msg = driver.findElement(By.id("addMessage"));
        String message = msg.getText();
        assertEquals("invalid due date 2022-01-08", message);

        // clear the date field and enter 04012028
        WebElement date = driver.findElement(By.id("date"));
        date.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        Thread.sleep(SLEEP_DURATION);
        date.sendKeys("04012028");

        // click Save
        driver.findElement(By.id("save")).click();
        Thread.sleep(SLEEP_DURATION);

        message = driver.findElement(By.id("addMessage")).getText();
        assertTrue(message.startsWith("Assignment added successfully"));

        // close the dialog
        driver.findElement(By.id("close")).click();

        // verify that new Section shows up on Sections list
        // find the row for cst499
        WebElement dbhw1 = driver.findElement(By.xpath("//tr[td='db homework 3']"));
        List<WebElement> buttons = dbhw1.findElements(By.tagName("button"));
        // delete is the second button
        assertEquals(2, buttons.size());
        buttons.get(1).click();
        Thread.sleep(SLEEP_DURATION);
        // find the YES to confirm button
        List<WebElement> confirmButtons = driver
                .findElement(By.className("react-confirm-alert-button-group"))
                .findElements(By.tagName("button"));
        assertEquals(2, confirmButtons.size());
        confirmButtons.get(0).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that Section list is now empty
        assertThrows(NoSuchElementException.class, () ->
                driver.findElement(By.xpath("//tr[td='db homework 3']")));
       
               
    }
}
