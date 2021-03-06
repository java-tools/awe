package com.almis.awe.testing.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
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
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.containers.Network;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * Utilities suite for selenium testing
 */
@Log4j2
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
public class SeleniumUtilities {
  private static WebDriver driver;
  private static String currentOption;
  private static final Integer RETRY_COUNT = 10;

  // Constants
  private static final String PARENT_ELEMENT = "')]/..";
  private static final String TEXT_VALUE = " text: '";
  private static final String CELL_SEARCH_CONTAINS_XPATH = "//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'";
  private static final String SELECT_DROP_CONTENTS_XPATH = "//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'";
  private static final String DAY = "day";
  private static final String MONTH = "month";
  private static final String YEAR = "year";
  private static final By BODY = By.cssSelector("body");
  private static final By DATEPICKER = By.cssSelector(".datepicker");
  private static final By GRID_LOADER_SELECTOR = By.cssSelector(".grid-loader");
  private static final By SELECT_DROP_INPUT = By.cssSelector("#select2-drop input.select2-input");
  private static final By SELECT_DROP_INPUT_NOT_HIDDEN = By.cssSelector("#select2-drop :not(.select2-search-hidden) input.select2-input");
  private static final By SELECT_DROP_RESULTS = By.cssSelector("#select2-drop .select2-results li");
  private static final ExpectedCondition<Boolean> GRID_LOADER_IS_NOT_VISIBLE = invisibilityOfElementLocated(GRID_LOADER_SELECTOR);
  private static final ExpectedCondition<Boolean> LOADER_IS_NOT_VISIBLE = invisibilityOfElementLocated(By.cssSelector(".loader"));

  @Value("${selenium.start.url}")
  private String startURL;

  @Value("${server.port}")
  private Integer serverPort;

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Value("${failsafe.timeout:30}")
  private Integer timeout;

  @Value("${browser.resolution.width}")
  private Integer browserWidth;

  @Value("${browser.resolution.height}")
  private Integer browserHeight;

  @Value("${screenshot.path}")
  private String screenshotPath;

  @Value("${failsafe.browser:headless-chrome}")
  private String browser;

  @Value("${show.mouse:true}")
  private Boolean showMouseCursor;

  /**
   * Set up test
   */
  @Before
  public void setUpTest() {
    // Setup window size
    String windowSize = "--window-size=" + browserWidth + "," + browserHeight;

    // Setup firefox options
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    FirefoxProfile firefoxProfile = new FirefoxProfile();
    firefoxProfile.setPreference("network.proxy.no_proxies_on", "localhost, 127.0.0.1");
    firefoxOptions.setProfile(firefoxProfile);
    firefoxOptions.addArguments(windowSize);
    firefoxOptions.setLogLevel(FirefoxDriverLogLevel.ERROR);

    // Setup chrome options
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments(windowSize);

    // Define browser web driver container
    if (getDriver() == null) {
      switch (browser) {
        case "docker-firefox":
          startURL = "http://" + getHost() + ":" + serverPort + contextPath;
          setDriver(getDockerWebDriver(firefoxOptions));
          break;
        case "firefox":
          WebDriverManager.firefoxdriver().setup();
          setDriver(new FirefoxDriver(firefoxOptions));
          break;
        case "headless-firefox":
          firefoxOptions.addArguments("--headless");
          WebDriverManager.firefoxdriver().setup();
          setDriver(new FirefoxDriver(firefoxOptions));
          break;
        case "docker-chrome":
          startURL = "http://" + getHost() + ":" + serverPort + contextPath;
          setDriver(getDockerWebDriver(chromeOptions));
          break;
        case "headless-chrome":
          WebDriverManager.chromedriver().setup();
          chromeOptions.setHeadless(true);
          setDriver(new ChromeDriver(chromeOptions));
          break;
        case "opera":
          WebDriverManager.operadriver().setup();
          setDriver(new OperaDriver());
          break;
        case "edge":
          WebDriverManager.edgedriver().setup();
          setDriver(new EdgeDriver());
          break;
        case "ie":
          WebDriverManager.iedriver().setup();
          setDriver(new InternetExplorerDriver());
          break;
        case "chrome":
        default:
          WebDriverManager.chromedriver().setup();
          setDriver(new ChromeDriver(chromeOptions));
          break;
      }

      // Set dimension if defined
      if (browserWidth != null && browserHeight != null) {
        driver.manage().window().setSize(new Dimension(browserWidth, browserHeight));
      }
    }
  }

  /**
   * Get browser web driver
   *
   * @return Docker webdriver
   */
  private WebDriver getDockerWebDriver(MutableCapabilities capabilities) {
    try (BrowserWebDriverContainer webDriverContainer = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
      .withCapabilities(capabilities)
      .withRecordingMode(VncRecordingMode.RECORD_FAILING, new File(screenshotPath))
      .withRecordingFileFactory(new DefaultRecordingFileFactory())
      .withPrivilegedMode(true)
      .withSharedMemorySize(2000000000L)
      .withNetwork(Network.SHARED)) {
      webDriverContainer.start();
      return webDriverContainer.getWebDriver();
    }
  }

  private String getHost() {
    return SystemUtils.IS_OS_LINUX ? "172.17.0.1" : "host.docker.internal";
  }

  /**
   * Clean driver after a test suite
   */
  @AfterClass
  public static void cleanDrivers() {
    if (getDriver() != null) {
      log.info("Disposing web driver...");
      if (getDriver() instanceof ChromeDriver) {
        getDriver().close();
      }
      getDriver().quit();
      setDriver(null);
      log.info("Web driver disposed");
    }
  }

  /**
   * Get current driver
   *
   * @return Web driver
   */
  public static WebDriver getDriver() {
    return driver;
  }

  /**
   * Set current driver
   *
   * @param newDriver New web driver
   */
  public static void setDriver(WebDriver newDriver) {
    driver = newDriver;
  }

  /**
   * Get current option
   *
   * @return Current option
   */
  public static String getCurrentOption() {
    return currentOption;
  }

  /**
   * Set current option
   *
   * @param option new option
   */
  public static void setCurrentOption(String option) {
    currentOption = option;
  }

  /**
   * Get current base url
   *
   * @return Start url
   */
  public String getBaseUrl() {
    return startURL;
  }

  /**
   * Retrieve web element from selector
   *
   * @param selector Selector
   * @return Element found
   */
  private WebElement getElement(By selector) {
    return driver.findElement(selector);
  }

  /**
   * Retrieve web element from selector
   *
   * @param selector Selector
   * @return Element found
   */
  private List<WebElement> getElements(By selector) {
    return driver.findElements(selector);
  }

  /**
   * Wait for screen load
   */
  private void waitForLoad() {
    ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
    waitUntil(pageLoadCondition);
  }

  /**
   * Wait until an expected condition
   *
   * @param condition Expected condition
   */
  private void waitUntil(ExpectedCondition<?> condition) {
    String message = condition.toString();
    try {
      new WebDriverWait(driver, timeout).until(condition);
      // Assert true on condition
      assertTrue(message, true);
      log.debug(message);
    } catch (Exception exc) {
      assertWithScreenshot(message, false, exc);
    }
  }

