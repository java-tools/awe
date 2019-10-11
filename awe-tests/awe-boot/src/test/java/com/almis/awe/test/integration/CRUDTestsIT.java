package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDTestsIT extends SeleniumUtilities {

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
   * Add a new user
   */
  private void addNewUser() {
    // Go for new screen
    addNew("tools", "users");

    // Insert text
    writeText("Usr", "test selenium");

    // Select on selector
    selectContain("Sta",  "Yes");

    // Insert text
    writeText("Pas", "1234");

    // Insert text
    writeText("RetPas", "1234");

    // Insert text
    writeText("Nam", "test");

    // Suggest on selector
    suggest("Pro", "TS1", "TS1");

    // Suggest on selector
    suggest("Thm", "grass", "grass");

    // Insert text
    writeText("Eml", "test@almis.com");
  }

  /**
   * Add a new site
   * @throws Exception
   */
  @Test
  public void t001_newSite() throws Exception {
    // Title
    setTestTitle("Add a new site");

    // Go for new screen
    addNew("tools", "sites");

    // Write on criterion
    writeText("Nam", "Site test");

    // Select on selector
    selectLast("Act");

    // Write on criterion
    writeText("Ord", "3");

    // Click on button
    clickButton("ButGrdAdd");

    // Suggest on column selector
    suggest("SitModDbsLst", "IdeMod", "Base", "Base");

    // Suggest on column selector
    suggest("SitModDbsLst", "IdeDbs", "awedb", "awedb");

    // Write on criterion
    writeText("SitModDbsLst", "Order", "3");

    // Save line
    saveRow();

    // Check row values
    checkRowContents("Base", "awedb", "3");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtSit", "Site test");

    // Check row contents
    checkRowContents("Base", "awedb");
  }

  /**
   * Update a site
   * @throws Exception
   */
  @Test
  public void t003_updateSite() throws Exception {
    // Title
    setTestTitle("Update a site");

    // Go to update
    update("CrtSit", "Site", "tools", "sites");

    // Write on criterion
    writeText("Nam", "Site changed");

    // Click row
    clickRowContents("Base");

    // Suggest on column selector
    suggest("SitModDbsLst", "IdeDbs", "awedb2", "awedb2");

    // Save line
    saveRow();

    // Check row values
    checkRowContents("Base", "awedb2", "3");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtSit", "Site changed");

    // Check row contents
    checkRowContents("Base", "awedb2");
  }

  /**
   * Add a new module
   * @throws Exception
   */
  @Test
  public void t011_newModule() throws Exception {
    // Title
    setTestTitle("Add a new module");

    // Go for new screen
    addNew("tools", "modules");

    // Select option
    suggest("Nam", "Inf", "Inf");

    // Suggest on selector
    suggest("Scr", "Sites", "Sites");

    // Suggest on  selector
    suggest("Thm", "gra", "gra");

    // Select on selector
    suggest("Act",  "Yes", "Yes");

    // Click on button
    clickButton("ButMdlUsrLstAdd");

    // Suggest on column selector
    suggest("MdlUsrLst", "IdeOpe", "test", "test");

    // Suggest on column selector
    suggest("MdlUsrLst", "IdeThm", "sky", "sky");

    // Save line
    saveRow("MdlUsrLst");

    // Check row values
    checkRowContentsGrid("MdlUsrLst","test", "sky");

    // Click on button
    clickButton("ButMdlPrfLstAdd");

    // Suggest on column selector
    suggest("MdlPrfLst", "IdePro", "TS", "TS");

    // Save line
    saveRow("MdlPrfLst");

    // Check row values
    checkRowContentsGrid("MdlPrfLst","TS");

    // Click on button
    clickButton("ButMdlSitDbsLstAdd");

    // Suggest on column selector
    suggest("MdlSitDbsLst", "IdeSit", "Site", "Site");

    // Suggest on column selector
    suggest("MdlSitDbsLst", "IdeDbs", "awedb", "awedb");

    // Save line
    saveRow("MdlSitDbsLst");

    // Check row values
    checkRowContentsGrid("MdlSitDbsLst","Site", "awedb");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtMod", "Inf");

    // Check contents
    checkCriterionContents("Nam", "Inf");

    // Check contents
    checkCriterionContents("Thm", "grass");

    // Check contents
    checkSelectContents("Scr", "Sites (Sit)");

    // Check row contents
    checkRowContents("test", "TST", "Site changed");
  }

  /**
   * Update the module
   * @throws Exception
   */
  @Test
  public void t013_updateModule() throws Exception {
    // Title
    setTestTitle("Update the module");

    // Go to update
    update("CrtMod", "Inf", "tools", "modules");

    // Write on criterion
    suggest("Nam", "Inf Changed","Inf Changed");

    // Write on criterion
    suggest("Scr", "Usr", "Usr");

    // Click row
    clickRowContents("test");

    // Suggest on column selector
    selectContain("MdlUsrLst", "IdeThm", "grass");

    // Save line
    saveRow("MdlUsrLst");

    // Check row values
    checkRowContentsGrid("MdlUsrLst","grass");

    // Click row
    clickRowContents("TST");

    // Suggest on column selector
    selectContain("MdlPrfLst", "IdePro", "ADM - administrator");

    // Save line
    saveRow("MdlPrfLst");

    // Check row values
    checkRowContentsGrid("MdlPrfLst","ADM - administrator");

    // Click row
    clickRowContents("Site changed");

    // Suggest on column selector
    selectContain("MdlSitDbsLst", "IdeDbs", "awedb2");

    // Save line
    saveRow("MdlSitDbsLst");

    // Check row values
    checkRowContentsGrid("MdlSitDbsLst","awedb2");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtMod", "Inf Changed");

    // Check criterion
    checkCriterionContents("Nam", "Inf Changed");

    // Check criterion
    checkCriterionContents("Thm", "grass");

    // Check criterion
    checkSelectContents("Scr", "Usr");

    // Check row contents
    checkRowContents("test", "ADM", "Site changed");
  }

  /**
   * Add a new profile
   * @throws Exception
   */
  @Test
  public void t021_newProfile() throws Exception {
    // Title
    setTestTitle("Add a new profile");

    // Go for new screen
    addNew("tools", "profiles");

    // Insert text
    writeText("Acr", "TS1");

    // Insert text
    writeText("Nam", "Test profile");

    // Select on selector
    suggest("Act",  "Yes", "Yes");

    // Suggest on  selector
    suggest("IdeThm", "sunse", "sunse");

    // Suggest on  selector
    suggest("ScrIni", "Modules", "Modules");

    // Verify criterion
    checkSelectContents("IdeThm", "sunset");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtPro", "TS1");

    // Check contents
    checkCriterionContents("Acr", "TS1");

    // Check contents
    checkCriterionContents("Nam", "Test profile");

    // Check contents
    checkSelectContents("Act", "Yes");
  }

  /**
   * Update a profile
   * @throws Exception
   */
  @Test
  public void t023_updateProfile() throws Exception {
    // Title
    setTestTitle("Update a profile");

    // Go to update
    update("CrtPro", "TS1", "tools", "profiles");

    // Insert text
    writeText("Nam", "Profile changed");

    // Verify criterion
    checkCriterionContents("Nam", "Profile changed");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtPro", "TS1");

    // Check contents
    checkCriterionContents("Acr", "TS1");

    // Check criterion
    checkCriterionContents("Nam", "Profile changed");

    // Check contents
    checkSelectContents("Act", "Yes");
  }

  /**
   * Add a new database connection
   * @throws Exception
   */
  @Test
  public void t031_newDatabase() throws Exception {
    // Title
    setTestTitle("Add a new database connection");

    // Go for new screen
    addNew("tools", "databases");

    // Insert text
    writeText("Als", "DBSTest");

    // Select on selector
    selectContain("Dct",  "Datasource");

    // Insert text
    writeText("Dbc", "jdbc/Test");

    // Select on selector
    selectContain("Typ",  "Development");

    // Insert text
    writeText("Des", "This is a test case of new DataBase");

    // Select on selector
    selectContain("Dbt",  "ORACLE");

    // Click on button
    clickButton("ButSitModDbsLstAdd");

    // Suggest on column selector
    suggest("SitModDbsLst", "IdeSit", "Site changed", "Site changed");

    // Suggest on column selector
    suggest("SitModDbsLst", "IdeMod", "Inf Changed", "Inf Changed");

    // Insert text
    writeText("SitModDbsLst","Ord", "5");

    // Save line
    saveRow();

    // Check row
    checkRowContents("Site changed", "Inf Changed", "5");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtAls", "DBSTest");

    // Check contents
    checkCriterionContents("Als", "DBSTest");

    // Check row contents
    checkRowContents("Site changed", "Inf Changed", "5");
  }

  /**
   * Update a database connection
   * @throws Exception
   */
  @Test
  public void t033_updateDatabase() throws Exception {
    // Title
    setTestTitle("Update a database connection");

    // Go to update
    update("CrtAls", "DBSTest", "tools", "databases");

    // Insert text
    writeText("Als", "DBSTest Changed");

    // Select on selector
    selectContain("Dct",  "Jdbc");

    // Insert text
    writeText("Dbc", "Test");

    // Insert text
    writeText("Des", "This is a tes case of modify DB");

    // Click on row
    clickRowContents("Site changed");

    // Suggest on column selector
    suggest("SitModDbsLst", "IdeMod", "Test", "Test");

    // Save line
    saveRow();

    // Check row
    checkRowContents("Test");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtAls", "DBSTest Changed");

    // Check contents
    checkCriterionContents("Als", "DBSTest Changed");

    // Check row contents
    checkRowContents("Test");
  }

  /**
   * Add a new user
   * @throws Exception
   */
  @Test
  public void t041_newUser() throws Exception {
    // Title
    setTestTitle("Add a new user");

    // Add a new user
    addNewUser();

    // Check new user added
    checkCriterionContents("Usr", "test selenium");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtUsr", "test sel");

    // Check contents
    checkCriterionContents("Nom", "test selenium");

    // Check contents
    checkSelectContents("Sta", "Yes");

    // Check contents
    checkCriterionContents("Nam", "test");

    // Check contents
    checkCriterionContents("Eml", "test@almis.com");

    // Check contents
    checkSelectContents("Thm", "grass");

    // Check contents
    checkSelectContents("Pro", "TS1 - Profile changed");
  }

  /**
   * Update a user
   * @throws Exception
   */
  @Test
  public void t043_updateUser() throws Exception {
    // Title
    setTestTitle("Update a user");

    // Go to update
    update("CrtUsr", "test sel", "tools", "users");

    // Insert text
    writeText("Eml", "testUpd@almis.com");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Verify
    verifyView("CrtUsr", "test sel");

    // Check contents
    checkCriterionContents("Eml", "testUpd@almis.com");
  }

  /**
   * Try to add a duplicated user
   * @throws Exception
   */
  @Test
  public void t045_newDuplicatedUser() throws Exception {
    // Title
    setTestTitle("Try to add a duplicated user");

    // Check user
    addNewUser();

    // Check text
    checkCriterionContents("Usr", "test selenium");

    // Store and confirm
    clickButtonAndConfirm("ButCnf", "warning");
  }

  /**
   * Delete a user
   * @throws Exception
   */
  @Test
  public void t050_deleteUser() throws Exception {
    // Title
    setTestTitle("Delete a user");

    // Delete the user
    delete("CrtUsr", "test sel", "tools", "users");

    // Verify deleted user
    verifyDeleted("test sel");
  }

  /**
   * Delete a database connection
   * @throws Exception
   */
  @Test
  public void t052_deleteDatabase() throws Exception {
    // Title
    setTestTitle("Delete a database connection");

    // Delete the database
    delete("CrtAls", "DBSTest", "tools", "databases");

    // Verify deleted
    verifyDeleted("DBSTest");
  }

  /**
   * Delete a profile
   * @throws Exception
   */
  @Test
  public void t054_deleteProfile() throws Exception {
    // Title
    setTestTitle("Delete a profile");

    // Delete the profile
    delete("CrtPro", "TS1", "tools", "profiles");

    // Verify deleted
    verifyDeleted("TS1");
  }

  /**
   * Delete a module
   * @throws Exception
   */
  @Test
  public void t056_deleteModule() throws Exception {
    // Title
    setTestTitle("Delete a module");

    // Delete the module
    delete("CrtMod", "Inf", "tools", "modules");

    // Verify deleted
    verifyDeleted("Inf");
  }

  /**
   * Delete a site
   * @throws Exception
   */
  @Test
  public void t058_deleteSite() throws Exception {
    // Title
    setTestTitle("Delete a site");

    // Delete the site
    delete("CrtSit", "Site", "tools", "sites");

    // Verify deleted site
    verifyDeleted("Site");
  }

  /**
   * Add a new theme
   * @throws Exception
   */
  @Test
  public void t061_newTheme() throws Exception {
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
  public void t063_updateTheme() throws Exception {
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
  public void t065_deleteTheme() throws Exception {
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
  public void t071_newSequence() throws Exception {
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
  public void t073_updateSequence() throws Exception {
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
  public void t075_deleteSequence() throws Exception {
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
  public void t081_newRestriction() throws Exception {
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
  public void t083_updateRestriction() throws Exception {
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
  public void t085_deleteRestriction() throws Exception {
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
  public void t091_newEmailServer() throws Exception {
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
  public void t093_updateEmailServer() throws Exception {
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
  public void t095_deleteEmailServer() throws Exception {
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
  public void t101_newEmailServerNoAuth() throws Exception {
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
  public void t103_updateEmailServerNoAuth() throws Exception {
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
  public void t105_deleteEmailServerNoAuth() throws Exception {
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
  public void t111_newScreenConfiguration() throws Exception {
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
  public void t113_updateScreenConfiguration() throws Exception {
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
  public void t115_deleteScreenConfiguration() throws Exception {
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
  public void t121_viewLog() throws Exception {
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
  public void t122_broadcastMessage() throws Exception {
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
  public void t123_encryptText() throws Exception {
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
