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

    // Perform enrollment
    driver.findElement(By.id("enrollInClass")).click();
    Thread.sleep(SLEEP_DURATION);

    // Navigate to view schedule
    driver.findElement(By.id("view_schedule")).click();
    Thread.sleep(SLEEP_DURATION);
    driver.findElement(By.id("year_input")).sendKeys("2024");
    driver.findElement(By.id("semester_input")).sendKeys("Spring");
    driver.findElement(By.id("view_schedule_button")).click();
    Thread.sleep(SLEEP_DURATION);

    // Assert the enrolled section appears in the schedule
    WebElement enrolledSection = driver.findElement(By.xpath("//tr[td[contains(text(),'cst101')]]"));
    assertNotNull(enrolledSection);
}

@Test
public void systemTestEnrollStudentFailureAlreadyEnrolled() throws Exception {
    // Attempt to enroll in the same section again
    // ...

    // Perform enrollment again
    driver.findElement(By.id("enrollInClass")).click();
    Thread.sleep(SLEEP_DURATION);

    // Check for an error message indicating failure due to already being enrolled
    WebElement errorMessage = driver.findElement(By.id("enrollment_error"));
    String message = errorMessage.getText();
    assertEquals("Already enrolled", message);
}

}
