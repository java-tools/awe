package com.almis.awe.test.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
  String startURL;

  @Value("${failsafe.timeout:30}")
  Integer timeout;

  @Value("${failsafe.browser:chrome}")
  public void setBrowser(String browser) {
    if (driver == null) {
      switch (browser) {
        case "firefox":
          WebDriverManager.firefoxdriver().setup();
          driver = new FirefoxDriver();
          break;
        case "headless-firefox":
          WebDriverManager.firefoxdriver().setup();
          FirefoxOptions firefoxOptions = new FirefoxOptions();
          firefoxOptions.addArguments("--headless");
          driver = new FirefoxDriver(firefoxOptions);
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

  protected void checkTextContains(By selector, String text) {
    assertTrue(driver.findElement(selector).getText().contains(text));
  }

  protected void checkCriterionContains(By selector, String text) {
    assertTrue(driver.findElement(selector).getAttribute("value").contains(text));
  }

  protected void sendKeys(By selector, CharSequence... text) {
    driver.findElement(selector).sendKeys(text);
  }

  protected void clearText(By selector) {
    driver.findElement(selector).clear();
  }

  protected void click(By selector) {
    driver.findElement(selector).click();
  }

  protected void gotoScreen(String... menuOptions) {
    for (String option : menuOptions) {
      // Wait for text in selector
      waitUntil(presenceOfElementLocated(By.name(option)));

      // Click on screen
      click(By.name(option));
    }
  }

  protected void waitForLoadingBar() {
    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.id("loading-bar")));  }

  protected void waitForLoadingGrid() {
    By selector = By.cssSelector(".grid-loader");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(selector));
  }

  protected void clickButton(String buttonName) {
    // Wait for element visible
    waitForButton(buttonName);

    // Click button
    click(By.cssSelector("#" + buttonName + ":not([disabled])"));
  }

  protected void clickRowContents(String search) {
    By selector = By.xpath("//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Click button
    click(selector);
  }

  protected void checkRowContents(String search) {
    By selector = By.xpath("//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkTextContains(selector, search);
  }

  protected void checkCriterionContents(String criterionName, String search) {
    By selector = By.cssSelector("[criterion-id='" + criterionName +  "'] input");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkCriterionContains(selector, search);
  }

  protected void checkSelectorContents(String criterionName, String search) {
    By selector = By.cssSelector("[criterion-id='" + criterionName +  "'] .select2-chosen");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkTextContains(selector, search);
  }

  protected void waitForButton(String buttonName) {
    // Wait for element visible
    waitUntil(visibilityOfElementLocated(By.cssSelector("#" + buttonName + ":not([disabled])")));
  }

  protected void writeText(String criterionName, String text, boolean isColumn) {
    String mainSelector = isColumn ? "column-id" : "criterion-id";
    By selector = By.cssSelector("[" + mainSelector + "='" + criterionName +  "'] input");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Clear previous text
    clearText(selector);

    // Write text
    sendKeys(selector, text);
  }

  private void selectClick(String criterionName, boolean isColumn) {
    String mainSelector = isColumn ? "column-id" : "criterion-id";
    By selector = By.cssSelector("[" + mainSelector + "='" + criterionName + "'] .select2-choice");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click selector
    click(selector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#select2-drop")));
  }

  protected void selectFirst(String criterionName, boolean isColumn) {
    // Click on selector
    selectClick(criterionName, isColumn);

    // Click option
    click(By.cssSelector("#select2-drop li:first-of-type"));
  }

  protected void selectLast(String criterionName, boolean isColumn) {
    // Click on selector
    selectClick(criterionName, isColumn);

    // Click option
    click(By.cssSelector("#select2-drop li:last-of-type"));
  }

  protected void selectContain(String criterionName, String label, boolean isColumn) {
    By selector = By.xpath("//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'" + label +"')]/..");

    // Click on selector
    selectClick(criterionName, isColumn);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  protected void suggest(String criterionName, String search, String label, boolean isColumn) {
    By selector = By.xpath("//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'" + label +"')]/..");

    // Click on selector
    selectClick(criterionName, isColumn);

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#select2-drop input.select2-input")));

    // Write username
    sendKeys(By.cssSelector("#select2-drop input.select2-input"), search);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  protected void saveLine() {
    saveLine(null);
  }

  protected void saveLine(String gridId) {
    String gridSelector = gridId == null ? "" : "[grid-id='" + gridId + "'] ";
    By selector = By.cssSelector(gridSelector + ".grid-row-save:not([disabled])");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));
  }

  protected void acceptConfirm() {
    clickButton("confirm-accept");

    // Wait for element not present
    waitUntil(invisibilityOfElementLocated(By.id("confirm-accept")));
  }

  protected void acceptMessage(String messageType) {
    By messageSelector = By.cssSelector(".alert-zone .alert-" + messageType + " button.close");

    // Wait for message selector
    waitUntil(presenceOfElementLocated(messageSelector));

    // Click on message selector
    click(messageSelector);

    // Wait for element not present
    waitUntil(invisibilityOfElementLocated(messageSelector));
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
    waitForButton("ButLogIn");

    // Write username
    writeText("cod_usr", "test", false);

    // Write password
    writeText("pwd_usr", "test", false);

    // Click button
    clickButton("ButLogIn");

    // Wait for element not visible
    waitForLoadingBar();

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#ButUsrAct span.info-text")));

    // Wait for element present
    waitUntil(textMatches(By.cssSelector("#ButUsrAct span.info-text"), Pattern.compile("Manager \\(test\\)")));

    // Assertion
    checkText(By.cssSelector("#ButUsrAct span.info-text"), "Manager (test)");
  }

  protected void doLogout() throws Exception {
    // Wait for element not visible
    waitForLoadingBar();

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#ButUsrAct span.info-text")));

    // Wait for element present
    waitUntil(textMatches(By.cssSelector("#ButUsrAct span.info-text"), Pattern.compile("Manager \\(test\\)")));

    // Wait for element present
    clickButton("ButLogOut");

    // Wait for element not visible
    waitForLoadingBar();

    // Wait for text in selector
    waitUntil(textMatches(By.cssSelector("div.slogan"), Pattern.compile("Almis Web Engine")));
  }
}
