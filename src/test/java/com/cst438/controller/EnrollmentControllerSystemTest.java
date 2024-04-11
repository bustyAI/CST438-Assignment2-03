package com.cst438.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentControllerSystemTest {

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver-win64/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final int SLEEP_DURATION = 1000; // 1 second.
    
    WebDriver driver;

    @BeforeEach
    public void setUpDriver() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(ops);
        driver.get(URL);
        Thread.sleep(SLEEP_DURATION);
    }

    @AfterEach
    public void terminateDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
            driver = null;
        }
    }

@Test
public void systemTestEnrollStudentSuccess() throws Exception {
    // Navigate to the enroll page and select a section
    // Assumes navigation to the page and the selection process is done correctly
    // ...

    // Navigate to view schedule
    driver.findElement(By.id("view_schedule")).click();
    Thread.sleep(SLEEP_DURATION);
    driver.findElement(By.id("syear")).sendKeys("2024");
    driver.findElement(By.id("ssemester")).sendKeys("Spring");
    driver.findElement(By.id("search")).click();
    Thread.sleep(SLEEP_DURATION);

    // verify that courseId: cst363 section: 9 is not in the list
    // if it exists, then delete it
    // Selenium throws NoSuchElementException when the element is not found
    try {
        while (true) {
            WebElement row363 = driver.findElement(By.xpath("//tr[td='cst363']"));
            List<WebElement> buttons = row363.findElements(By.tagName("button"));
            // delete is the first button
            assertEquals(1, buttons.size());
            buttons.get(0).click();
            Thread.sleep(SLEEP_DURATION);
            Thread.sleep(SLEEP_DURATION);
        }
    } catch (NoSuchElementException e) {
        // do nothing, continue with test
    }

    // Perform enrollment
    driver.findElement(By.id("enrollInClass")).click();
    Thread.sleep(SLEEP_DURATION);

    // select section 9
    driver.findElement(By.id("section 9")).click();
    Thread.sleep(SLEEP_DURATION);

    // enroll in section
    driver.findElement(By.id("enrollButton")).click();
    Thread.sleep(SLEEP_DURATION);

    // Navigate to view schedule
    driver.findElement(By.id("view_schedule")).click();
    Thread.sleep(SLEEP_DURATION);
    driver.findElement(By.id("syear")).sendKeys("2024");
    driver.findElement(By.id("ssemester")).sendKeys("Spring");
    driver.findElement(By.id("search")).click();
    Thread.sleep(SLEEP_DURATION);


    // verify that courseId: cst363 section: 9 is not in the list
    // if it exists, then delete it
    // Selenium throws NoSuchElementException when the element is not found
    WebElement row363 = driver.findElement(By.xpath("//tr[td='cst363']"));
    assertNotNull(row363);
    List<WebElement> buttons = row363.findElements(By.tagName("button"));
    // delete is the first button
    assertEquals(1, buttons.size());
    buttons.get(0).click();
    Thread.sleep(SLEEP_DURATION);
    Thread.sleep(SLEEP_DURATION);
    
    }

}