  /**
   * Take a screenshot when an error has occurred
   *
   * @param message   Assert message
   * @param condition Assert condition
   * @param throwable Throwable list
   */
  private void assertWithScreenshot(String message, boolean condition, Throwable... throwable) {
    if (!condition) {
      File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String messageSanitized = message
        .toLowerCase()
        .replaceAll("[\\W\\s]+", "_")
        .replaceAll("_+", "_");
      messageSanitized = messageSanitized.length() > 180 ? messageSanitized.substring(0, 180) : messageSanitized;
      String timestamp = new SimpleDateFormat("HHmmssSSS").format(new Date());
      Path path = Paths.get(screenshotPath, "screenshot-" + getCurrentOption() + "-" + timestamp + "-" + messageSanitized + ".png");
      log.error(message, (Object) throwable);
      log.error("Storing screenshot at: " + path);

      // Now you can do whatever you need to do with it, for example copy somewhere
      try {
        Files.createDirectories(path.getParent());
        FileUtils.copyFile(scrFile, path.toFile());
      } catch (IOException ioExc) {
        log.error("Error trying to store screenshot at: " + path, ioExc);
      }
    }

    // Assert false
    assertTrue(message, condition);
  }

  private boolean isWritable(By selector) {
    try {
      getElement(selector);
      return true;
    } catch (Exception exc) {
      return false;
    }
  }

  /**
   * Type keys on a criterion
   *
   * @param selector Criterion selector to type keys
   * @param text     Text to type
   */
  private void sendKeys(By selector, CharSequence... text) {
    String conditionMessage = "";
    try {
      WebElement element = getElement(selector);
      new Actions(driver)
        .sendKeys(element, text)
        .pause(200)
        .perform();

      // Assert true on condition
      assertTrue(conditionMessage, true);
    } catch (Exception exc) {
      assertWithScreenshot("Error sending keys to element: " + selector.toString() + "\n" + exc.getMessage(), false, exc);
    }
  }

  /**
   * Clear text on criterion
   *
   * @param selector Criterion selector
   */
  private void clearText(By selector) {
    getElement(selector).clear();
  }

  /**
   * Click on an element
   *
   * @param selector Element selector
   */
  private void click(By selector) {
    click(getElement(selector));
  }

  /**
   * Click on an element
   *
   * @param element Element
   */
  private void click(WebElement element) {
    String conditionMessage = "";
    try {
      new Actions(driver)
        .moveToElement(element)
        .click(element)
        .pause(100)
        .perform();

      // Assert true on condition
      assertTrue(conditionMessage, true);
    } catch (Exception exc) {
      assertWithScreenshot("Error clicking on element: " + element.toString() + "\n" + exc.getMessage(), false, exc);
    }
  }

  /**
   * Context menu on element
   *
   * @param selector Element selector
   */
  private void contextMenu(By selector) {
    String conditionMessage = "";
    try {
      WebElement element = getElement(selector);
      new Actions(driver)
        .moveToElement(element)
        .contextClick(element)
        .pause(100)
        .perform();
      assertTrue(conditionMessage, true);
    } catch (Exception exc) {
      assertWithScreenshot("Error right clicking on element: " + selector.toString() + "\n" + exc.getMessage(), false, exc);
    }
  }

  /**
   * Retrieve parent selector in css
   *
   * @param criterionName Criterion name
   * @return Css parent selector
   */
  private String getCriterionSelectorCss(String criterionName) {
    return "[criterion-id='" + criterionName + "']";
  }

  /**
   * Retrieve parent selector in css
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @return Css parent selector
   */
  private String getParentSelectorCss(String gridId, String rowId, String columnId) {
    if (rowId == null && columnId == null) {
      return getGridScopeCss(gridId) + " .ui-grid-header-checkbox label.checkbox";
    } else if (rowId == null) {
      return getGridScopeCss(gridId) + " .ui-grid-row-selected [column-id='" + columnId + "'] ";
    } else {
      return getGridScopeCss(gridId) + " [row-id='" + rowId + "'] [column-id='" + columnId + "'] ";
    }
  }

  /**
   * Get grid scope in css
   *
   * @param gridId Grid id
   * @return Grid scope string
   */
  private String getGridScopeCss(String gridId) {
    return ".grid [id='scope-" + gridId + "']";
  }

  /**
   * Retrieve parent selector in xpath
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @return Css parent selector
   */
  private String getParentSelectorXpath(String gridId, String rowId, String columnId) {
    if (rowId == null) {
      return containsGridOrTreeGrid(gridId) + "//*[contains(@class, 'ui-grid-row-selected')]//*[@column-id='" + columnId + "']";
    } else {
      return containsGridOrTreeGrid(gridId) + "//*[@row-id='" + rowId + "']//*[@column-id='" + columnId + "']";
    }
  }

  /**
   * Retrieve header column selector in xpath
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @return Css parent selector
   */
  private String getHeaderSelectorXpath(String gridId, String columnId) {
    return containsGridOrTreeGrid(gridId) + "//*[contains(@class, 'ui-grid-header-cell-row')]//*[@column-id='" + columnId + "']";
  }

  /**
   * Retrieve grid scroll zone
   *
   * @param gridId Grid identifier
   * @return Scroll zone
   */
  private By getGridScrollZone(String gridId) {
    return By.xpath(containsGridOrTreeGrid(gridId) + "//*[contains(@class, 'ui-grid-render-container-body')]//*[contains(@class, 'ui-grid-render-container')]//*[contains(@class, 'ui-grid-viewport')]");
  }


  /**
   * Retrieve parent selector in xpath
   *
   * @param gridId Grid id
   * @return Css parent selector
   */
  private String getGridSelectorXpath(String gridId) {
    if (gridId == null) {
      return "";
    } else {
      return containsGridOrTreeGrid(gridId);
    }
  }

  /**
   * Retrieve criterion css selector
   *
   * @param parentSelector Parent selector
   * @return Criterion input selector
   */
  private By getCriterionInputSelector(String parentSelector) {
    return By.cssSelector(parentSelector + " input," + parentSelector + " textarea");
  }

  /**
   * Get xpath string for grid or treegrid
   *
   * @param gridId Grid identifier
   * @return Xpath string
   */
  private String containsGridOrTreeGrid(String gridId) {
    return "//*[@grid-id='" + gridId + "' or @tree-grid-id='" + gridId + "']";
  }

