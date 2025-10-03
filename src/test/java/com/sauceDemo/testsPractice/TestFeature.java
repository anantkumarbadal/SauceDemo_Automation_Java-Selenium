package com.sauceDemo.testsPractice;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestFeature {

    @Test
    public void testTitle()
    {
        WebDriver driver = new ChromeDriver();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        driver.manage().window().maximize();
        driver.get("https://www.hyundaiusa.com/us/en/assurance/america-best-warranty ");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));


        System.out.printf("Title: " + driver.getTitle());

        driver.close();

    }





}
