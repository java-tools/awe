package com.almis.awe.test.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
public class SeleniumTestsUtil {
  // Logger
  private Logger logger = LogManager.getLogger(SeleniumTestsUtil.class);
  private static WebDriver driver;
  private static final Integer RETRY_COUNT = 10;

  @Value("${selenium.start.url}")
  private String startURL;

  @Value("${failsafe.timeout:30}")
  private Integer timeout;

  @Value("${browser.resolution.width}")
  private Integer browserWidth;

  @Value("${browser.resolution.height}")
  private Integer browserHeight;

  @Value("${screenshot.path}")
  private String screenshotPath;

  // Browser
  private String browser;

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
      driver = null;
    }
  }

  /**
   * Get current driver
   * @return
   */
  public WebDriver getDriver() {
    return driver;
  }

  /**
   * Get current base url
   * @return
   */
  public String getBaseUrl() {
    return startURL;
  }

  /**
   * Wait for screen load
   */
  private void waitForLoad() {
    ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
    waitUntil(pageLoadCondition);
  }

  /**
   * Take a screenshot when an error has occurred
   * @param message
   */
  private void assertWithScreenshot(String message, boolean condition, Throwable... throwable) {
    if (!condition) {
      File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String messageSanitized = message
        .toLowerCase()
        .replaceAll("[\\W\\s]", "_")
        .replaceAll("_+", "_")
        .replaceAll("_build_info.*", "")
        .replaceAll("_session_info.*", "");
      messageSanitized = messageSanitized.length() > 180 ? messageSanitized.substring(0, 180) : messageSanitized;
      String timestamp = new SimpleDateFormat("HHmmssSSS").format(new Date());
      Path path = Paths.get(screenshotPath, "screenshot-" + timestamp + "-" + messageSanitized + ".png");
      logger.error(message, throwable);
      logger.error("Storing screenshot at: " + path);

      // Now you can do whatever you need to do with it, for example copy somewhere
      try {
        path.toFile().getParentFile().mkdirs();
        FileUtils.copyFile(scrFile, path.toFile());
      } catch (IOException ioExc) {
        logger.error("Error trying to store screenshot at: " + path, ioExc);
      }
    }

    // Assert false
    assertTrue(message, condition);
  }

  /**
   * Wait until an expected condition
   * @param condition Expected condition
   */
  protected void waitUntil(ExpectedCondition condition) {
    String message = condition.toString();
    try {
      new WebDriverWait(driver, timeout).until(condition);
      // Assert true on condition
      assertTrue(message, true);
      logger.log(Level.DEBUG, message);
    } catch (Exception exc) {
      assertWithScreenshot(message, false, exc);
    }
  }

  /**
   * Check text inside selector
   * @param selector Selector to check
   * @param text Text to compare
   */
  protected void checkText(By selector, String text) {
    String nodeText = driver.findElement(selector).getText();
    String message = selector.toString() + " text: '" + nodeText + " isn't equal to " + text;

    // Assert element is not located
    assertWithScreenshot(message, nodeText.equalsIgnoreCase(text));
  }

  /**
   * Check if selector contains text
   * @param selector Selector to check
   * @param text Text to compare
   */
  protected void checkTextContains(By selector, String text) {
    String nodeText = driver.findElement(selector).getText();
    String message = selector.toString() + " text: '" + nodeText + "'  doesn't contain " + text;

    // Assert element is not located
    assertWithScreenshot(message, nodeText.contains(text));
  }

  /**
   * Check if selector doesn't contain a text
   * @param selector Selector to check
   * @param text Text to compare
   */
  protected void checkTextNotContains(By selector, String text) {
    String nodeText = driver.findElement(selector).getText();
    String message = selector.toString() + " text: '" + nodeText + " contains " + text;

    // Assert element is not located
    assertWithScreenshot(message, !nodeText.contains(text));
  }

  /**
   * Check if a criterion contains text
   * @param selector Criterion selector
   * @param text Text to compare
   */
  protected void checkCriterionContains(By selector, String text) {
    String nodeText = driver.findElement(selector).getAttribute("value");
    String message = selector.toString() + " text: '" + nodeText + " doesn't contain " + text;

    // Assert element is not located
    assertWithScreenshot(message, nodeText.contains(text));
  }

  /**
   * Type keys on a criterion
   * @param selector Criterion selector to type keys
   * @param text Text to type
   */
  protected void sendKeys(By selector, CharSequence... text) {
    driver.findElement(selector).sendKeys(text);
  }

  /**
   * Clear text on criterion
   * @param selector Criterion selector
   */
  protected void clearText(By selector) {
    driver.findElement(selector).clear();
  }

  /**
   * Click on an element
   * @param selector Element selector
   */
  protected void click(By selector) {
    String conditionMessage = "";
    try {
      driver.findElement(selector).click();
      // Assert true on condition
      assertTrue(conditionMessage, true);
    } catch (Exception exc) {
      assertWithScreenshot( "Error clicking on element: " + selector.toString() + "\n" + exc.getMessage(), false, exc);
    }
  }

  /**
   * Context menu on element
   * @param selector Element selector
   */
  protected void contextMenu(By selector) {
    String conditionMessage = "";
    try {
      new Actions(driver)
        .moveToElement(driver.findElement(selector))
        .contextClick()
        .build()
        .perform();
      assertTrue(conditionMessage, true);
    } catch (Exception exc) {
      assertWithScreenshot("Error right clicking on element: " + selector.toString() + "\n" + exc.getMessage(), false, exc);
    }
  }

  /**
   * Go to a screen defined on the menu
   * @param menuOptions Menu options to navigate to
   */
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
    By selector = By.cssSelector(".grid-loader");

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(selector));

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Click on a button
   * @param buttonName Button name
   */
  protected void clickButton(String buttonName) {
    clickButton(buttonName, false);
  }

  /**
   * Click on a button
   * @param buttonName Button name
   * @param waitForLoadingBar Wait for loading bar after clicking
   */
  protected void clickButton(String buttonName, boolean waitForLoadingBar) {
    // Wait for element visible
    waitForButton(buttonName);

    // Click button
    clickSelector(By.cssSelector("#" + buttonName + ":not([disabled])"));

    if (waitForLoadingBar) {
      // Wait for loading bar
      waitForLoadingBar();
    }
  }

  /**
   * Click on a context button
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
   * @param tabName Tab name
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
   * @param infoButtonName Button name
   */
  protected void clickInfoButton(String infoButtonName) {
    clickSelector(By.cssSelector("[info-dropdown-id='" + infoButtonName + "'] a"));
  }

  /**
   * Click on tree button
   * @param gridId Grid id
   * @param rowId Row id
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
   * Retrieve parent selector in css
   * @param criterionName Criterion name
   * @return Css parent selector
   */
  private String getCriterionSelectorCss(String criterionName) {
      return "[criterion-id='" + criterionName + "']";
  }

  /**
   * Retrieve parent selector in css
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @return Css parent selector
   */
  private String getParentSelectorCss(String gridId, String rowId, String columnId) {
    if (rowId == null) {
      return ".grid [id='scope-" + gridId + "'] .ui-grid-row-selected [column-id='" + columnId + "'] ";
    } else {
      return ".grid [id='scope-" + gridId + "'] [row-id='" + rowId + "'] [column-id='" + columnId + "'] " ;
    }
  }

  /**
   * Retrieve parent selector in xpath
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @return Css parent selector
   */
  private String getParentSelectorXpath(String gridId, String rowId, String columnId) {
    if (rowId == null) {
      return "//*[contains(@grid-id, '" + gridId + "') or contains(@tree-grid-id, '" + gridId + "')]//*[contains(@class, 'ui-grid-row-selected')]//*[contains(@column-id, '" + columnId + "')]";
    } else {
      return "//*[contains(@grid-id, '" + gridId + "') or contains(@tree-grid-id, '" + gridId + "')]//*[contains(@row-id, '" + rowId + "')]//*[contains(@column-id, '" + columnId + "')]";
    }
  }

  /**
   * Retrieve parent selector in xpath
   * @param gridId Grid id
   * @return Css parent selector
   */
  private String getGridSelectorXpath(String gridId) {
    if (gridId == null) {
      return "";
    } else {
      return "//*[contains(@grid-id, '" + gridId + "') or contains(@tree-grid-id, '" + gridId + "')]";
    }
  }

  /**
   * Click on datepicker
   * @param criterionName Datepicker name
   */
  protected void clickDate(String criterionName) {
    clickDateFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Click on datepicker on grid
   * @param gridId Grid id
   * @param columnId Column id
   */
  protected void clickDate(String gridId, String columnId) {
    clickDateFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Click on datepicker on grid
   * @param gridId Grid id
   * @param rowId row id
   * @param columnId Column id
   */
  protected void clickDate(String gridId, String rowId, String columnId) {
    clickDateFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Click on datepicker
   * @param parentSelector Parent selector
   */
  private void clickDateFromSelector(String parentSelector) {
    clickSelector(By.cssSelector(parentSelector + " input"));
  }

  /**
   * Select a date in datepicker
   * @param dateName Datepicker name
   * @param dateValue Date to select
   */
  protected void selectDate(String dateName, CharSequence dateValue) {
    selectDateFromSelector(getCriterionSelectorCss(dateName), dateValue);
  }

  /**
   * Select a date in a grid
   * @param gridId Grid id
   * @param columnId Column id
   * @param dateValue Date to select
   */
  protected void selectDate(String gridId, String columnId, CharSequence dateValue) {
    // Select date with parent selector
    selectDateFromSelector(getParentSelectorCss(gridId, null, columnId), dateValue);
  }

  /**
   * Select a date in a grid
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param dateValue Date to select
   */
  protected void selectDate(String gridId, String rowId, String columnId, CharSequence dateValue) {
    // Select date with parent selector
    selectDateFromSelector(getParentSelectorCss(gridId, rowId, columnId), dateValue);
  }

  /**
   * Select a date in datepicker
   * @param parentSelector Parent selector
   * @param dateValue Date value
   */
  protected void selectDateFromSelector(String parentSelector, CharSequence dateValue) {
    // Click on date
    clickDateFromSelector(parentSelector);

    // Write text on date
    writeTextFromSelector(parentSelector, dateValue, true);

    // Click on selector
    clickSelector(By.cssSelector(".datepicker td.day:not(.disabled)"));
  }

  /**
   * Click on selector
   * @param selector Selector
   */
  protected void clickSelector(By selector) {
    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Click on selector
    click(selector);
  }

  /**
   * Click on search button (ButSch) and wait the grid to load
   */
  protected void searchAndWait() {
    searchAndWait("ButSch");
  }

  /**
   * Click on search button and wait the grid to load
   * @param buttonName Button name
   */
  protected void searchAndWait(String buttonName) {
    clickButton(buttonName, false);

    // Wait for loading bar
    waitForLoadingGrid();
  }

  /**
   * Click on row with a text
   * @param search Text to search
   */
  protected void clickRowContents(String search) {
    clickRowContentsFromSelector("", search);
  }

  /**
   * Click on row with a text
   * @param gridId Grid to search in
   * @param search Text to search
   */
  protected void clickRowContents(String gridId, String search) {
    clickRowContentsFromSelector(getGridSelectorXpath(gridId), search);
  }

  /**
   * Click on row with a text
   * @param gridId Grid id
   * @param columnId Column id
   */
  protected void clickRow(String gridId, String columnId) {
    clickRowFromSelector(getParentSelectorXpath(gridId, null, columnId));
  }

  /**
   * Click on row with a text
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   */
  protected void clickRow(String gridId, String rowId, String columnId) {
    clickRowFromSelector(getParentSelectorXpath(gridId, rowId, columnId));
  }

  /**
   * Click on row
   * @param parentSelector Parent selector
   */
  private void clickRowFromSelector(String parentSelector) {
    By selector = By.xpath(parentSelector);

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader"))));

    // Click button
    click(selector);
  }

  /**
   * Click on row with a text
   * @param parentSelector Grid to search in
   * @param search Text to search
   */
  private void clickRowContentsFromSelector(String parentSelector, String search) {
    clickRowFromSelector(parentSelector + "//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");
  }

  /**
   * Context menu on row
   * @param search Text to search
   */
  protected void contextMenuRowContents(String search) {
    contextMenuRowContents(null, search);
  }

  /**
   * Context menu on row
   * @param search Text to search
   */
  protected void contextMenuRowContents(String gridId, String search) {
    contextMenuFromSelector(getGridSelectorXpath(gridId) + "//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");
  }


  /**
   * Context menu on a grid
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   */
  protected void contextMenu(String gridId, String rowId, String columnId) {
    contextMenuFromSelector(getParentSelectorXpath(gridId, rowId, columnId));
  }

  /**
   * Context menu on row
   * @param parentSelector Text to search
   */
  protected void contextMenuFromSelector(String parentSelector) {
    By selector = By.xpath(parentSelector);

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader"))));

    // Click button
    contextMenu(selector);
  }

  /**
   * Check if grid contains some texts
   * @param searchList Texts to search for in the grid
   */
  protected void checkRowContents(String... searchList) {
    checkRowContentsGrid(null, searchList);
  }

  /**
   * Check if grid contains some texts
   * @param gridId Grid Identifier
   * @param searchList Texts to search for in the grid
   */
  protected void checkRowContentsGrid(String gridId, String... searchList) {
    String gridSelector = getGridSelectorXpath(gridId);
    for (String search : searchList) {
      By selector = By.xpath(gridSelector + "//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search + "')]/..");

      // Wait for element visible
      waitUntil(and(visibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader"))));

      // Check text
      checkTextContains(selector, search);
    }
  }

  /**
   * Check cell contents
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param search Search value
   */
  protected void checkCellContents(String gridId, String rowId, String columnId, String search) {
    By selector = By.xpath(getParentSelectorXpath(gridId, rowId, columnId) + "//text()[contains(.,'" + search + "')]/..");

    // Wait for element visible
    waitUntil(and(visibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader"))));

    // Check text
    checkTextContains(selector, search);
  }

  /**
   * Check if grid doesn't contain some texts
   * @param search Texts to search for in the grid
   */
  protected void checkRowNotContains(String search) {
    By selector = By.xpath("//*[contains(@class,'ui-grid-row')]//*[contains(@class,'ui-grid-cell-contents')]//text()[contains(.,'" + search +"')]/..");

    ExpectedCondition<Boolean> condition = and(invisibilityOfElementLocated(selector), invisibilityOfElementLocated(By.cssSelector(".grid-loader")));

    // Assert element is not located
    assertWithScreenshot(condition.toString(), condition.apply(driver).booleanValue());
  }

  /**
   * Assert if a criterion contains a text
   * @param criterionName Criterion name
   * @param search Text to check
   */
  protected void checkCriterionContents(String criterionName, String search) {
    By selector = By.cssSelector(
      getCriterionSelectorCss(criterionName) + " input," +
      getCriterionSelectorCss(criterionName) + " textarea");

    // Wait for element visible
    waitUntil(presenceOfElementLocated(selector));

    // Check text
    checkCriterionContains(selector, search);
  }

  /**
   * Assert if some criteria are checked or not
   * @param isChecked Flag to check
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
   * @param criterionName Selector name
   * @param search Text to check
   */
  protected void checkSelectorContents(String criterionName, String search) {
    By selector = By.cssSelector(getCriterionSelectorCss(criterionName) + " .select2-chosen");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkTextContains(selector, search);
  }

  /**
   * Assert if a selector contains a text
   * @param criterionName Selector name
   * @param search Text to check
   */
  protected void checkMultipleSelectorContents(String criterionName, String search) {
    By selector = By.cssSelector(getCriterionSelectorCss(criterionName) + " .select2-search-choice div");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));

    // Check text
    checkTextContains(selector, search);
  }

  /**
   * Wait for button to be clickable
   * @param buttonName Button name
   */
  protected void waitForButton(String buttonName) {
    waitForSelector(By.cssSelector("#" + buttonName + ":not([disabled])"));
  }

  /**
   * Wait for context button to be clickable
   * @param buttonName Button name
   */
  protected void waitForContextButton(String buttonName) {
    waitForSelector(By.cssSelector(".context-menu [option-id='" + buttonName + "'] a:not([disabled])"));
  }

  /**
   * Wait for selector to be clickable
   * @param selector Selector to wait for
   */
  protected void waitForSelector(By selector) {
    // Safecheck
    Integer safecheck = 0;

    // Wait for element visible
    waitUntil(elementToBeClickable(selector));

    // Move mouse while help is being displayed
    List<WebElement> popovers = driver.findElements(By.cssSelector(".popover:not(.ng-hide)"));
    while (popovers.size() > 0 && safecheck < RETRY_COUNT) {
      WebElement element = popovers.get(0);
      new Actions(driver)
        .pause(100)
        .moveByOffset(30, 30)
        .build()
        .perform();

      popovers = driver.findElements(By.cssSelector(".popover:not(.ng-hide)"));
      safecheck++;
    }
  }

  /**
   * Wait for text inside a tag with a CSS class
   * @param clazz CSS class
   * @param contains Text to check
   */
  protected void waitForText(String clazz, String contains) {
    By selector = By.xpath("//*[contains(@class,'" + clazz + "')]//text()[contains(.,'" + contains +"')]/..");

    // Wait for element visible
    waitUntil(visibilityOfElementLocated(selector));
  }

  /**
   * Pause
   * @param time Milliseconds
   */
  protected void pause(Integer time) {
    new Actions(driver)
      .pause(time)
      .build()
      .perform();
  }

  /**
   * Write text on selector
   * @param selector Selector
   * @param text Text
   */
  protected void writeText(By selector, CharSequence text) {
    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Write text
    sendKeys(selector, text);
  }

  /**
   * Write text on criterion
   * @param criterionName Criterion name
   * @param text Text
   */
  protected void writeText(String criterionName, CharSequence text) {
    writeText(criterionName, text, true);
  }

  /**
   * Write text check clear text
   * @param criterionName Criterion name
   * @param text Text
   * @param clearText Clear text
   */
  protected void writeText(String criterionName, CharSequence text, boolean clearText) {
    writeTextFromSelector(getCriterionSelectorCss(criterionName), text, clearText);
  }

  /**
   * Write text check clear text
   * @param gridId Grid id
   * @param columnId Column id
   * @param text Text to write
   */
  protected void writeText(String gridId, String columnId, CharSequence text) {
    // Write text on grid
    writeTextFromSelector(getParentSelectorCss(gridId, null, columnId), text, true);
  }


  /**
   * Write text check clear text
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param text Text to write
   */
  protected void writeText(String gridId, String rowId, String columnId, CharSequence text) {
    // Write text on grid
    writeTextFromSelector(getParentSelectorCss(gridId, rowId, columnId), text, true);
  }

  /**
   * Write text check clear text
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param text Text to write
   * @param clearText Clear previous text
   */
  protected void writeText(String gridId, String rowId, String columnId, CharSequence text, boolean clearText) {
    // Write text on grid
    writeTextFromSelector(getParentSelectorCss(gridId, rowId, columnId), text, clearText);
  }

  /**
   * Write text check clear text
   * @param parentSelector Parent selector
   * @param text Text
   * @param clearText Clear text
   */
  private void writeTextFromSelector(String parentSelector, CharSequence text, boolean clearText) {
    By selector = By.cssSelector(parentSelector + " input," +
      parentSelector + " textarea");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Clear previous text
    if (clearText) {
      clearText(selector);
    }

    // Write text
    sendKeys(selector, text);

    // Write text
    sendKeys(selector, Keys.TAB);

    // Pause 100 ms
    pause(100);
  }

  /**
   * Get criterion text
   * @param criterionName Criterion name
   */
  protected String getText(String criterionName) {
    return getTextFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Get criterion text
   * @param gridId Grid id
   * @param columnId Column id
   * @return Criterion text
   */
  protected String getText(String gridId, String columnId) {
    return getTextFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Get criterion text
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @return Criterion text
   */
  protected String getText(String gridId, String rowId, String columnId) {
    return getTextFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Get criterion text
   * @param parentSelector Parent selector
   */
  protected String getTextFromSelector(String parentSelector) {
    By selector = By.cssSelector(
      parentSelector + " input," +
      parentSelector + " textarea");

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Get selector text
    return driver.findElement(selector).getAttribute("value");
  }

  /**
   * Click on a checkbox or a radio button
   * @param criterionName Criterion name
   */
  protected void clickCheckbox(String criterionName) {
    clickCheckboxFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Click on a checkbox or a radio button
   * @param gridId Grid id
   * @param columnId Column id
   */
  protected void clickCheckbox(String gridId, String columnId) {
    clickCheckboxFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Click on a checkbox or a radio button
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   */
  protected void clickCheckbox(String gridId, String rowId, String columnId) {
    clickCheckboxFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Click on a checkbox or a radio button
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
   * @param criterionName Criterion name
   */
  protected void selectFirst(String criterionName) {
    selectFirstFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Select first value of the select
   * @param gridId Grid id
   * @param columnId Column id
   */
  protected void selectFirst(String gridId, String columnId) {
    selectFirstFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Select first value of the select
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   */
  protected void selectFirst(String gridId, String rowId, String columnId) {
    selectFirstFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Select first value of the select
   * @param parentSelector Parent selector
   */
  private void selectFirstFromSelector(String parentSelector) {
    // Click on selector
    selectClick(parentSelector);

    // Click option
    click(By.cssSelector("#select2-drop li:first-of-type"));
  }

  /**
   * Select first value of the select
   * @param criterionName Criterion name
   */
  protected void selectLast(String criterionName) {
    selectLastFromSelector(getCriterionSelectorCss(criterionName));
  }

  /**
   * Select first value of the select
   * @param gridId Grid id
   * @param columnId Column id
   */
  protected void selectLast(String gridId, String columnId) {
    selectLastFromSelector(getParentSelectorCss(gridId, null, columnId));
  }

  /**
   * Select first value of the select
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   */
  protected void selectLast(String gridId, String rowId, String columnId) {
    selectLastFromSelector(getParentSelectorCss(gridId, rowId, columnId));
  }

  /**
   * Select last element
   * @param parentSelector Parent selector
   */
  private void selectLastFromSelector(String parentSelector) {
    // Click on selector
    selectClick(parentSelector);

    // Click option
    click(By.cssSelector("#select2-drop li:last-of-type"));
  }

  /**
   * Select value on the selector
   * @param criterionName Criterion name
   * @param label Label to search
   */
  protected void selectContain(String criterionName, String label) {
    selectContainFromSelector(getCriterionSelectorCss(criterionName), label);
  }

  /**
   * Select value on the selector
   * @param gridId Grid id
   * @param columnId Column id
   * @param label Label to search
   */
  protected void selectContain(String gridId, String columnId, String label) {
    selectContainFromSelector(getParentSelectorCss(gridId, null, columnId), label);
  }

  /**
   * Select value on the selector
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param label Label to search
   */
  protected void selectContain(String gridId, String rowId, String columnId, String label) {
    selectContainFromSelector(getParentSelectorCss(gridId, rowId, columnId), label);
  }

  /**
   * Select an element which contains a label
   * @param parentSelector Parent selector
   * @param label Label to search
   */
  private void selectContainFromSelector(String parentSelector, String label) {
    By selector = By.xpath("//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'" + label +"')]/..");

    // Click on selector
    selectClick(parentSelector);

    // Wait for element present
    waitUntil(presenceOfElementLocated(selector));

    // Click option
    click(selector);
  }

  /**
   * Suggest element which contains label
   * @param criterionName Criterion name
   * @param search Search string
   * @param label Label to search
   */
  protected void suggest(String criterionName, String search, String label) {
    suggestFromSelector(getCriterionSelectorCss(criterionName), search, label);
  }

  /**
   * Suggest element which contains label
   * @param gridId Grid id
   * @param columnId Column id
   * @param search Search string
   * @param label Label to search
   */
  protected void suggest(String gridId, String columnId, String search, String label) {
    suggestFromSelector(getParentSelectorCss(gridId, null, columnId), search, label);
  }

  /**
   * Suggest element which contains label
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param search Search string
   * @param label Label to search
   */
  protected void suggest(String gridId, String rowId, String columnId, String search, String label) {
    suggestFromSelector(getParentSelectorCss(gridId, rowId, columnId), search, label);
  }

  /**
   * Suggest element which contains label
   * @param parentSelector Parent selector
   * @param search Search string
   * @param label Label to search
   */
  private void suggestFromSelector(String parentSelector, String search, String label) {
    By selector = By.xpath("//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'" + label +"')]/..");

    // Wait for element present
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".loader")));

    // Click on selector
    selectClick(parentSelector);

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

  /**
   * Suggest element which contains label
   * @param criterionName Criterion name
   * @param search Search string
   */
  protected void suggestLast(String criterionName, String search) {
    suggestLastFromSelector(getCriterionSelectorCss(criterionName), search);
  }

  /**
   * Suggest element which contains label
   * @param gridId Grid id
   * @param columnId Column id
   * @param search Search string
   */
  protected void suggestLast(String gridId, String columnId, String search) {
    suggestLastFromSelector(getParentSelectorCss(gridId, null, columnId), search);
  }

  /**
   * Suggest element which contains label
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param search Search string
   */
  protected void suggestLast(String gridId, String rowId, String columnId, String search) {
    suggestLastFromSelector(getParentSelectorCss(gridId, rowId, columnId), search);
  }

  /**
   * Suggest last element which contains label
   * @param parentSelector Criterion name
   * @param search Search string
   */
  private void suggestLastFromSelector(String parentSelector, String search) {
    By selector = By.cssSelector("#select2-drop li:last-of-type .select2-result-label");

    // Wait for element present
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".loader")));

    // Click on selector
    selectClick(parentSelector);

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

  /**
   * Suggest or select multiple element which contains label
   * @param criterionName Criterion name
   * @param search Search string
   * @param label Text to find in label
   */
  protected void suggestMultiple(String criterionName, String search, String label) {
    suggestMultipleFromSelector(getCriterionSelectorCss(criterionName), search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   * @param gridId Grid id
   * @param columnId Column id
   * @param search Search string
   * @param label Text to find in label
   */
  protected void suggestMultiple(String gridId, String columnId, String search, String label) {
    suggestMultipleFromSelector(getParentSelectorCss(gridId, null, columnId), search, label);
  }

  /**
   * Suggest or select multiple element which contains label
   * @param gridId Grid id
   * @param rowId Row id
   * @param columnId Column id
   * @param search Search string
   * @param label Text to find in label
   */
  protected void suggestMultiple(String gridId, String rowId, String columnId, String search, String label) {
    suggestMultipleFromSelector(getParentSelectorCss(gridId, rowId, columnId), search, label);
  }

  /**
   * Suggest or select multiple
   * @param parentSelector Parent selector
   * @param search Text to search
   * @param label Text to find in label
   */
  private void suggestMultipleFromSelector(String parentSelector, String search, String label) {
    // Safecheck
    Integer safecheck = 0;

    By selector = By.xpath("//*[@id='select2-drop']//*[contains(@class,'select2-result-label')]//text()[contains(.,'" + label +"')]/..");
    By searchBox = By.cssSelector(parentSelector + " input.select2-input");

    // Wait for element present
    waitUntil(invisibilityOfElementLocated(By.cssSelector(".loader")));

    // Wait for element present
    waitUntil(presenceOfElementLocated(searchBox));

    // Clear selector
    By clearSelector = By.cssSelector(parentSelector + " .select2-search-choice-close");
    while (driver.findElements(clearSelector).size() > 0 && safecheck < RETRY_COUNT) {
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

  /**
   * Click on save row and wait
   */
  protected void saveRow() {
    saveRowFromSelector(".grid-row-save:not([disabled])");
  }

  /**
   * Click on save row and wait
    * @param gridId Grid with the save button
   */
  protected void saveRow(String gridId) {
    saveRowFromSelector(".grid [id='scope-" + gridId + "'] .grid-row-save:not([disabled])");
  }

  /**
   * Click on save row and wait
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
   * Accept confirm window and wait for it to disappear
   */
  protected void acceptConfirm() {
    // Pause 250 ms
    pause(250);

    // Click on button
    clickButton("confirm-accept");

    // Wait for element not present
    waitUntil(invisibilityOfElementLocated(By.id("confirm-accept")));
  }

  /**
   * Accept a message box
   * @param messageType Message type (success (default), info, warning, danger)
   */
  protected void verifyAndAcceptMessage(String messageType) {
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
   */
  protected void clickButtonAndConfirm(String button) {
    clickButtonAndConfirm(button, "success");
  }

  /**
   * Click on confirm button, accept confirmation and accept message
   */
  protected void clickButtonAndConfirm(String button, String messageType) {
    // Click on button
    clickButton(button);

    // Accept confirm
    acceptConfirm();

    // Accept message
    verifyAndAcceptMessage(messageType);
  }

  /**
   * Check if message is missing
   * @param messageType Message type
   */
  protected void checkMessageMissing(String messageType) {
    By messageSelector = By.cssSelector(".alert-zone .alert-" + messageType + " button.close");

    // Wait 1 second
    pause(1000);
    List<WebElement> messages = driver.findElements(messageSelector);

    // Check there are no messages of messageType
    assertEquals(0, messages.size());
  }

  /**
   * Check element is present
   * @param cssSelector CSS selector
   */
  protected void checkPresence(String cssSelector) {
    By selector = By.cssSelector(cssSelector);

    // Wait until visible
    waitUntil(ExpectedConditions.presenceOfElementLocated(selector));
  }

  /**
   * Check element is visible
   * @param cssSelector CSS selector
   */
  protected void checkVisible(String cssSelector) {
    By selector = By.cssSelector(cssSelector);

    // Wait until visible
    waitUntil(ExpectedConditions.visibilityOfElementLocated(selector));
  }

  /**
   * Check element is visible
   * @param cssSelector CSS selector
   * @param search Search string
   */
  protected void checkVisibleAndContains(String cssSelector, String search) {
    // Check if it is visible
    checkVisible(cssSelector);

    // Check text contains
    checkTextContains(By.cssSelector(cssSelector), search);
  }

  /**
   * Check element is not visible
   * @param cssSelector CSS selector
   */
  protected void checkNotVisible(String cssSelector) {
    By selector = By.cssSelector(cssSelector);

    // Wait until visible
    waitUntil(ExpectedConditions.invisibilityOfElementLocated(selector));
  }

  /**
   * Select module
   * @param moduleName Module name
   * @throws Exception
   */
  protected void selectModule(String moduleName) throws Exception {
    // Click on info button
    clickInfoButton("ButSetTog");

    // Suggest
    suggest("module",  moduleName, moduleName);

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Log into the application
   * @throws Exception
   */
  protected void checkLogin() throws Exception {
    assertNotNull(driver);

    System.out.println("Launching tests with '" + browser + "' browser");

    // Set driver timeout
    driver.manage().timeouts().setScriptTimeout(timeout, SECONDS);

    // Open page in different browsers
    driver.get(startURL);

    // Wait for load
    waitForLoad();

    // Test title
    setTestTitle("Login test: Log into the application");

    // Wait for text in selector
    waitForText("slogan", "Almis Web Engine");

    // Wait for element present
    waitForButton("ButLogIn");

    // Write username
    writeText("cod_usr", "test");

    // Write password
    writeText("pwd_usr", "test");

    // Click button
    clickButton("ButLogIn");

    // Wait for element not visible
    waitForLoadingBar();

    // Wait for element present
    checkPresence("#ButUsrAct span.info-text");

    // Wait for element present
    waitForText("info-text", "Manager (test)");

    // Assertion
    checkText(By.cssSelector("#ButUsrAct span.info-text"), "Manager (test)");
  }

  /**
   * Log out the application
   * @throws Exception
   */
  protected void checkLogout() throws Exception {
    // Test title
    setTestTitle("Logout test: Log out the application");

    // Wait for element not visible
    waitForLoadingBar();

    // Wait for element present
    waitUntil(presenceOfElementLocated(By.cssSelector("#ButUsrAct span.info-text")));

    // Wait for element present
    waitForText("info-text", "Manager (test)");

    // Wait for element present
    clickButton("ButLogOut");

    // Wait for element not visible
    waitForLoadingBar();

    // Wait for text in selector
    waitForText("slogan", "Almis Web Engine");
  }

  /**
   * Broadcast a message to a user
   */
  protected void broadcastMessageToUser() {
    // Go to broadcast screen
    gotoScreen("tools", "broadcast-messages");

    // Suggest
    suggest("MsgTar", "test", "test");

    // Write on criterion
    writeText("MsgDes", "This is a broadcast message test");

    // Search and wait
    clickButton("ButSnd");

    // Accept message
    verifyAndAcceptMessage("success");

    // Accept message
    verifyAndAcceptMessage("info");
  }

  /**
   * Set test title
   * @param title
   */
  protected void setTestTitle(String title) {
    // Info
    logger.log(Level.INFO, "======================================================================================");
    logger.log(Level.INFO, "| " + title);
    logger.log(Level.INFO, "======================================================================================");
  }
}