  /**
   * Select a date in datepicker
   *
   * @param parentSelector Parent selector
   * @param dateValue      Date value
   */
  private void selectDateFromSelector(String parentSelector, CharSequence dateValue) {
    // Click on date
    clickDateFromSelector(parentSelector);

    // Wait until datepicker is visible
    checkVisible(DATEPICKER);

    // Write text on date
    By activeSelector = By.xpath("//*[contains(@class,'datepicker')]//*[contains(@class,'active')]");
    writeTextFromSelector(getCriterionInputSelector(parentSelector), dateValue, true, activeSelector);

    // Make click twice if datepicker is still visible
    if (!driver.findElements(activeSelector).isEmpty()) {
      clickSelector(activeSelector);
    }

    // Wait for not visible
    checkNotVisible(DATEPICKER);

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Select from datepicker
   *
   * @param parentSelector Datepicker selector
   * @param type           Select type
   * @param search         Search string
   */
  private void selectFromDatepicker(String parentSelector, String type, String search) {
    // Click on date
    clickDateFromSelector(parentSelector);

    // Wait until datepicker is visible
    waitUntil(visibilityOfElementLocated(DATEPICKER));

    // Click on selector
    click(By.xpath("//*[contains(@class,'datepicker')]//*[contains(@class,'datepicker-" + type + "s')]//*[contains(@class,'" + type + "') and not(contains(@class, 'old')) and not(contains(@class, 'new'))]//text()[.='" + search + "']/.."));

    // Wait for not visible
    checkNotVisible(DATEPICKER);
  }

  /**
   * Click on selector
   *
   * @param selector Selector
   */
  private void clickSelector(By selector) {
    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Move mouse before clicking on selector
    moveMouse();

    // Click on selector
    click(selector);
  }

  /**
   * Click on datepicker
   *
   * @param parentSelector Parent selector
   */
  private void clickDateFromSelector(String parentSelector) {
    clickSelector(By.cssSelector(parentSelector + " input"));
  }

  /**
   * Click on row
   *
   * @param parentSelector Parent selector
   */
  private void clickRowFromSelector(String parentSelector) {
    By selector = By.xpath(parentSelector);

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), GRID_LOADER_IS_NOT_VISIBLE));

    // Click button
    click(selector);
  }

  /**
   * Click on row with a text
   *
   * @param parentSelector Grid to search in
   * @param search         Text to search
   */
  private void clickRowContentsFromSelector(String parentSelector, String search) {
    clickRowFromSelector(parentSelector + CELL_SEARCH_CONTAINS_XPATH + search + PARENT_ELEMENT);
  }

  /**
   * Context menu on row
   *
   * @param parentSelector Text to search
   */
  private void contextMenuFromSelector(String parentSelector) {
    By selector = By.xpath(parentSelector);

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), GRID_LOADER_IS_NOT_VISIBLE));

    // Click button
    contextMenu(selector);
  }

  /**
   * Wait for selector to be clickable
   *
   * @param selector Selector to wait for
   */
  private void waitForSelector(By selector) {
    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Move mouse again
    moveMouse();
  }

  /**
   * Move mouse to avoid help popovers
   */
  private void moveMouse() {
    By popoverSelector = By.cssSelector(".popover:not(.ng-hide)");
    try {
      // Safecheck
      int safecheck = 0;

      // Move mouse while help is being displayed
      List<WebElement> popovers = getElements(popoverSelector);
      while (!popovers.isEmpty() && safecheck < RETRY_COUNT) {
        new Actions(driver)
          .pause(100)
          .moveToElement(popovers.get(0))
          .pause(100)
          .build()
          .perform();

        popovers = getElements(popoverSelector);
        safecheck++;
      }
    } catch (Exception exc) {
      // Assert error moving mouse
      assertWithScreenshot(exc.getMessage(), true);
    }
  }

  /**
   * Move mouse out of criterion
   */
  private void moveMouseOutOfCriterion() {
    try {
      // Move mouse out of criterion (up)
        new Actions(driver)
          .moveByOffset(0, -30)
          .click()
          .pause(100)
          .build()
          .perform();
    } catch (Exception exc) {
      // Assert error moving mouse
      assertWithScreenshot(exc.getMessage(), true);
    }
  }

  /**
   * Write text check clear text
   *
   * @param selector      Element selector
   * @param text          Text
   * @param clearText     Clear text
   * @param clickSelector Selector for click element
   */
  private void writeTextFromSelector(By selector, CharSequence text, boolean clearText, By clickSelector) {
    // Write text from selector
    writeTextFromSelector(selector, text, clearText);

    // Click on click selector
    waitUntil(elementToBeClickable(clickSelector));
    clickSelector(clickSelector);
  }

  private void writeTextFromSelector(By selector, CharSequence text, boolean clearText) {
    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Clear previous text
    if (clearText) {
      clearText(selector);
    }

    // Write text
    sendKeys(selector, text);
  }

  /**
   * Get criterion text
   *
   * @param parentSelector Parent selector
   * @return Text from criterion
   */
  private String getTextFromSelector(String parentSelector) {
    By selector = getCriterionInputSelector(parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Get selector text
    return getElement(selector).getAttribute("value");
  }

  /**
   * Click on a checkbox or a radio button
   *
   * @param parentSelector parent selector
   */
  private void clickCheckboxFromSelector(String parentSelector) {
    By selector = By.cssSelector(parentSelector + " .input label," + parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click on checkbox
    click(selector);
  }

  /**
   * Click on select box
   *
   * @param parentSelector Select box
   */
  private void selectClick(String parentSelector) {
    By selector = By.cssSelector(parentSelector + " .select2-choice");
    By loaderSelector = By.cssSelector(parentSelector + " .loader");

    // Wait for loader
    waitUntil(invisibilityOfElementLocated(loaderSelector));

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click selector
    click(selector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#select2-drop")));
  }

  /**
   * Select first value of the select
   *
   * @param parentSelector Parent selector
   */
  private void selectFirstFromSelector(String parentSelector) {
    // Click on selector
    selectClick(parentSelector);

    // Click option
    click(By.cssSelector("#select2-drop li:first-of-type"));
  }

  /**
   * Select last element
   *
   * @param parentSelector Parent selector
   */
  private void selectLastFromSelector(String parentSelector) {
    // Click on selector
    selectClick(parentSelector);

    // Click option
    click(By.cssSelector("#select2-drop li:last-of-type"));
  }

  /**
   * Select an element which contains a label
   *
   * @param parentSelector Parent selector
   * @param label          Label to search
   */
  private void selectContainFromSelector(String parentSelector, String label) {
    // Click on selector
    selectClick(parentSelector);

    // Select result on list
    selectResult(label);
  }

  /**
   * Suggest element which contains label
   *
   * @param parentSelector Parent selector
   * @param search         Search string
   * @param label          Label to search
   */
  private void suggestFromSelector(String parentSelector, String search, String label) {
    // Wait for element present
    waitUntil(LOADER_IS_NOT_VISIBLE);

    // Click on selector
    selectClick(parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(SELECT_DROP_INPUT));

    // Write text
    if (isWritable(SELECT_DROP_INPUT_NOT_HIDDEN)) {
      sendKeys(SELECT_DROP_INPUT, search);
    }

    // Wait for loading bar
    waitForLoadingBar();

    // Select result on list
    selectResult(label);
  }

  /**
   * Suggest last element which contains label
   *
   * @param parentSelector Criterion name
   * @param search         Search string
   */
  private void suggestLastFromSelector(String parentSelector, String search) {
    By selector = By.cssSelector("#select2-drop li:last-of-type .select2-result-label");

    // Wait for element present
    waitUntil(LOADER_IS_NOT_VISIBLE);

    // Click on selector
    selectClick(parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(SELECT_DROP_INPUT));

    // Write username
    sendKeys(SELECT_DROP_INPUT, search);

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  /**
   * Suggest or select multiple
   *
   * @param parentSelector Parent selector
   * @param search         Text to search
   * @param label          Text to find in label
   */
  private void suggestMultipleFromSelector(String parentSelector, boolean clear, String search, String label) {
    // Safecheck
    int safecheck = 0;
    By searchBox = By.cssSelector(parentSelector + " input.select2-input");

    // Wait for element present
    waitUntil(LOADER_IS_NOT_VISIBLE);

    // Wait for element present
    waitUntil(presenceOfElementLocated(searchBox));

    // Clear selector
    if (clear) {
      By clearSelector = By.cssSelector(parentSelector + " .select2-search-choice-close");
      while (!getElements(clearSelector).isEmpty() && safecheck < RETRY_COUNT) {
        click(clearSelector);
        safecheck++;
      }
    }

    // Write search text
    sendKeys(searchBox, search);

    // Wait for loading bar
    waitForLoadingBar();

    // Select result on list
    selectResult(label);
  }

  /**
   * Click on save row and wait
   *
   * @param parentSelector Parent selector
   */
  private void saveRowFromSelector(String parentSelector) {
    By selector = By.cssSelector(parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));
  }

  /**
   * Check text inside selector
   *
   * @param selector Selector to check
   * @param text     Text to compare
   */
  private void checkText(By selector, String text) {
    String nodeText = getElement(selector).getText();
    String message = selector.toString() + TEXT_VALUE + nodeText + "' isn't equal to " + text;

    // Assert element is not located
    assertWithScreenshot(message, nodeText.equalsIgnoreCase(text));
  }

  /**
   * Check if selector contains text
   *
   * @param selector Selector to check
   * @param text     Text to compare
   */
  private void checkTextContains(By selector, String text) {
    String nodeText = getElement(selector).getText();
    String message = selector.toString() + TEXT_VALUE + nodeText + "' doesn't contain " + text;

    // Assert element is not located
    assertWithScreenshot(message, nodeText.contains(text));
  }

  /**
   * Check if selector doesn't contain a text
   *
   * @param selector Selector to check
   * @param text     Text to compare
   */
  private void checkTextNotContains(By selector, String text) {
    String nodeText = getElement(selector).getText();
    String message = selector.toString() + TEXT_VALUE + nodeText + "' contains " + text;

    // Assert element is not located
    assertWithScreenshot(message, !nodeText.contains(text));
  }

  /**
   * Check if a criterion contains text
   *
   * @param selector Criterion selector
   * @param text     Text to compare
   */
  private void checkCriterionContains(By selector, String text) {
    String nodeText = getElement(selector).getAttribute("value");
    String message = selector.toString() + TEXT_VALUE + nodeText + "' doesn't contain " + text;

    // Assert element is not located
    assertWithScreenshot(message, nodeText.contains(text));
  }

  // ===================================================================================================================
  // Public API
  // ===================================================================================================================

  /**
   * Set test title
   *
   * @param title Test title
   */
  protected void setTestTitle(String title) {
    // Info
    log.info("======================================================================================");
    log.info("| " + title);
    log.info("======================================================================================");
  }

  /**
   * Go to a screen defined on the menu
   *
   * @param menuOptions Menu options to navigate to
   */
  protected void gotoScreen(String... menuOptions) {

    int optionNumber = 1;
    for (String option : menuOptions) {
      // Wait for text in selector
      waitUntil(visibilityOfElementLocated(By.name(option)));

      // If it is not the last option, check if it is already opened
      List<WebElement> openedChildren = getElements(By.xpath("//*[@name='" + option + "']/following-sibling::ul[contains(@class,'opened')]"));
      if (optionNumber == menuOptions.length || openedChildren.isEmpty()) {
        // Click on screen
        click(By.name(option));
      }
      setCurrentOption(option);
      optionNumber++;
    }

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".mm-dropdown-first")));

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Wait for css selector
   *
   * @param cssSelector CSS Selector
   */
  protected By waitForCssSelector(String cssSelector) {
    By selector = By.cssSelector(cssSelector);

    // Wait for selector
    waitForSelector(selector);

    // Return selector
    return selector;
  }

  /**
   * Wait for loading bar to hide
   */
  protected void waitForLoadingBar() {
    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.id("loading-bar")));
  }

  /**
   * Wait for loading grid to hide
   */
  protected void waitForLoadingGrid() {
    // Wait for element not visible
    waitUntil(GRID_LOADER_IS_NOT_VISIBLE);

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Wait for button to be clickable
   *
   * @param buttonName Button name
   */
  protected void waitForButton(String buttonName) {
    waitForSelector(By.cssSelector("#" + buttonName + ":not([disabled])"));
  }

  /**
   * Wait for context button to be clickable
   *
   * @param buttonName Button name
   */
  protected void waitForContextButton(String buttonName) {
    // Wait some milliseconds
    pause(100);

    // Wait for context button
    waitForSelector(By.cssSelector(".context-menu [option-id='" + buttonName + "'] a:not([disabled])"));
  }

  /**
   * Wait for text inside a tag with a CSS class
   *
   * @param clazz    CSS class
   * @param contains Text to check
   */
  protected void waitForText(String clazz, String contains) {
    By selector = By.xpath("//*[contains(@class,'" + clazz + "')]//text()[contains(.,'" + contains + PARENT_ELEMENT);

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));
  }

  /**
   * Wait for text inside a tag with a CSS class
   *
   * @param selector Selector
   * @param contains Text to check
   */
  protected void waitForText(By selector, String contains) {
    // Wait for element visible
    waitUntil(textToBePresentInElementLocated(selector, contains));
  }

  /**
   * Wait for text inside a tag with a CSS class
   *
   * @param selector Selector
   * @param contains Text to check
   */
  protected void waitForValue(By selector, String contains) {
    // Wait for element visible
    waitUntil(textToBePresentInElementValue(selector, contains));
  }

  /**
   * Pause
   *
   * @param time Milliseconds
   */
  protected void pause(Integer time) {
    new Actions(driver)
      .pause(time)
      .build()
      .perform();
  }

  /**
   * Click on an element
   *
   * @param cssSelector Input selector
   */
  protected void click(String cssSelector) {
    click(By.cssSelector(cssSelector));
  }

  /**
   * Click on a button
   *
   * @param buttonName Button name
   */
  protected void clickButton(String buttonName) {
    clickButton(buttonName, false);
  }

  /**
   * Click on a button
   *
   * @param buttonName        Button name
   * @param waitForLoadingBar Wait for loading bar after clicking
   */
  protected void clickButton(String buttonName, boolean waitForLoadingBar) {
    // Wait for element visible
    waitForButton(buttonName);

    // Click button
    By selector = By.cssSelector("#" + buttonName + ":not([disabled])");
    clickSelector(selector);

    if (waitForLoadingBar) {
      // Wait for loading bar
      waitForLoadingBar();
    } else {
      // Move mouse
      moveMouse();
    }
  }

  /**
   * Click on a context button
   *
   * @param contextButtonOptionList Context button option list
   */
  protected void clickContextButton(String... contextButtonOptionList) {
    By contextButtonSelector = null;
    for (String contextButtonOption : contextButtonOptionList) {
      // Set context button name
      contextButtonSelector = By.cssSelector(".context-menu [option-id='" + contextButtonOption + "'] a:not([disabled])");

      // Wait for context button
      waitForContextButton(contextButtonOption);

      // Mouse over context button
      new Actions(driver)
        .moveToElement(driver.findElement(contextButtonSelector))
        .build()
        .perform();
    }

    // Click on last option
    if (contextButtonSelector != null) {
      // Click button
      clickSelector(contextButtonSelector);
    }
  }

  /**
   * Click on tab
   *
   * @param tabName  Tab name
   * @param tabLabel Tab label local
   */
  protected void clickTab(String tabName, String tabLabel) {
    // Tab selector
    clickSelector(By.cssSelector(getCriterionSelectorCss(tabName) + " span[translate-multiple*='" + tabLabel + "']"));
    By tabActive = By.cssSelector(getCriterionSelectorCss(tabName) + " li.active span[translate-multiple*='" + tabLabel + "']");

    // Wait for tab active
    waitUntil(visibilityOfElementLocated(tabActive));
  }

  /**
   * Click on info button
   *
   * @param infoButtonName Button name
   */
  protected void clickInfoButton(String infoButtonName) {
    clickSelector(By.cssSelector("[info-dropdown-id='" + infoButtonName + "'] a"));
  }

  /**
   * Click on tree button
   *
   * @param gridId Grid id
   * @param rowId  Row id
   */
  protected void clickTreeButton(String gridId, String rowId) {

    // Click on tree button
    clickSelector(By.cssSelector("[tree-grid-id='" + gridId + "'] [row-id='" + rowId + "'] i.tree-icon"));

    // Check loader is not visible
    checkNotVisible(".fa-spin");

    // Pause to wait tree leaf to open
    pause(250);
  }

  /**
   * Click on datepicker
   *
   * @param criterionName Datepicker name
   */
  protected void clickDate(String criterionName) {
    clickDateFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Click on datepicker on grid
   *
   * @param gridId   Grid id
   * @param columnId Column id
   */
  protected void clickDate(String gridId, String columnId) {
    clickDateFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Click on datepicker on grid
   *
   * @param gridId   Grid id
   * @param rowId    row id
   * @param columnId Column id
   */
  protected void clickDate(String gridId, String rowId, String columnId) {
    clickDateFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Select a date in datepicker
   *
   * @param dateName  Datepicker name
   * @param dateValue Date to select
   */
  protected void selectDate(String dateName, CharSequence dateValue) {
    selectDateFromSelector(getCriterionSelectorCss(dateName), dateValue);
  }

  /**
   * Select a date in a grid
   *
   * @param gridId    Grid id
   * @param columnId  Column id
   * @param dateValue Date to select
   */
  protected void selectDate(String gridId, String columnId, CharSequence dateValue) {
    // Select date with parent selector
    selectDateFromSelector(getParentSelectorCss(gridId, null, columnId), dateValue);
  }

  /**
   * Select a date in a grid
   *
   * @param gridId    Grid id
   * @param rowId     Row id
   * @param columnId  Column id
   * @param dateValue Date to select
   */
  protected void selectDate(String gridId, String rowId, String columnId, CharSequence dateValue) {
    // Select date with parent selector
    selectDateFromSelector(getParentSelectorCss(gridId, rowId, columnId), dateValue);
  }

  /**
   * Select a day in datepicker (current month)
   *
   * @param dateName Datepicker name
   * @param day      Day to select
   */
  protected void selectDay(String dateName, @Nonnull Integer day) {
    selectFromDatepicker(getCriterionSelectorCss(dateName), DAY, day.toString());
  }

  /**
   * Select a day in datepicker (current month) in a grid
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param day      Day to select
   */
  protected void selectDay(String gridId, String columnId, @Nonnull Integer day) {
    // Select date with parent selector
    selectFromDatepicker(getParentSelectorCss(gridId, null, columnId), DAY, day.toString());
  }

  /**
   * Select a day in datepicker (current month) in a grid
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param day      Day to select
   */
  protected void selectDay(String gridId, String rowId, String columnId, @Nonnull Integer day) {
    // Select date with parent selector
    selectFromDatepicker(getParentSelectorCss(gridId, rowId, columnId), DAY, day.toString());
  }

  /**
   * Retrieve today day of month
   *
   * @return Day of month as string
   */
  protected Integer getTodayDay() {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retrieve today day of month
   *
   * @return Day of month as string
   */
  protected Integer getTomorrowDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Select a month in datepicker
   *
   * @param dateName Datepicker name
   * @param month    Month to select
   */
  protected void selectMonth(String dateName, String month) {
    selectFromDatepicker(getCriterionSelectorCss(dateName), MONTH, month);
  }

  /**
   * Select a month in datepicker in a grid
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param month    Month to select
   */
  protected void selectMonth(String gridId, String columnId, String month) {
    // Select date with parent selector
    selectFromDatepicker(getParentSelectorCss(gridId, null, columnId), MONTH, month);
  }

  /**
   * Select a month in datepicker in a grid
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param month    Month to select
   */
  protected void selectMonth(String gridId, String rowId, String columnId, String month) {
    // Select date with parent selector
    selectFromDatepicker(getParentSelectorCss(gridId, rowId, columnId), MONTH, month);
  }

  /**
   * Select a year in datepicker
   *
   * @param dateName Datepicker name
   * @param year     Year to select
   */
  protected void selectYear(String dateName, @Nonnull Integer year) {
    selectFromDatepicker(getCriterionSelectorCss(dateName), YEAR, year.toString());
  }

  /**
   * Select a year in datepicker in a grid
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param year     Year to select
   */
  protected void selectYear(String gridId, String columnId, @Nonnull Integer year) {
    // Select date with parent selector
    selectFromDatepicker(getParentSelectorCss(gridId, null, columnId), YEAR, year.toString());
  }

  /**
   * Select a year in datepicker in a grid
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param year     Year to select
   */
  protected void selectYear(String gridId, String rowId, String columnId, @Nonnull Integer year) {
    // Select date with parent selector
    selectFromDatepicker(getParentSelectorCss(gridId, rowId, columnId), YEAR, year.toString());
  }

  /**
   * Click on a checkbox or a radio button
   *
   * @param criterionName Criterion name
   */
  protected void clickCheckbox(String criterionName) {
    clickCheckboxFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Click on a checkbox or a radio button
   *
   * @param gridId   Grid id
   * @param columnId Column id
   */
  protected void clickCheckbox(String gridId, String columnId) {
    clickCheckboxFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Click on a checkbox or a radio button
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   */
  protected void clickCheckbox(String gridId, String rowId, String columnId) {
    clickCheckboxFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Click on row with a text
   *
   * @param search Text to search
   */
  protected void clickRowContents(String search) {
    clickRowContentsFromSelector("", search);
  }

  /**
   * Click on row with a text
   *
   * @param gridId Grid to search in
   * @param search Text to search
   */
  protected void clickRowContents(String gridId, String search) {
    clickRowContentsFromSelector(getGridSelectorXpath(gridId), search);
  }

  /**
   * Click on a cell on selected row
   *
   * @param gridId   Grid id
   * @param columnId Column id
   */
  protected void clickCell(String gridId, String columnId) {
    clickRowFromSelector(getParentSelectorXpath(gridId, null, columnId));
  }

  /**
   * Click on a grid cell
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   */
  protected void clickCell(String gridId, String rowId, String columnId) {
    clickRowFromSelector(getParentSelectorXpath(gridId, rowId, columnId));
  }

  /**
   * Context menu on row
   *
   * @param search Text to search
   */
  protected void contextMenuRowContents(String search) {
    contextMenuRowContents(null, search);
  }

  /**
   * Context menu on row
   *
   * @param gridId Grid identifier
   * @param search Text to search
   */
  protected void contextMenuRowContents(String gridId, String search) {
    contextMenuFromSelector(getGridSelectorXpath(gridId) + CELL_SEARCH_CONTAINS_XPATH + search + PARENT_ELEMENT);
  }


  /**
   * Context menu on a grid
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   */
  protected void contextMenu(String gridId, String rowId, String columnId) {
    contextMenuFromSelector(getParentSelectorXpath(gridId, rowId, columnId));
  }

  /**
   * Type keys on a criterion
   *
   * @param selector Criterion selector to type keys
   * @param text     Text to type
   */
  protected void writeTextOnDriver(By selector, CharSequence... text) {
    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Write text
    getElement(selector).sendKeys(text);
  }

  /**
   * Write text on selector
   *
   * @param selector Selector
   * @param text     Text
   */
  protected void writeText(By selector, CharSequence text) {
    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Write text
    sendKeys(selector, text);
  }

  /**
   * Write text on criterion
   *
   * @param criterionName Criterion name
   * @param text          Text
   */
  protected void writeText(String criterionName, CharSequence text) {
    writeText(criterionName, text, true);
  }

  /**
   * Write text check clear text
   *
   * @param criterionName Criterion name
   * @param text          Text
   * @param clearText     Clear text
   */
  protected void writeText(String criterionName, CharSequence text, boolean clearText) {
    writeTextFromSelector(getCriterionInputSelector(getCriterionSelectorCss(criterionName)), text, clearText);
    moveMouseOutOfCriterion();
  }

  /**
   * Write text check clear text
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param text     Text to write
   */
  protected void writeText(String gridId, String columnId, CharSequence text) {
    // Write text on grid
    writeText(gridId, null, columnId, text, true);
  }


  /**
   * Write text check clear text
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param text     Text to write
   */
  protected void writeText(String gridId, String rowId, String columnId, CharSequence text) {
    // Write text on grid
    writeText(gridId, rowId, columnId, text, true);
  }

  /**
   * Write text check clear text
   *
   * @param gridId    Grid id
   * @param rowId     Row id
   * @param columnId  Column id
   * @param text      Text to write
   * @param clearText Clear previous text
   */
  protected void writeText(String gridId, String rowId, String columnId, CharSequence text, boolean clearText) {
    // Write text on grid
    By selector = getCriterionInputSelector(getParentSelectorCss(gridId, rowId, columnId));
    writeTextFromSelector(selector, text, clearText, selector);
  }

  /**
   * Clear text on input selector
   *
   * @param cssSelector Input selector
   */
  protected void clearText(String cssSelector) {
    clearText(By.cssSelector(cssSelector));
  }

  /**
   * Get criterion text
   *
   * @param criterionName Criterion name
   * @return Text from criterion
   */
  protected String getText(String criterionName) {
    return getTextFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Get selected row cell text
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @return Cell text
   */
  protected String getText(String gridId, String columnId) {
    return getTextFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Get grid cell text
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @return Cell text
   */
  protected String getText(String gridId, String rowId, String columnId) {
    return getTextFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Select first value of the select
   *
   * @param criterionName Criterion name
   */
  protected void selectFirst(String criterionName) {
    selectFirstFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Select first value of the select
   *
   * @param gridId   Grid id
   * @param columnId Column id
   */
  protected void selectFirst(String gridId, String columnId) {
    selectFirstFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Select first value of the select
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   */
  protected void selectFirst(String gridId, String rowId, String columnId) {
    selectFirstFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Select first value of the select
   *
   * @param criterionName Criterion name
   */
  protected void selectLast(String criterionName) {
    selectLastFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Select first value of the select
   *
   * @param gridId   Grid id
   * @param columnId Column id
   */
  protected void selectLast(String gridId, String columnId) {
    selectLastFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Select first value of the select
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   */
  protected void selectLast(String gridId, String rowId, String columnId) {
    selectLastFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Select value on the selector
   *
   * @param criterionName Criterion name
   * @param label         Label to search
   */
  protected void selectContain(String criterionName, String label) {
    selectContainFromSelector(getCriterionSelectorCss(criterionName), label);
  }

  /**
   * Select value on the selector
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param label    Label to search
   */
  protected void selectContain(String gridId, String columnId, String label) {
    selectContainFromSelector(getParentSelectorCss(gridId, null, columnId), label);
  }

  /**
   * Select value on the selector
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param label    Label to search
   */
  protected void selectContain(String gridId, String rowId, String columnId, String label) {
    selectContainFromSelector(getParentSelectorCss(gridId, rowId, columnId), label);
  }

  /**
   * Select all rows of grid
   *
   * @param gridId Grid id
   */
  protected void selectAllRowsOfGrid(String gridId) {
    String parentSelector = getParentSelectorCss(gridId, null, null);

    By selector = By.cssSelector(parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click on checkbox
    click(selector);

  }

  /**
   * Select result on select/suggest list
   *
   * @param match Match label
   */
  protected void selectResult(String match) {
    By selector = By.xpath(SELECT_DROP_CONTENTS_XPATH + match + PARENT_ELEMENT);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  /**
   * Suggest element which contains label
   *
   * @param criterionName Criterion name
   * @param search        Search string
   * @param label         Label to search
   */
  protected void suggest(String criterionName, String search, String label) {
    suggestFromSelector(getCriterionSelectorCss(criterionName), search, label);
  }

  /**
   * Suggest element which contains label
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param search   Search string
   * @param label    Label to search
   */
  protected void suggest(String gridId, String columnId, String search, String label) {
    suggestFromSelector(getParentSelectorCss(gridId, null, columnId), search, label);
  }

  /**
   * Suggest element which contains label
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param search   Search string
   * @param label    Label to search
   */
  protected void suggest(String gridId, String rowId, String columnId, String search, String label) {
    suggestFromSelector(getParentSelectorCss(gridId, rowId, columnId), search, label);
  }

  /**
   * Suggest element which contains label
   *
   * @param criterionName Criterion name
   * @param search        Search string
   */
  protected void suggestLast(String criterionName, String search) {
    suggestLastFromSelector(getCriterionSelectorCss(criterionName), search);
  }

  /**
   * Suggest element which contains label
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param search   Search string
   */
  protected void suggestLast(String gridId, String columnId, String search) {
    suggestLastFromSelector(getParentSelectorCss(gridId, null, columnId), search);
  }

  /**
   * Suggest element which contains label
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param search   Search string
   */
  protected void suggestLast(String gridId, String rowId, String columnId, String search) {
    suggestLastFromSelector(getParentSelectorCss(gridId, rowId, columnId), search);
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param criterionName Criterion name
   * @param items         Items to add and search for
   */
  protected void suggestMultipleList(String criterionName, String... items) {
    boolean clear = true;
    for (String item : items) {
      suggestMultiple(criterionName, clear, item, item);
      clear = false;
    }
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param criterionName Criterion name
   * @param search        Search string
   * @param label         Text to find in label
   */
  protected void suggestMultiple(String criterionName, String search, String label) {
    suggestMultiple(criterionName, true, search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param search   Search string
   * @param label    Text to find in label
   */
  protected void suggestMultiple(String gridId, String columnId, String search, String label) {
    suggestMultiple(gridId, null, columnId, true, search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param search   Search string
   * @param label    Text to find in label
   */
  protected void suggestMultiple(String gridId, String rowId, String columnId, String search, String label) {
    suggestMultiple(gridId, rowId, columnId, true, search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param criterionName Criterion name
   * @param search        Search string
   * @param label         Text to find in label
   */
  protected void suggestMultiple(String criterionName, boolean clear, String search, String label) {
    suggestMultipleFromSelector(getCriterionSelectorCss(criterionName), clear, search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param gridId   Grid id
   * @param columnId Column id
   * @param search   Search string
   * @param label    Text to find in label
   */
  protected void suggestMultiple(String gridId, String columnId, boolean clear, String search, String label) {
    suggestMultipleFromSelector(getParentSelectorCss(gridId, null, columnId), clear, search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param search   Search string
   * @param label    Text to find in label
   */
  protected void suggestMultiple(String gridId, String rowId, String columnId, boolean clear, String search, String label) {
    suggestMultipleFromSelector(getParentSelectorCss(gridId, rowId, columnId), clear, search, label);
  }

  /**
   * Click on search button (ButSch) and wait the grid to load
   */
  protected void searchAndWait() {
    searchAndWait("ButSch");
  }

  /**
   * Click on search button and wait the grid to load
   *
   * @param buttonName Button name
   */
  protected void searchAndWait(String buttonName) {
    clickButton(buttonName, false);

    // Wait for loading bar
    waitForLoadingGrid();

    // Move mouse
    moveMouse();
  }

  /**
   * Click on save row and wait
   */
  protected void saveRow() {
    saveRowFromSelector(".grid-row-save:not([disabled])");
  }

  /**
   * Click on save row and wait
   *
   * @param gridId Grid with the save button
   */
  protected void saveRow(String gridId) {
    saveRowFromSelector("#" + gridId + "-grid-row-save:not([disabled])");
  }


  /**
   * Scroll grid
   *
   * @param gridId     Grid identifier
   * @param horizontal Horizontal scroll in pixels
   * @param vertical   Vertical scroll in pixels
   */
  protected void scrollGrid(String gridId, int horizontal, int vertical) {
    JavascriptExecutor js = ((JavascriptExecutor) driver);
    WebElement grid = driver.findElement(getGridScrollZone(gridId));
    js.executeScript("arguments[0].scrollTo(arguments[1], arguments[2]);", grid, horizontal, vertical);
  }

  /**
   * Show mouse
   */
  protected void showMouse() {
    JavascriptExecutor js = ((JavascriptExecutor) driver);
    js.executeScript("var seleniumFollowerImg=document.createElement(\"span\");" +
      "seleniumFollowerImg.setAttribute('id', 'selenium_mouse');" +
      "seleniumFollowerImg.setAttribute('style', 'position: absolute; z-index: 99999999999; pointer-events: none; transition: all .1s ease, text-shadow .1s linear; -moz-transition: all .01s linear, text-shadow .1s linear; color: white;-webkit-text-stroke-width: 2px;-webkit-text-stroke-color: #000;');" +
      "$(seleniumFollowerImg).addClass('fa fa-mouse-pointer fa-2x');" +
      "document.body.appendChild(seleniumFollowerImg);" +
      "$(document).mousemove(function(e) {" +
      "$('#selenium_mouse').css({'left': e.pageX + 'px', 'top': e.pageY + 'px'});" +
      "});" +
      "$(document).click(function(e) {" +
      "$('#selenium_mouse').css({'text-shadow': '0 0 20px blue', 'color': 'blue'});" +
      "setTimeout(function() {$('#selenium_mouse').css({'text-shadow': '0 0 0px blue', 'color': 'white'});}, 100);" +
      "});");
  }

  /**
   * Click on column header
   *
   * @param gridId   Grid identifier
   * @param columnId Column identifier
   */
  protected void sortGrid(String gridId, String columnId) {
    // Click on header
    clickRowFromSelector(getHeaderSelectorXpath(gridId, columnId));

    // Wait for loading bar
    waitForLoadingGrid();
  }

  /**
   * Accept confirm window and wait for it to disappear
   */
  protected void acceptConfirm() {
    // Pause 300 ms
    pause(300);

    // Click on button
    clickButton("confirm-accept");

    // Wait for element not present
    waitUntil(invisibilityOfElementLocated(By.id("confirm-accept")));
  }

  /**
   * Check a message box and close it
   *
   * @param messageType Message type (success (default), info, warning, danger)
   */
  protected void checkAndCloseMessage(String messageType) {
    By messageSelector = By.cssSelector(".alert-zone .alert-" + messageType + " button.close");

    // Pause 250 ms
    pause(250);

    // Wait for message selector
    waitUntil(elementToBeClickable(messageSelector));

    // Click on message selector
    click(messageSelector);

    // Wait for element not present
    waitUntil(invisibilityOfElementLocated(messageSelector));

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Click on confirm button, accept confirmation and accept message
   *
   * @param button Button name
   */
  protected void clickButtonAndConfirm(String button) {
    clickButtonAndConfirm(button, "success");
  }

  /**
   * Click on confirm button, accept confirmation and accept message
   *
   * @param button      Button name
   * @param messageType Message type (info, warning, success, danger)
   */
  protected void clickButtonAndConfirm(String button, String messageType) {
    // Click on button
    clickButton(button);

    // Accept confirm
    acceptConfirm();

    // Accept message
    checkAndCloseMessage(messageType);
  }

  /**
   * Check text inside css selector
   *
   * @param cssSelector Selector to check
   * @param text        Text to compare
   */
  protected void checkText(String cssSelector, String text) {
    // Check selector text
    checkText(waitForCssSelector(cssSelector), text);
  }

  /**
   * Check text inside css selector
   *
   * @param cssSelector Selector to check
   * @param text        Text to compare
   */
  protected void checkTextContains(String cssSelector, String text) {
    // Check selector text
    checkTextContains(waitForCssSelector(cssSelector), text);
  }

  /**
   * heck if selector doesn't contain a text
   *
   * @param cssSelector Selector to check
   * @param text        Text to compare
   */
  protected void checkTextNotContains(String cssSelector, String text) {
    checkTextNotContains(waitForCssSelector(cssSelector), text);
  }

  /**
   * Check if grid contains some texts
   *
   * @param searchList Texts to search for in the grid
   */
  protected void checkRowContents(String... searchList) {
    checkRowContentsGrid(null, searchList);
  }

  /**
   * Check if grid contains some texts
   *
   * @param gridId     Grid Identifier
   * @param searchList Texts to search for in the grid
   */
  protected void checkRowContentsGrid(String gridId, String... searchList) {
    String gridSelector = getGridSelectorXpath(gridId);
    for (String search : searchList) {
      By selector = By.xpath(gridSelector + CELL_SEARCH_CONTAINS_XPATH + search + PARENT_ELEMENT);

      // Wait for element visible
      waitUntil(and(visibilityOfElementLocated(selector), GRID_LOADER_IS_NOT_VISIBLE));

      // Check text
      checkTextContains(selector, search);
    }
  }

  /**
   * Check cell contents
   *
   * @param gridId   Grid id
   * @param rowId    Row id
   * @param columnId Column id
   * @param search   Search value
   */
  protected void checkCellContents(String gridId, String rowId, String columnId, String search) {
    By selector = By.xpath(getParentSelectorXpath(gridId, rowId, columnId) + "//text()[contains(.,'" + search + PARENT_ELEMENT);

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), GRID_LOADER_IS_NOT_VISIBLE));

    // Check text
    checkTextContains(selector, search);
  }

  /**
   * Check if grid doesn't contain some texts
   *
   * @param search Texts to search for in the grid
   */
  protected void checkRowNotContains(String search) {
    By selector = By.xpath(CELL_SEARCH_CONTAINS_XPATH + search + PARENT_ELEMENT);

    ExpectedCondition<Boolean> condition = and(invisibilityOfElementLocated(selector), GRID_LOADER_IS_NOT_VISIBLE);

    // Assert element is not located
    assertWithScreenshot(condition.toString(), condition.apply(driver));
  }

  /**
   * Assert if a criterion contains a text
   *
   * @param criterionName Criterion name
   * @param search        Text to check
   */
  protected void checkCriterionContents(String criterionName, String search) {
    By selector = getCriterionInputSelector(getCriterionSelectorCss(criterionName));

    // Wait for element visible
    waitUntil(presenceOfElementLocated(selector));

    // Check text
    checkCriterionContains(selector, search);
  }

  /**
   * Assert if some criteria are checked or not
   *
   * @param isChecked     Flag to check
   * @param criteriaNames Elements to check
   */
  protected void checkCheckboxRadio(boolean isChecked, String... criteriaNames) {
    String checkedSelector = isChecked ? ":checked" : ":not(:checked)";
    String activeSelector = isChecked ? ".active" : ":not(.active)";
    for (String criterionName : criteriaNames) {
      By selector = By.cssSelector(
        getCriterionSelectorCss(criterionName) + " .input label input" + checkedSelector + "," +
          getCriterionSelectorCss(criterionName) + activeSelector);

      // Wait for element visible
      waitUntil(presenceOfElementLocated(selector));
    }
  }

  /**
   * Assert if a selector contains a text
   *
   * @param criterionName Selector name
   * @param search        Text to check
   */
  protected void checkSelectContents(String criterionName, String search) {
    By selector = By.cssSelector(getCriterionSelectorCss(criterionName) + " .select2-chosen");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkTextContains(selector, search);
  }

  /**
   * Assert if a selector contains a number of results
   *
   * @param criterionName Selector name
   * @param number        Number of results to check
   */
  protected void checkSelectNumberOfResults(String criterionName, Integer number) {
    selectClick(getCriterionSelectorCss(criterionName));

    // Assert element is not located
    assertWithScreenshot("Number of elements doesn't match", number == getElements(SELECT_DROP_RESULTS).size());
  }

  /**
   * Assert if a selector contains a text
   *
   * @param criterionName Selector name
   * @param search        Text to check
   */
  protected void checkMultipleSelectorContents(String criterionName, String search) {
    By selector = By.cssSelector(getCriterionSelectorCss(criterionName) + " .select2-search-choice div");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkTextContains(selector, search);
  }

  /**
   * Check if message is missing
   *
   * @param messageType Message type
   */
  protected void checkMessageMissing(String messageType) {
    By messageSelector = By.cssSelector(".alert-zone .alert-" + messageType + " button.close");

    // Wait 1 second
    pause(1000);
    List<WebElement> messages = getElements(messageSelector);

    // Check there are no messages of messageType
    assertEquals(0, messages.size());
  }

  /**
   * Check element is present
   *
   * @param cssSelector CSS selector
   */
  protected void checkPresence(String cssSelector) {
    By selector = By.cssSelector(cssSelector);

    // Wait until visible
    waitUntil(presenceOfElementLocated(selector));
  }

  /**
   * Check element is visible
   *
   * @param cssSelector CSS selector
   */
  protected void checkVisible(String cssSelector) {
    checkVisible(By.cssSelector(cssSelector));
  }

  /**
   * Check element is visible
   *
   * @param selector Selector
   */
  protected void checkVisible(By selector) {
    // Wait until visible
    waitUntil(visibilityOfElementLocated(selector));
  }

  /**
   * Check element is visible
   *
   * @param cssSelector CSS selector
   * @param search      Search string
   */
  protected void checkVisibleAndContains(String cssSelector, String search) {
    // Check if it is visible
    checkVisible(cssSelector);

    // Check text contains
    checkTextContains(By.cssSelector(cssSelector), search);
  }

  /**
   * Check element is not visible
   *
   * @param cssSelector CSS selector
   */
  protected void checkNotVisible(String cssSelector) {
    checkNotVisible(By.cssSelector(cssSelector));
  }

  /**
   * Check element is not visible
   *
   * @param selector selector
   */
  protected void checkNotVisible(By selector) {
    // Wait until visible
    waitUntil(invisibilityOfElementLocated(selector));
  }

  /**
   * Log into the application
   *
   * @param username    User name
   * @param password    Password
   * @param cssSelector Selector to check
   * @param checkText   Text to check inside selector
   */
  protected void checkLogin(String username, String password, String cssSelector, String checkText) {
    assertNotNull(driver);

    log.info("Launching tests with '" + browser + "' browser: " + startURL);

    // Set driver timeout
    driver.manage().timeouts().setScriptTimeout(timeout, SECONDS);

    // Open page in different browsers
    driver.get(startURL);

    // Show mouse if defined
    if (showMouseCursor) {
      showMouse();
    }

    // Wait for load
    waitForLoad();

    // Test title
    setTestTitle("Login test: Log into the application");

    // Wait for element present
    waitForButton("ButLogIn");

    // Write username
    writeText("cod_usr", username);

    // Write password
    writeText("pwd_usr", password);

    // Click button
    clickButton("ButLogIn", true);

    // Wait for element present
    waitForSelector(By.cssSelector(cssSelector));

    // Assertion
    checkText(cssSelector, checkText);
  }

  /**
   * Log out the application
   *
   * @param cssSelector Selector to check
   * @param checkText   Text to check inside selector
   */
  protected void checkLogout(String cssSelector, String checkText) {
    // Test title
    setTestTitle("Logout test: Log out the application");

    // Wait for element not visible
    waitForLoadingBar();

    // Wait for element present
    clickButton("ButLogOut", true);

    // Wait for text in selector
    checkText(cssSelector, checkText);
  }

  /**
   * Select module in module list
   *
   * @param moduleName Module name
   */
  protected void selectModule(String moduleName) {
    // Click on info button
    clickInfoButton("ButSetTog");

    // Suggest
    suggest("module", moduleName, moduleName);

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Broadcast a message to a user
   *
   * @param user User name
   * @param text Text to send
   */
  protected void broadcastMessageToUser(String user, String text) {
    // Go to broadcast screen
    gotoScreen("tools", "broadcast-messages");

    // Suggest
    suggest("MsgTar", user, user);

    // Write on criterion
    writeText("MsgDes", text);

    // Search and wait
    clickButton("ButSnd");

    // Accept message
    checkAndCloseMessage("success");

    // Accept message
    checkAndCloseMessage("info");
  }
}
