package com.almis.awe.test.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
public class SeleniumTestsUtil {
  // Logger
  private static Logger logger = LogManager.getLogger(SeleniumTestsUtil.class);
  private static WebDriver driver;

  @Value("${selenium.start.url}")
  String startURL = "http://localhost:8080/awe-boot";

  @Value("${selenium.timeout:30}")
  Integer timeout = 30;

  @Value("${selenium.browser:chrome}")
  public void setBrowser(String browser) {
    if (driver == null) {
      switch (browser) {
        case "firefox":
          WebDriverManager.firefoxdriver().setup();
          driver = new FirefoxDriver();
          break;
        case "headless-firefox":
          WebDriverManager.firefoxdriver().setup();
          /*FirefoxOptions firefoxOptions = new FirefoxOptions();
          firefoxOptions.addArguments("--headless");
          driver = new FirefoxDriver(firefoxOptions);*/
          break;
        case "headless-chrome":
          WebDriverManager.chromedriver().setup();
          ChromeOptions options = new ChromeOptions();
          options.addArguments("--headless");
          options.addArguments("--disable-gpu");
          driver = new ChromeDriver(options);
          break;
        case "opera":
          WebDriverManager.operadriver().setup();
          driver = new OperaDriver();
          break;
        case "edge":
          WebDriverManager.edgedriver().setup();
          driver = new EdgeDriver();
          break;
        case "ie":
          WebDriverManager.iedriver().setup();
          driver = new InternetExplorerDriver();
          break;
        case "chrome":
        default:
          WebDriverManager.chromedriver().setup();
          driver = new ChromeDriver();
          break;
      }
    }
  }

  @AfterClass
  public static void cleanDrivers() {
    if (driver != null) {
      driver.quit();
    }
  }

  public WebDriver getDriver() {
    return driver;
  }

  protected void setupTimeout() {
    driver.manage().timeouts().implicitlyWait(timeout, SECONDS);
  }

  protected void waitForLoad() {
    ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    wait.until(pageLoadCondition);
  }

  protected void waitUntil(ExpectedCondition isTrue) {
    try {
      new WebDriverWait(driver, timeout).until(isTrue);
      logger.info(isTrue.toString());
    } catch (Exception exc) {
      logger.error(isTrue.toString());
      assertTrue((boolean) isTrue.apply(driver));
    }
  }

  protected void checkText(By selector, String text) {
    assertEquals(text, driver.findElement(selector).getText());
  }

  protected void sendKeys(By selector, String text) {
    driver.findElement(selector).sendKeys(text);
  }

  protected void click(By selector) {
    driver.findElement(selector).click();
  }

  protected void doLogin() throws Exception {
    assertNotNull(driver);

    // Open page in different browsers
    driver.get(startURL);

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
    waitUntil(invisibilityOfElementLocated(By.id("loading-bar")));

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#ButUsrAct span.info-text")));

    // Wait for element present
    waitUntil(textMatches(By.cssSelector("#ButUsrAct span.info-text"), Pattern.compile("Manager \\(test\\)")));

    // Assertion
    checkText(By.cssSelector("#ButUsrAct span.info-text"), "Manager (test)");
  }
}
