package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTestsIT extends SeleniumUtilities {

  /**
   * Log into the application
   */
  @Test
  public void t000_loginTest() {
    checkLogin("test", "test", "#ButUsrAct span.info-text", "Manager (test)");
  }

  /**
   * Log out from the application
   */
  @Test
  public void t999_logoutTest() {
    checkLogout(".slogan", "Almis Web Engine");
  }

  /**
   * Test screen configuration usage (hide ButPrn button on databases screen)
   */
  @Test
  public void t010_screenConfigurationUsage() {
    // Title
    setTestTitle("Test screen configuration usage");

    // Go to screen
    gotoScreen("settings", "screen-configuration");

    // Click button
    clickButton("ButRst");

    // Click button
    clickButton("ButGrdAdd");

    // Select on selector
    suggest("GrdScrCnf", "Scr",  "Dbs", "Dbs");

    // Select on selector
    suggest("GrdScrCnf", "IdeOpe",  "test", "test");

    // Select on selector
    suggest("GrdScrCnf", "Nam",  "ButPrn", "ButPrn");

    // Select text
    suggest("GrdScrCnf", "Atr", "visible", "Visible");

    // Select text
    writeText("GrdScrCnf", "Val", "false");

    // Scroll grid to the right
    scrollGrid("GrdScrCnf", 10000, 0);

    // Select text
    selectContain("GrdScrCnf", "Act", "Yes");

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Go to databases screen
    gotoScreen("tools", "databases");

    // Wait for button
    waitForButton("ButRst");

    // Verify that ButPrn button is not visible
    checkNotVisible("#ButPrn");

    // Go to screen
    gotoScreen("settings", "screen-configuration");

    // Click button
    clickButton("ButRst");

    // Select on selector
    suggest("CrtScr",  "Dbs", "Dbs");

    // Select on selector
    suggest("CrtUsr",  "test", "test");

    // Select text
    selectContain("CrtAct", "Yes");

    // Search and wait
    searchAndWait();

    // Click on row
    clickRowContents("Dbs");

    // Click on delete button
    clickButton("ButGrdDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Go to databases screen
    gotoScreen("tools", "databases");

    // Click button
    waitForButton("ButPrn");
  }

  /**
   * Select test module on select criterion
   */
  @Test
  public void t020_selectTestModule() {
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
   * Test screen modules usage (edit module to change order and check modules selector gets the proper order)
   */
  @Test
  public void t030_screenModulesUsage() {
    // Title
    setTestTitle("Test screen modules usage");

    // Go to screen
    gotoScreen("tools", "modules");

    // Click on row
    clickRowContents("Test");

    // Click button
    clickButton("ButUpd");

    // Wait for button
    waitForButton("ButCnf");

    // Select text
    writeText("Ord", "0");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    waitForButton("ButRst");

    checkLogout(".slogan", "Almis Web Engine");

    checkLogin("test", "test", "#ButUsrAct span.info-text", "Manager (test)");

    // Verify that ButPrn button is not visible
    checkVisibleAndContains("li[option-name='test']", "Tests");

  }

  /**
   * Grid test: Having query
   */
  @Test
  public void t040_gridTestHaving() {
    // Title
    setTestTitle("Grid test: Having query");

    // Go to screen
    gotoScreen("test", "matrix", "matrix-test-having");

    // Search and wait
    searchAndWait("ButGrdEdiDel");

    // Check row contents
    checkRowContents("I");
  }

  /**
   * Chart test
   */
  @Test
  public void t050_chartTest() {
    // Title
    setTestTitle("Chart test");

    // Go to screen
    gotoScreen("test", "chart", "chart-test");

    // Check visible
    checkVisible("[chart-id='ChrLinTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrBarTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrAreTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrPieTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrDonutTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrStockTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrBarHorTst'] svg");

    // Check visible
    checkVisible("[chart-id='ChrSemiCircleTst'] svg");
  }

  /**
   * Wizard test
   */
  @Test
  public void t060_wizardTest() {
    // Title
    setTestTitle("Wizard test");

    // Go to screen
    gotoScreen("test", "wizard-test");

    // Check visibility
    checkVisibleAndContains("li.active > span.wizard-step-number", "1");

    // Check visibility
    checkVisibleAndContains("[awe-tag-list='wizard-tag-list-1'] > span", "Manager (test)");

    // Check visibility
    checkVisibleAndContains("[awe-tag-list='wizard-tag-list-2']   span", "MANAGER (TEST)");

    // Write text
    writeText("epa", "aaa");

    // Write text
    writeText("tutu", "bbb");

    // Click button
    clickButton("FwStep2");

    // Check visibility and content
    checkVisibleAndContains("li.active > span.wizard-step-number", "2");

    // Write text
    writeText("lala", "aaa");

    // Write text
    writeText("prueba", "bbb");

    // Write text
    writeText("pwd_usr", "ccc");

    // Click button
    clickButton("FwStep3");

    // Check visibility and content
    checkVisibleAndContains("li.active > span.wizard-step-number", "3");

    // Write text
    writeText("epa12", "aaa");

    // Write text
    writeText("tutu12", "bbb");

    // Click button
    clickButton("FwStep4");

    // Check visibility and content
    checkVisibleAndContains("li.active > span.wizard-step-number", "4");

    // Write text
    writeText("epa121", "aaa");

    // Write text
    writeText("tutu121", "bbb");

    // Click button
    clickButton("Finish", true);
  }

  /**
   * SQL extractor engine test
   */
  @Test
  public void t070_sqlExtractorEngine() {
    // Title
    setTestTitle("SQL extractor engine test");

    // Go to screen
    gotoScreen("tools", "sqlExtractor");

    // Write text on criteria
    writeText("selectCriteria", "select * from awekey");

    // Search and wait
    searchAndWait();

    // Check visible
    checkRowContents("OpeKey");
  }

  /**
   * File Manager test
   */
  @Test
  public void t080_fileManager() {
    // Title
    setTestTitle("File Manager test");

    // Go to screen
    gotoScreen("test", "filemanager-test");

    // Wait for iframe
    waitForCssSelector("iframe");

    // Switch driver
    WebElement iframe = getDriver().findElement(By.cssSelector("iframe"));
    getDriver().switchTo().frame(iframe);

    // Check visible
    checkText("ol.breadcrumb a", "angular-filemanager");

    // Return driver
    getDriver().switchTo().defaultContent();
  }
}
