package com.almis.awe.test.integration;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaticTestsIT extends SeleniumTestsUtil {

  @Test
  public void t000_loginTest() throws Exception {
    doLogin();
  }

  @Test
  public void t999_logoutTest() throws Exception {
    doLogout();
  }

  @Test
  public void t001_newSite() throws Exception {
    // Go to screen
    gotoScreen("tools", "sites");

    // Wait for loading bar
    waitForLoadingBar();

    // Click on new button
    clickButton("ButNew");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButCnf");

    // Write on criterion
    writeText("Nam", "Site test", false);

    // Select on selector
    selectLast("Act", false);

    // Write on criterion
    writeText("Ord", "3", false);

    // Click on button
    clickButton("ButGrdAdd");

    // Suggest on column selector
    suggest("IdeMod", "Base", "Base", true);

    // Suggest on column selector
    suggest("IdeDbs", "awedb", "awedb", true);

    // Write on criterion
    writeText("Order", "3", true);

    // Save line
    saveLine();

    // Click on button
    clickButton("ButCnf");

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage("success");
  }

  @Test
  public void t002_verifyNewSite() throws Exception {
    // Go to screen
    gotoScreen("tools", "sites");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtSit", "Site test", "Site test", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Site test");

    // Click on button
    clickButton("ButViw");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButBck");

    // Check row contents
    checkRowContents("Base");

    // Check row contents
    checkRowContents("awedb");
  }

  @Test
  public void t003_updateSite() throws Exception {
    // Go to screen
    gotoScreen("tools", "sites");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtSit", "Site", "Site", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Site");

    // Click on button
    clickButton("ButUpd");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButCnf");

    // Write on criterion
    writeText("Nam", "Site changed", false);

    // Click row
    clickRowContents("Base");

    // Suggest on column selector
    suggest("IdeDbs", "awedb2", "awedb2", true);

    // Save line
    saveLine();

    // Click on button
    clickButton("ButCnf");

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage("success");
  }

  @Test
  public void t004_verifyUpdatedSite() throws Exception {
    // Go to screen
    gotoScreen("tools", "sites");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtSit", "Site changed", "Site changed", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Site changed");

    // Click on button
    clickButton("ButViw");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButBck");

    // Check row contents
    checkRowContents("Base");

    // Check row contents
    checkRowContents("awedb2");
  }

  @Test
  public void t011_newModule() throws Exception {
    // Go to screen
    gotoScreen("tools", "modules");

    // Wait for loading bar
    waitForLoadingBar();

    // Click on new button
    clickButton("ButNew");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButCnf");

    // Select option
    suggest("Nam", "Inf", "Inf", false);

    // Suggest on selector
    suggest("Scr", "Sites", "Sites", false);

    // Suggest on  selector
    suggest("Thm", "gra", "gra", false);

    // Select on selector
    suggest("Act",  "Yes", "Yes", false);

    // Click on button
    clickButton("ButMdlUsrLstAdd");

    // Suggest on column selector
    suggest("IdeOpe", "test", "test", true);

    // Suggest on column selector
    suggest("IdeThm", "sky", "sky", true);

    // Save line
    saveLine("MdlUsrLst");

    // Click on button
    clickButton("ButMdlPrfLstAdd");

    // Suggest on column selector
    suggest("IdePro", "TS", "TS", true);

    // Save line
    saveLine("MdlPrfLst");

    // Click on button
    clickButton("ButMdlSitDbsLstAdd");

    // Suggest on column selector
    suggest("IdeSit", "Site", "Site", true);

    // Suggest on column selector
    suggest("IdeDbs", "awedb", "awedb", true);

    // Save line
    saveLine("MdlSitDbsLst");

    // Click on button
    clickButton("ButCnf");

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage("success");
  }

  @Test
  public void t012_verifyNewModule() throws Exception {
    // Go to screen
    gotoScreen("tools", "modules");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtMod", "Inf", "Inf", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Inf");

    // Click on button
    clickButton("ButViw");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButBck");

    // Check contents
    checkCriterionContents("Nam", "Inf");

    // Check contents
    checkCriterionContents("Thm", "grass");

    // Check contents
    checkSelectorContents("Scr", "Sites (Sit)");

    // Check row contents
    checkRowContents("test");

    // Check row contents
    checkRowContents("TST");

    // Check row contents
    checkRowContents("Site changed");
  }

  @Test
  public void t013_updateModule() throws Exception {
    // Go to screen
    gotoScreen("tools", "modules");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtMod", "Inf", "Inf", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Inf");

    // Click on button
    clickButton("ButUpd");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButCnf");

    // Write on criterion
    suggest("Nam", "Inf changed","Inf changed", false);

    // Write on criterion
    suggest("Scr", "Usr", "Usr", false);

    // Click row
    clickRowContents("test");

    // Suggest on column selector
    selectContain("IdeThm", "grass", true);

    // Save line
    saveLine("MdlUsrLst");

    // Click row
    clickRowContents("TST");

    // Suggest on column selector
    selectContain("IdePro", "ADM - administrator", true);

    // Save line
    saveLine("MdlPrfLst");

    // Click row
    clickRowContents("Site changed");

    // Suggest on column selector
    selectContain("IdeDbs", "awedb", true);

    // Save line
    saveLine("MdlSitDbsLst");

    // Click on button
    clickButton("ButCnf");

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage("success");
  }

  /*@Test
  public void t014_verifyUpdatedModule() throws Exception {
    // Go to screen
    gotoScreen("tools", "modules");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtSit", "Site changed", "Site changed", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Site changed");

    // Click on button
    clickButton("ButViw");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    waitForButton("ButBck");

    // Check row contents
    checkRowContents("Base");

    // Check row contents
    checkRowContents("awedb2");
  }*/

  @Test
  public void t055_deleteModule() throws Exception {
    // Go to screen
    gotoScreen("tools", "modules");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtMod", "Inf", "Inf", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Inf");

    // Click on button
    clickButton("ButDel");

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage("success");
  }

  @Test
  public void t056_deleteSite() throws Exception {
    // Go to screen
    gotoScreen("tools", "sites");

    // Wait for loading bar
    waitForLoadingBar();

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtSit", "Site", "Site", false);

    // Click on button
    clickButton("ButSch");

    // Wait for loading bar
    waitForLoadingGrid();

    // Click row
    clickRowContents("Site");

    // Click on button
    clickButton("ButDel");

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage("success");
  }
}
