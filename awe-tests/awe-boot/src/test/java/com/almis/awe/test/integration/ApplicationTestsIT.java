package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTestsIT extends SeleniumUtilities {

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
   * Go to a screen to add a new option
   * @param options
   */
  private void addNew(String... options) {
    // Go to screen
    gotoScreen(options);

    // Click on new button
    clickButton("ButNew", true);

    // Wait for button
    waitForButton("ButCnf");
  }

  /**
   * Go to a screen to add a new option
   * @param options
   */
  private void update(String suggest, String search, String... options) {
    // Go to screen
    gotoScreen(options);

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(suggest, search, search);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents(search);

    // Click on button
    clickButton("ButUpd", true);

    // Wait for button
    waitForButton("ButCnf");

    // Wait for loading bar
    waitForLoadingBar();
  }

  /**
   * Click on confirm button, accept confirmation and accept message
   */
  private void verifyView(String suggest, String search) {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(suggest, search, search);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents(search);

    // Click on button
    clickButton("ButViw", true);

    // Wait for button
    waitForButton("ButBck");
  }

  /**
   * Delete from a screen
   * @param criterion Criterion to search
   * @param search Search text
   * @param options Screen options
   */
  private void delete(String criterion, String search, String... options) {
    // Go to screen
    gotoScreen(options);

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(criterion, search, search);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents(search);

    // Store and confirm
    clickButtonAndConfirm("ButDel");
  }

  /**
   * Verify deleted
   * @param search
   */
  private void verifyDeleted(String search) {
    // Wait for button
    clickButton("ButRst");

    // Search on grid
    searchAndWait();

    // Click row
    checkRowNotContains(search);
  }

  /**
   * Add a new theme
   * @throws Exception
   */
  @Test
  public void t001_newTheme() throws Exception {
    // Title
    setTestTitle("Add a new theme");

    addNew("tools", "themes");

    // Insert text
    writeText("Nam", "Theme test");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtNam", "test");

    // Check contents
    checkCriterionContents("Nam", "Theme test");
  }

  /**
   * Update a theme
   * @throws Exception
   */
  @Test
  public void t003_updateTheme() throws Exception {
    // Title
    setTestTitle("Update a theme");

    // Go to update
    update("CrtNam", "test", "tools", "themes");

    // Insert text
    writeText("Nam", "Theme changed");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtNam", "Theme changed");

    // Check contents
    checkCriterionContents("Nam", "Theme changed");
  }

  /**
   * Delete a theme
   * @throws Exception
   */
  @Test
  public void t005_deleteTheme() throws Exception {
    // Title
    setTestTitle("Delete a theme");

    // Delete a theme
    delete("CrtNam", "Theme changed", "tools", "themes");

    // Verify
    verifyDeleted("Theme changed");
  }

  /**
   * Add a new sequence
   * @throws Exception
   */
  @Test
  public void t011_newSequence() throws Exception {
    // Title
    setTestTitle("Add a new sequence");

    // Go to screen
    gotoScreen("tools", "sequences");

    // Wait for button
    clickButton("ButRst");

    // Wait for button
    clickButton("ButGrdKeyLstAdd");

    // Insert text
    writeText("GrdKeyLst", "KeyNam", "testKey");

    // Insert text
    writeText("GrdKeyLst", "KeyVal", "0");

    // Select on selector
    selectContain("GrdKeyLst", "Act",  "Yes");

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test");

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("testKey");
  }

  /**
   * Update a sequence
   * @throws Exception
   */
  @Test
  public void t013_updateSequence() throws Exception {
    // Title
    setTestTitle("Update a sequence");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test");

    // Wait for button
    searchAndWait();

    // Click on row
    clickRowContents("testKey");

    // Insert text
    writeText("GrdKeyLst", "KeyVal", "1");

    // Select on selector
    selectContain("GrdKeyLst", "Act",  "No");

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test");

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("testKey", "1", "No");
  }

  /**
   * Delete a sequence
   * @throws Exception
   */
  @Test
  public void t015_deleteSequence() throws Exception {
    // Title
    setTestTitle("Delete a sequence");

    // Go to screen
    gotoScreen("tools", "sequences");

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtKeyNam", "test", "test");

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents("test");

    // Click on delete button
    clickButton("ButGrdKeyLstDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyDeleted("test");
  }

  /**
   * Add a new screen restriction
   * @throws Exception
   */
  @Test
  public void t021_newRestriction() throws Exception {
    // Title
    setTestTitle("Add a new screen restriction");

    // Go to screen
    gotoScreen("settings", "security", "screen-access");

    // Wait for button
    clickButton("ButRst");

    // Wait for button
    clickButton("ButGrdAdd");

    // Select on selector
    suggest("GrdScrAccLst", "IdeOpe",  "test", "test");

    // Select on selector
    suggest("GrdScrAccLst", "Opt",  "application-info", "application-info");

    // Select text
    selectContain("GrdScrAccLst", "AccMod", "Restricted");

    // Select text
    selectContain("GrdScrAccLst", "Act", "Yes");

    // Save row
    saveRow();

    // Check row contents
    checkRowContents("application-info", "Restricted", "Yes");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggestMultiple("CrtOpe", "test", "test");

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info");

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("application-info");
  }

  /**
   * Update a screen restriction
   * @throws Exception
   */
  @Test
  public void t023_updateRestriction() throws Exception {
    // Title
    setTestTitle("Update a screen restriction");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggestMultiple("CrtOpe", "test", "test");

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info");

    // Wait for button
    searchAndWait();

    // Click on row
    clickRowContents("application-info");

    // Select on selector
    selectContain("GrdScrAccLst", "AccMod",  "Restricted");

    // Select on selector
    selectContain("GrdScrAccLst", "Act",  "No");

    // Save row
    saveRow();

    // Check row contents
    checkRowContents("Restricted", "No");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggestMultiple("CrtOpe", "test", "test");

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info");

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("application-info", "Restricted", "No");
  }

  /**
   * Delete a screen restriction
   * @throws Exception
   */
  @Test
  public void t025_deleteRestriction() throws Exception {
    // Title
    setTestTitle("Delete a screen restriction");

    // Go to screen
    gotoScreen("settings", "security", "screen-access");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggestMultiple("CrtOpe", "test", "test");

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info");

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents("application-info");

    // Click on delete button
    clickButton("ButGrdDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyDeleted("application-info");
  }

  /**
   * Add a new email server
   * @throws Exception
   */
  @Test
  public void t031_newEmailServer() throws Exception {
    // Title
    setTestTitle("Add a new email server");

    // Go for new screen
    addNew("tools", "email-servers");

    // Insert text
    writeText("SrvNam", "test server");

    // Insert text
    writeText("Hst", "localhost");

    // Check box
    clickCheckbox("Ath");

    // Insert text
    writeText("EmlUsr", "test");

    // Insert text
    writeText("EmlPwd", "test");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    verifyNewEmailServer();
  }

  /**
   * Verify an email server has been added
   * @throws Exception
   */
  private void verifyNewEmailServer() throws Exception {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtNam", "test", "test");

    // Search on grid
    searchAndWait();

    // Check row contents
    checkRowContents("test server", "localhost");
  }

  /**
   * Update an email server
   * @throws Exception
   */
  @Test
  public void t033_updateEmailServer() throws Exception {
    // Title
    setTestTitle("Update an email server");

    // Go to update
    update("CrtNam", "test", "tools", "email-servers");

    // Insert text
    writeText("SrvNam", "server update");

    // Insert text
    writeText("EmlUsr", "test2");

    // Insert text
    writeText("EmlPwd", "test2");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    verifyUpdatedEmailServerNoAuth();

    // Check row contents
    checkRowContents("test2");
  }

  /**
   * Delete an email server
   * @throws Exception
   */
  @Test
  public void t035_deleteEmailServer() throws Exception {
    // Title
    setTestTitle("Delete an email server");

    // Delete email server
    delete("CrtNam", "server update", "tools", "email-servers");

    // Verify deleted
    verifyDeleted("server update");
  }

  /**
   * Add a new email server without authentication
   * @throws Exception
   */
  @Test
  public void t041_newEmailServerNoAuth() throws Exception {
    // Title
    setTestTitle("Add a new email server without authentication");

    // Go for new screen
    addNew("tools", "email-servers");

    // Insert text
    writeText("SrvNam", "test server");

    // Insert text
    writeText("Hst", "localhost");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify new email server
    verifyNewEmailServer();
  }

  /**
   * Update an email server without authentication
   * @throws Exception
   */
  @Test
  public void t043_updateEmailServerNoAuth() throws Exception {
    // Title
    setTestTitle("Update an email server without authentication");

    // Go to update
    update("CrtNam", "test", "tools", "email-servers");

    // Insert text
    writeText("SrvNam", "server update");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyUpdatedEmailServerNoAuth();
  }

  /**
   * Verify an email server without authentication has been updated
   * @throws Exception
   */
  private void verifyUpdatedEmailServerNoAuth() throws Exception {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtNam", "server update", "server update");

    // Search on grid
    searchAndWait();

    // Check row contents
    checkRowContents("server update");
  }

  /**
   * Delete an email server without authentication
   * @throws Exception
   */
  @Test
  public void t045_deleteEmailServerNoAuth() throws Exception {
    // Title
    setTestTitle("Delete an email server without authentication");

    // Delete email server
    delete("CrtNam", "server update", "tools", "email-servers");

    // Verify deleted email server
    verifyDeleted("server update");
  }

  /**
   * Add a new screen configuration
   * @throws Exception
   */
  @Test
  public void t051_newScreenConfiguration() throws Exception {
    // Title
    setTestTitle("Add a new screen configuration");

    // Go to screen
    gotoScreen("settings", "screen-configuration");

    // Wait for button
    clickButton("ButRst");

    // Wait for button
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
    writeText("GrdScrCnf", "Val", "true");

    // Select text
    selectContain("GrdScrCnf", "Act", "Yes");

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtScr", "Dbs", "Dbs");

    // Suggest
    suggest("CrtUsr", "test", "test");

    // Select
    selectContain("CrtAct", "Yes");

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("Dbs", "test", "ButPrn", "Visible", "true");
  }

  /**
   * Update a screen configuration
   * @throws Exception
   */
  @Test
  public void t053_updateScreenConfiguration() throws Exception {
    // Title
    setTestTitle("Update a screen configuration");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtScr", "Dbs", "Dbs");

    // Suggest
    suggest("CrtUsr", "test", "test");

    // Select
    selectContain("CrtAct", "Yes");

    // Wait for button
    searchAndWait();

    // Click on row
    clickRowContents("Dbs");

    // Select on selector
    writeText("GrdScrCnf", "Val",  "false");

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtScr", "Dbs", "Dbs");

    // Suggest
    suggest("CrtUsr", "test", "test");

    // Select
    selectContain("CrtAct", "Yes");

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("Dbs", "test", "ButPrn", "Visible", "false");
  }

  /**
   * Delete a screen configuration
   * @throws Exception
   */
  @Test
  public void t055_deleteScreenConfiguration() throws Exception {
    // Title
    setTestTitle("Delete a screen configuration");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtScr", "Dbs", "Dbs");

    // Suggest
    suggest("CrtUsr", "test", "test");

    // Select
    selectContain("CrtAct", "Yes");

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents("Dbs");

    // Click on delete button
    clickButton("ButGrdDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyDeleted("Dbs");
  }

  /**
   * View a log file
   * @throws Exception
   */
  @Test
  public void t061_viewLog() throws Exception {
    // Title
    setTestTitle("View a log file");

    // Go to log screen
    gotoScreen("tools", "log");

    // Wait for reset button
    waitForButton("ButRst");

    // Write on criterion
    writeText("CrtFil", "awe-boot_test");

    // Search and wait
    searchAndWait();

    // Click on row
    clickRowContents("awe-boot");

    // Click on button
    clickButton("ButViw", true);

    // Wait for button
    waitForButton("ButBck");

    // Check text
    waitForText("visible-text", "INFO");

    // Check text
    checkTextContains(".visible-text", "INFO");
  }

  /**
   * Broadcast message to a user
   * @throws Exception
   */
  @Test
  public void t062_broadcastMessage() throws Exception {
    // Title
    setTestTitle("Broadcast message to a user");

    // Broadcast messaget o user
    broadcastMessageToUser("test", "This is a broadcast message test");

    // Check message box is empty
    checkCriterionContents("MsgDes", "");
  }

  /**
   * Encrypt text with encryption tools
   * @throws Exception
   */
  @Test
  public void t063_encryptText() throws Exception {
    // Title
    setTestTitle("Encrypt text with encryption tools");

    // Go to log screen
    gotoScreen("settings", "security", "encrypt-tools");

    // Wait for reset button
    waitForButton("ButRst");

    // Write on criterion
    writeText("CrtTxt", "Texto de prueba");

    // Wait for reset button
    clickButton("ButEnc");

    // Check criterion contents
    checkCriterionContents("CrtEnc", "dOakAf2lwfqAke4O41A0Ww==");
  }
}
