package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrintTestsIT extends SeleniumUtilities {

  /**
   * Log into the application
   * @throws Exception
   */
  @Test
  public void t000_loginTest() throws Exception {
    checkLogin("test", "test", "#ButUsrAct span.info-text", "Manager (test)");
  }

  /**
   * Log out from the application
   * @throws Exception
   */
  @Test
  public void t999_logoutTest() throws Exception {
    checkLogout(".slogan", "Almis Web Engine");
  }

  /**
   * Select test module on select criterion
   * @throws Exception Error on test
   */
  @Test
  public void t001_selectTestModule() throws Exception {
    // Title
    setTestTitle("Select test module: Test to select test module");

    // Select module
    selectModule("Test");

    // Wait for text
    waitForText("mm-text", "Tests");

    // Check text
    checkVisible("[translate-multiple='MENU_TEST'");
  }

  /**
   * Print user list
   * @throws Exception Error on test
   */
  @Test
  public void t010_printUserList() throws Exception {
    // Title
    setTestTitle("Print user list");

    // Print screen
    verifyPrintScreen(false,"tools", "users");
  }

  /**
   * Print profile list
   * @throws Exception Error on test
   */
  @Test
  public void t020_printProfilesList() throws Exception {
    // Title
    setTestTitle("Print profiles list");

    // Print screen
    verifyPrintScreen(false, "tools", "profiles");
  }

  /**
   * Print matrix selected tab
   * @throws Exception Error on test
   */
  @Test
  public void t030_printMatrixSelectedTab() throws Exception {
    // Title
    setTestTitle("Print matrix selected tab");

    // Print screen
    verifyPrintScreen(false, "test", "matrix", "matrix-test");
  }

  /**
   * Print matrix all tabs
   * @throws Exception Error on test
   */
  @Test
  public void t040_printMatrixAllTabs() throws Exception {
    // Title
    setTestTitle("Print all matrix tabs");

    // Print screen
    verifyPrintScreen(true,"test", "matrix", "matrix-test");
  }

  /**
   * Print chart screen
   * @throws Exception Error on test
   */
  @Test
  public void t050_printChartScreen() throws Exception {
    // Title
    setTestTitle("Print chart screen");

    // Print screen
    verifyPrintScreen(false,"test", "chart", "chart-test");
  }

  /**
   * Print chart and grid screen
   * @throws Exception Error on test
   */
  @Test
  public void t060_printChartAndGrid() throws Exception {
    // Title
    setTestTitle("Print chart and grid");

    // Print screen
    verifyPrintScreen(false,"test", "chart", "grid-and-chart");

    // Check for pager values selector
    Select select = new Select(getDriver().findElement(By.cssSelector(".grid-pager")));
    WebElement option = select.getFirstSelectedOption();
    assertEquals("25", option.getText());
  }

  /**
   * Go to a screen and print the options
   * @param allTabs print all tabs
   * @param menuOptions Menu options
   */
  private void verifyPrintScreen(boolean allTabs, String... menuOptions) {
    // Go to matrix test
    gotoScreen(menuOptions);

    // Click print button
    clickButton("ButPrn");

    // Wait 1 second
    pause(1000);

    // Wait for button
    waitForButton("ButDiaVal");

    // Select
    selectContain("ActPrn", "Generate");

    // Check all tabs if defined
    if (allTabs) {
      selectContain("TypPrn", "All tabs");
    } else {
      selectContain("TypPrn", "Selected tab");
    }

    // Click accept
    clickButton("ButDiaVal");

    // Accept message
    checkAndCloseMessage("success");

    // Wait modal backdrop to disappear
    checkNotVisible(".modal-backdrop");
  }
}
