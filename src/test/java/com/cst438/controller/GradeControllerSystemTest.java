package com.cst438.controller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GradeControllerSystemTest {

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
    public void systemTestChangeGrade() throws Exception {
        // assumes the assignment db homework 1 exists
        // changes the grade to 60
        // verifies the change and changes back to 80
        // verifies the second change

        // enter 2024 and Spring then click show Assignments
        driver.findElement(By.id("year")).sendKeys("2024");
        driver.findElement(By.id("semester")).sendKeys("Spring");
        driver.findElement(By.id("search")).click();
        Thread.sleep(SLEEP_DURATION);

        // verify that cst363 is in the list of sections
        WebElement row499 = driver.findElement(By.xpath("//tr[td='cst363']"));
        row499.findElement(By.id("8assignments")).click();
        Thread.sleep(SLEEP_DURATION);

        // open grades for assignmentId 1
        driver.findElement(By.id("1 grades")).click();
        Thread.sleep(SLEEP_DURATION);

        // change gradeid score to 60
        WebElement score = driver.findElement(By.id("2 score"));
        score.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        Thread.sleep(SLEEP_DURATION);
        score.sendKeys("60");
        
        driver.findElement(By.id("2 save")).click();
        Thread.sleep(SLEEP_DURATION);

        WebElement msg = driver.findElement(By.id("gradeMessage"));
        String message = msg.getText();
        assertEquals("Grade saved", message);

        // change gradeid score back to 80
        score = driver.findElement(By.id("2 score"));
        score.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        Thread.sleep(SLEEP_DURATION);
        score.sendKeys("80");
        
        driver.findElement(By.id("2 save")).click();
        Thread.sleep(SLEEP_DURATION);

        msg = driver.findElement(By.id("gradeMessage"));
        message = msg.getText();
        assertEquals("Grade saved", message);

    }

}
