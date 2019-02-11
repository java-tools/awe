package com.almis.awe.test.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
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

  @Value("${browser.resolution.width}")
  Integer browserWidth;

  @Value("${browser.resolution.height}")
  Integer browserHeight;

  @Value("${screenshot.path}")
  String screenshotPath;

  // Browser
  String browser;

  @Value("${failsafe.browser:headless-chrome}")
  public void setBrowser(String browser) {
    if (driver == null) {
      this.browser = browser;
      switch (browser) {
        case "firefox":
          WebDriverManager.firefoxdriver().setup();
          FirefoxOptions firefoxOptions = new FirefoxOptions();
          FirefoxProfile firefoxProfile = new FirefoxProfile();
          firefoxProfile.setPreference("network.proxy.no_proxies_on", "localhost, 127.0.0.1");
          firefoxOptions.setProfile(firefoxProfile);
          driver = new FirefoxDriver(firefoxOptions);
          break;
        case "headless-firefox":
          WebDriverManager.firefoxdriver().setup();
          firefoxOptions = new FirefoxOptions();
          firefoxOptions.addArguments("--headless");
          firefoxProfile = new FirefoxProfile();
          firefoxProfile.setPreference("network.proxy.no_proxies_on", "localhost, 127.0.0.1");
          firefoxOptions.setProfile(firefoxProfile);
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

    // Set dimension if defined
    if (browserWidth != null && browserHeight != null) {
      driver.manage().window().setSize(new Dimension(browserWidth, browserHeight));
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

  protected void waitForLoad() {
    ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
    waitUntil(pageLoadCondition);
  }

  /**
   * Take a screenshot when an error has occurred
   * @param message
   */
  private void manageError(String message) {
    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    String messageSanitized = message
      .toLowerCase()
      .replaceAll("[\\W\\s]", "_")
      .replaceAll("_+", "_")
      .replaceAll("_build_info.*", "")
      .replaceAll("_session_info.*", "");
    String timestamp = new SimpleDateFormat("HHmmssSSS").format(new Date());
    Path path = Paths.get(screenshotPath, "screenshot-" + timestamp + "-" + messageSanitized + ".png");
    logger.error(message);
    logger.error("Storing screenshot at: " + path);

    // Now you can do whatever you need to do with it, for example copy somewhere
    try {
      path.toFile().getParentFile().mkdirs();
      FileUtils.copyFile(scrFile, path.toFile());
    } catch (IOException ioExc) {
      logger.error("Error trying to store screenshot at: " + path);
    }
  }

  protected void waitUntil(ExpectedCondition isTrue) {
    String message = isTrue.toString();
    try {
      new WebDriverWait(driver, timeout).until(isTrue);
      logger.info(message);
    } catch (Exception exc) {
      manageError(message);
      assert (!(boolean) isTrue.apply(driver)) : isTrue.toString();
    }
  }

  protected void checkText(By selector, String text) {
    boolean condition = driver.findElement(selector).getText().equals(text);

    // Assert element is not located
    if (!condition) {
      String conditionMessage = driver.findElement(selector).getText() + " isn't equal to " + text;
      manageError(conditionMessage);
      assert false : conditionMessage;
    }
  }

  protected void checkTextContains(By selector, String text) {
    boolean condition = driver.findElement(selector).getText().contains(text);

    // Assert element is not located
    if (!condition) {
      String conditionMessage = selector.toString() + " doesn't contain " + text;
      manageError(conditionMessage);
      assert false : conditionMessage;
    }
  }

  protected void checkTextNotContains(By selector, String text) {
    boolean condition = driver.findElement(selector).getText().contains(text);

    // Assert element is not located
    if (!condition) {
      String conditionMessage = selector.toString() + " contains " + text;
      manageError(conditionMessage);
      assert false : conditionMessage;
    }
  }

  protected void checkCriterionContains(By selector, String text) {
    boolean condition = driver.findElement(selector).getAttribute("value").contains(text);

    // Assert element is not located
    if (!condition) {
      String conditionMessage = selector.toString() + " doesn't contain " + text;
      manageError(conditionMessage);
      assert false : conditionMessage;
    }
  }

  protected void sendKeys(By selector, CharSequence... text) {
    driver.findElement(selector).sendKeys(text);
  }

  protected void clearText(By selector) {
    driver.findElement(selector).clear();
  }

  protected void click(By selector) {
    try {
      driver.findElement(selector).click();
    } catch (Exception exc) {
      manageError(exc.getMessage());
      assert false : "Error clicking element: " + selector.toString() + "\n" + exc.getMessage();
    }
  }

  protected void gotoScreen(String... menuOptions) {
    for (String option : menuOptions) {
      // Wait for text in selector
      waitUntil(visibilityOfElementLocated(By.name(option)));

      // Click on screen
      click(By.name(option));
    }

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".mm-dropdown-first")));

    // Wait for loading bar
    waitForLoadingBar();
  }

  protected void waitForLoadingBar() {
    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.id("loading-bar")));
  }

  protected void waitForLoadingGrid() {
    By selector = By.cssSelector(".grid-loader");

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(selector));

    // Wait for loading bar
    waitForLoadingBar();
  }

  protected void clickButton(String buttonName) {
    clickButton(buttonName, false);
  }

  protected void clickButton(String buttonName, boolean waitForLoadingBar) {
    // Wait for element visible
    waitForButton(buttonName);

    // Click button
    click(By.cssSelector("#" + buttonName + ":not([disabled])"));

    if (waitForLoadingBar) {
      // Wait for loading bar
      waitForLoadingBar();
    }
  }

  protected void searchAndWait() {
    searchAndWait("ButSch");
  }

  protected void searchAndWait(String buttonName) {
    clickButton(buttonName, false);

    // Wait for loading bar
    waitForLoadingGrid();
  }

  protected void clickRowContents(String search) {
    By selector = By.xpath("//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader"))));

    // Click button
    click(selector);
  }

  protected void checkRowContents(String... searchList) {
    for (String search : searchList) {
      By selector = By.xpath("//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search + "')]/..");

      // Wait for element visible
      waitUntil(and(visibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader"))));

      // Check text
      checkTextContains(selector, search);
    }
  }

  protected void checkRowNotContains(String search) {
    By selector = By.xpath("//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");

    ExpectedCondition<Boolean> condition = and(invisibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader")));

    // Assert element is not located
    if (!condition.apply(driver).booleanValue()) {
      manageError(condition.toString());
      assert false : condition.toString();
    }
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
    // Safecheck
    Integer safecheck = 0;

    // Wait for element visible
    waitUntil(elementToBeClickable(By.cssSelector("#" + buttonName + ":not([disabled])")));

    // Move mouse while help is being displayed
    List<WebElement> popovers = driver.findElements(By.cssSelector(".popover:not(.ng-hide)"));
    while (popovers.size() > 0 && safecheck < 5) {
      WebElement element = popovers.get(0);
      new Actions(driver)
        .moveByOffset(30, 30)
        .build()
        .perform();
      popovers = driver.findElements(By.cssSelector(".popover:not(.ng-hide)"));
      safecheck++;
    }
  }

  protected void waitForText(String clazz, String contains) {
    By selector = By.xpath("//*[contains(@class,'" + clazz + "')]//text()[contains(.,'" + contains +"')]/..");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));
  }

  protected void writeText(String criterionName, String text, boolean isColumn) {
    String mainSelector = isColumn ? "column-id" : "criterion-id";
    By selector = By.cssSelector("[" + mainSelector + "='" + criterionName +  "'] input,[" + mainSelector + "='" + criterionName +  "'] textarea");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Clear previous text
    clearText(selector);

    // Write text
    sendKeys(selector, text);
  }

  protected void clickCheckbox(String criterionName, boolean isColumn) {
    String mainSelector = isColumn ? "column-id" : "criterion-id";
    By selector = By.cssSelector("[" + mainSelector + "='" + criterionName +  "'] .input label");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click on checkbox
    click(selector);
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

    // Wait for element present
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".loader")));

    // Click on selector
    selectClick(criterionName, isColumn);

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#select2-drop input.select2-input")));

    // Write username
    sendKeys(By.cssSelector("#select2-drop input.select2-input"), search);

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  protected void suggestMultiple(String criterionName, String search, String label, boolean isColumn) {
    // Safecheck
    Integer safecheck = 0;

    By selector = By.xpath("//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'" + label +"')]/..");
    String mainSelector = isColumn ? "column-id" : "criterion-id";
    By searchBox = By.cssSelector("[" + mainSelector + "='" + criterionName +  "'] input.select2-input");

    // Wait for element present
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".loader")));

    // Wait for element present
    waitUntil(presenceOfElementLocated(searchBox));

    // Clear selector
    By clearSelector = By.cssSelector("[" + mainSelector + "='" + criterionName +  "'] .select2-search-choice-close");
    while (driver.findElements(clearSelector).size() > 0 && safecheck < 10) {
      click(clearSelector);
      safecheck++;
    }

    // Write username
    sendKeys(searchBox, search);

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  protected void saveRow() {
    saveRow(null);
  }

  protected void saveRow(String gridId) {
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
    waitUntil(visibilityOfElementLocated(messageSelector));

    // Click on message selector
    click(messageSelector);

    // Wait for element not present
    waitUntil(invisibilityOfElementLocated(messageSelector));

    // Wait for loading bar
    waitForLoadingBar();
  }

  protected void doLogin() throws Exception {
    assertNotNull(driver);

    logger.info("Launching tests with '" + browser + "' browser");
    System.out.println("Launching tests with '" + browser + "' browser");

    // Set driver timeout
    driver.manage().timeouts().setScriptTimeout(timeout, SECONDS);

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
