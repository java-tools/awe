package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTestsIT extends SeleniumUtilities {

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
   * Load suggest on grid: Test to check suggest initial load on grid (#30648)
   * @throws Exception Error on test
   */
  @Test
  public void t002_loadSuggestOnGrid() throws Exception {
    // Title
    setTestTitle("Load suggest on grid: Test to check suggest initial load on grid (#30648)");

    // Go to matrix test
    gotoScreen("test", "matrix", "matrix-test");

    // Click on tab
    clickTab("TabSelMat", "EDITABLE");

    // Check row contents
    checkRowContents("Prueba - adminflare");
  }

  /**
   * Test to check suggest criteria with 'strict' attribute set to false
   * @throws Exception Error on test
   */
  @Test
  public void t003_suggestStrict() throws Exception {
    // Title
    setTestTitle("Suggest Strict: Test to check suggest criteria with 'strict' attribute set to false");

    // Go to screen
    gotoScreen("tools", "users");

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggestLast("CrtUsr", "te");

    // Search and wait
    searchAndWait();

    // Check grid values
    checkRowContents("test");

    // Check criterion value
    checkSelectContents("CrtUsr", "te");
  }

  /**
   * Test for read dependency
   * @throws Exception Error on test
   */
  @Test
  public void t004_readDependency() throws Exception {
    // Title
    setTestTitle("Test for read dependency");

    // Go to screen
    gotoScreen("test","criteria", "criteria-test");

    // Wait for button
    waitForButton("ButPrn");

    // Write on criterion
    writeText("TxtReq", "aaa");

    // Write on criterion
    writeText("Unt", "325.274,50");

    // Assert text
    checkCriterionContents("Unt", "325.274,50");
  }

  /**
   * Quote check on unit label
   * @throws Exception Error on test
   */
  @Test
  public void t005_quoteCheckUnitLabel() throws Exception {
    // Title
    setTestTitle("Quote check on unit label");

    // Write on criterion
    writeText("Tar", "\"");

    // Click on checkbox
    clickCheckbox("RadBox3");

    // Wait for text
    checkText("[criterion-id='Unt'] .unit", "USD");
  }

  /**
   * Check filtered date dependency (#31141)
   * @throws Exception Error on test
   */
  @Test
  public void t006_checkFilteredDateDependency() throws Exception {
    // Title
    setTestTitle("Check filtered date dependency (#31141)");

    // Write on criterion
    writeText("Txt", "edita");

    // Check text
    checkCriterionContents("Txt", "edita");

    // Click on date
    clickDate("FilCalRea");

    // Click on selector
    click(".datepicker td.day:not(.disabled)");
  }

  /**
   * Keep criteria test
   * @throws Exception Error on test
   */
  @Test
  public void t007_keepCriteria() throws Exception {
    // Title
    setTestTitle("Keep criteria test");

    // Go to screen
    gotoScreen("tools","users");

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtUsr", "test", "test");

    // Search and wait
    searchAndWait();

    // Check grid values
    clickRowContents("test");

    // Click button
    clickButton("ButViw", true);

    // Click button
    clickButton("ButBck", true);

    // Wait for button
    waitForButton("ButRst");

    // Wait for button
    checkSelectContents("CrtUsr", "test");
  }

  /**
   * Delayed suggest
   * @throws Exception Error on test
   */
  @Test
  public void t008_delayedSuggest() throws Exception {
    // Title
    setTestTitle("Delayed suggest");

    // Go to screen
    gotoScreen("test","criteria", "criteria-test");

    // Click button
    clickButton("ButRst");

    // Suggest delayed
    click("[criterion-id='Sug'] .select2-choice");
    suggestDelayed("#select2-drop", "tee", "test", "test", 800);

    // Suggest delayed
    suggestDelayed("[criterion-id='SugMulReq']", "tee", "test", "test", 800);

    // Check selector
    checkMultipleSelectorContents("SugMulReq", "test (test@test.com)");
  }

  /**
   * Wrong login
   * @throws Exception Error on test
   */
  @Test
  public void t009_wrongLogin() throws Exception {
    // Title
    setTestTitle("Wrong login");

    // Do logout
    checkLogout(".slogan", "Almis Web Engine");

    // Check wrong login
    checkLogin("test", "lala", ".alert.alert-warning div", "Invalid credentials  The credentials entered for the user -test- are not valid");

    // Check wrong login
    checkLogin("tutu", "lala", ".alert.alert-warning div", "Wrong username  Username -tutu- is wrong or inactive");

    // Do right login
    checkLogin("test", "test", "span.info-text", "Manager (test)");
  }

  /**
   * Sort a grid using a component column
   * @throws Exception Error on test
   */
  @Test
  public void t010_sortComponentColumn() throws Exception {
    // Title
    setTestTitle("Sort a grid using a component column");

    // Go to screen
    gotoScreen("tools","users");

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtUsr", "test", "test");

    // Search and wait
    searchAndWait();

    // Scroll grid to the right
    scrollGrid("GrdUsrLst", 10000, 0);

    // Sort by a component field
    sortGrid("GrdUsrLst", "StaIco");

    // Expect not to have an error message
    checkMessageMissing("danger");

    // Sort by a component field
    sortGrid("GrdUsrLst", "BlkIco");

    // Expect not to have an error message
    checkMessageMissing("danger");

    // Sort by a component field
    sortGrid("GrdUsrLst", "LanTxt");

    // Expect not to have an error message
    checkMessageMissing("danger");

    // Sort by a component field
    sortGrid("GrdUsrLst", "LanImg");

    // Expect not to have an error message
    checkMessageMissing("danger");
  }

  /**
   * Suggest delayed
   * @param selector Selector
   * @param search1 Search on first case
   * @param search2 Search on second case
   * @param match Match result
   * @param pause Pause
   */
  private void suggestDelayed(String selector, String search1, String search2, String match, Integer pause) {
    // Write text
    writeText(By.cssSelector(selector + " input.select2-input"), search1);

    // Pause
    pause(pause);

    // Clear text
    clearText(selector + " input.select2-input");

    // Write select
    writeText(By.cssSelector(selector + " input.select2-input"), search2);

    // Click selector
    selectResult(match);
  }
}
