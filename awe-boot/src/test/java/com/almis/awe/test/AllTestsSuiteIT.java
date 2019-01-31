package com.almis.awe.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
public class AllTestsSuiteIT {
  // Logger
  private static Logger logger = LogManager.getLogger(AllTestsSuiteIT.class);

  private List<WebDriver> driverList;

  @Value("${selenium.start.url}")
  String startURL;

  @Value("${selenium.timeout:30}")
  Integer timeout;

  @BeforeClass
  public static void setupClass() {
    WebDriverManager.chromedriver().setup();
    WebDriverManager.firefoxdriver().setup();
  }

  @Before
  public void setupTest() {
    driverList = new ArrayList<>();
    driverList.add(new ChromeDriver());
    driverList.add(new FirefoxDriver());
  }

  @After
  public void teardown() {
    for (WebDriver driver : driverList) {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  private void setupTimeout() {
    for (WebDriver driver : driverList) {
      driver.manage().timeouts().implicitlyWait(timeout, SECONDS);
    }
  }

  private void waitForLoad() {
    for (WebDriver driver : driverList) {
      ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(pageLoadCondition);
    }
  }

  private void waitUntil(ExpectedCondition isTrue) {
    for (WebDriver driver : driverList) {
      new WebDriverWait(driver, 30).pollingEvery(Duration.ofMillis(100)).until(isTrue);
    }
  }

  private void checkText(By selector, String text) {
    for (WebDriver driver : driverList) {
      assertEquals(text, driver.findElement(selector).getText());
    }
  }

  private void sendKeys(By selector, String text) {
    for (WebDriver driver : driverList) {
      driver.findElement(selector).sendKeys(text);
    }
  }

  private void click(By selector) {
    for (WebDriver driver : driverList) {
      driver.findElement(selector).click();
    }
  }

  @Test
  public void loginTest() throws Exception {
    // Test data
    int timeout = 30;

    // Implicit timeout
    setupTimeout();

    // Open page in different browsers
    for (WebDriver driver : driverList) {
      driver.get(startURL);
    }

    // Wait for load
    waitForLoad();

    // Wait for text in selector
    waitUntil(textMatches(By.cssSelector("div.slogan"), Pattern.compile("Almis Web Engine")));

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#ButLogIn:not([disabled])")));

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("[criterion-id='cod_usr']")));

    // Write username
    sendKeys(By.cssSelector("[criterion-id='cod_usr'] input"), "test");

    // Write password
    sendKeys(By.cssSelector("[criterion-id='pwd_usr'] input"), "test");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(By.cssSelector("#ButLogIn:not([disabled])")));

    // Click button
    click(By.cssSelector("#ButLogIn:not([disabled])"));

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.cssSelector("#loading-bar")));

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#ButUsrAct span.info-text")));

    // Wait for element present
    waitUntil(textMatches(By.cssSelector("#ButUsrAct span.info-text"), Pattern.compile("Manager \\(test\\)")));

    // Assertion
    checkText(By.cssSelector("#ButUsrAct span.info-text"), "Manager (test)");
  }
}

