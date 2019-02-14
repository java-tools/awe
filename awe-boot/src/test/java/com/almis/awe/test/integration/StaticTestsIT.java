package com.almis.awe.test.integration;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaticTestsIT extends SeleniumTestsUtil {

  /**
   * Log into the application
   * @throws Exception
   */
  @Test
  public void t000_loginTest() throws Exception {
    doLogin();
  }

  /**
   * Log out from the application
   * @throws Exception
   */
  @Test
  public void t999_logoutTest() throws Exception {
    doLogout();
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
    suggest(suggest, search, search, false);

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
  private void clickButtonAndConfirm(String button) {
    clickButtonAndConfirm(button, "success");
  }

  /**
   * Click on confirm button, accept confirmation and accept message
   */
  private void clickButtonAndConfirm(String button, String messageType) {
    // Click on button
    clickButton(button);

    // Accept confirm
    acceptConfirm();

    // Accept message
    acceptMessage(messageType);
  }

  /**
   * Click on confirm button, accept confirmation and accept message
   */
  private void verifyView(String suggest, String search) {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(suggest, search, search, false);

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
    suggest(criterion, search, search, false);

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
   * @param options
   */
  private void verifyDeleted(String search, String... options) {
    // Go to screen
    gotoScreen(options);

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
    writeText("Usr", "test selenium", false);

    // Select on selector
    selectContain("Sta",  "Yes", false);

    // Insert text
    writeText("Pas", "1234", false);

    // Insert text
    writeText("RetPas", "1234", false);

    // Insert text
    writeText("Nam", "test", false);

    // Suggest on selector
    suggest("Pro", "TS1", "TS1", false);

    // Suggest on selector
    suggest("Thm", "grass", "grass", false);

    // Insert text
    writeText("Eml", "test@almis.com", false);
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
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a new site has been added
   * @throws Exception
   */
  @Test
  public void t002_verifyNewSite() throws Exception {
    // Title
    setTestTitle("Verify a new site has been added");

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
    writeText("Nam", "Site changed", false);

    // Click row
    clickRowContents("Base");

    // Suggest on column selector
    suggest("IdeDbs", "awedb2", "awedb2", true);

    // Save line
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the site has been updated
   * @throws Exception
   */
  @Test
  public void t004_verifyUpdatedSite() throws Exception {
    // Title
    setTestTitle("Verify the site has been updated");

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
    saveRow("MdlUsrLst");

    // Click on button
    clickButton("ButMdlPrfLstAdd");

    // Suggest on column selector
    suggest("IdePro", "TS", "TS", true);

    // Save line
    saveRow("MdlPrfLst");

    // Click on button
    clickButton("ButMdlSitDbsLstAdd");

    // Suggest on column selector
    suggest("IdeSit", "Site", "Site", true);

    // Suggest on column selector
    suggest("IdeDbs", "awedb", "awedb", true);

    // Save line
    saveRow("MdlSitDbsLst");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a new module has been added
   * @throws Exception
   */
  @Test
  public void t012_verifyNewModule() throws Exception {
    // Title
    setTestTitle("Verify a new module has been added");

    // Verify
    verifyView("CrtMod", "Inf");

    // Check contents
    checkCriterionContents("Nam", "Inf");

    // Check contents
    checkCriterionContents("Thm", "grass");

    // Check contents
    checkSelectorContents("Scr", "Sites (Sit)");

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
    suggest("Nam", "Inf Changed","Inf Changed", false);

    // Write on criterion
    suggest("Scr", "Usr", "Usr", false);

    // Click row
    clickRowContents("test");

    // Suggest on column selector
    selectContain("IdeThm", "grass", true);

    // Save line
    saveRow("MdlUsrLst");

    // Click row
    clickRowContents("TST");

    // Suggest on column selector
    selectContain("IdePro", "ADM - administrator", true);

    // Save line
    saveRow("MdlPrfLst");

    // Click row
    clickRowContents("Site changed");

    // Suggest on column selector
    selectContain("IdeDbs", "awedb", true);

    // Save line
    saveRow("MdlSitDbsLst");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the module has been updated
   * @throws Exception
   */
  @Test
  public void t014_verifyUpdatedModule() throws Exception {
    // Title
    setTestTitle("Verify the module has been updated");

    // Verify
    verifyView("CrtMod", "Inf Changed");

    // Check criterion
    checkCriterionContents("Nam", "Inf Changed");

    // Check criterion
    checkCriterionContents("Thm", "grass");

    // Check criterion
    checkSelectorContents("Scr", "Usr");

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
    writeText("Acr", "TS1", false);

    // Insert text
    writeText("Nam", "Test profile", false);

    // Select on selector
    suggest("Act",  "Yes", "Yes", false);

    // Suggest on  selector
    suggest("IdeThm", "sunse", "sunse", false);

    // Suggest on  selector
    suggest("ScrIni", "Modules", "Modules", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a new profile has been added
   * @throws Exception
   */
  @Test
  public void t022_verifyNewProfile() throws Exception {
    // Title
    setTestTitle("Verify a new profile has been added");

    // Verify
    verifyView("CrtPro", "TS1");

    // Check contents
    checkCriterionContents("Acr", "TS1");

    // Check contents
    checkCriterionContents("Nam", "Test profile");

    // Check contents
    checkSelectorContents("Act", "Yes");
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
    writeText("Nam", "Profile changed", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the profile has been updated
   * @throws Exception
   */
  @Test
  public void t024_verifyUpdatedProfile() throws Exception {
    // Title
    setTestTitle("Verify the profile has been updated");

    // Verify
    verifyView("CrtPro", "TS1");

    // Check contents
    checkCriterionContents("Acr", "TS1");

    // Check criterion
    checkCriterionContents("Nam", "Profile changed");

    // Check contents
    checkSelectorContents("Act", "Yes");
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
    writeText("Als", "DBSTest", false);

    // Select on selector
    selectContain("Dct",  "Datasource", false);

    // Insert text
    writeText("Dbc", "jdbc/Test", false);

    // Select on selector
    selectContain("Typ",  "Development", false);

    // Insert text
    writeText("Des", "This is a test case of new DataBase", false);

    // Select on selector
    selectContain("Dbt",  "ORACLE", false);

    // Click on button
    clickButton("ButSitModDbsLstAdd");

    // Suggest on column selector
    suggest("IdeSit", "Site changed", "Site changed", true);

    // Suggest on column selector
    suggest("IdeMod", "Inf Changed", "Inf Changed", true);

    // Insert text
    writeText("Ord", "5", true);

    // Save line
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the database connection has been added
   * @throws Exception
   */
  @Test
  public void t032_verifyNewDatabase() throws Exception {
    // Title
    setTestTitle("Verify the database connection has been added");

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
    writeText("Als", "DBSTest Changed", false);

    // Select on selector
    selectContain("Dct",  "Jdbc", false);

    // Insert text
    writeText("Dbc", "Test", false);

    // Insert text
    writeText("Des", "This is a tes case of modify DB", false);

    // Click on row
    clickRowContents("Site changed");

    // Suggest on column selector
    suggest("IdeMod", "Test", "Test", true);

    // Save line
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the database connection has been updated
   * @throws Exception
   */
  @Test
  public void t034_verifyUpdatedDatabase() throws Exception {
    // Title
    setTestTitle("Verify the database connection has been updated");

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

    addNewUser();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a new user has been added
   * @throws Exception
   */
  @Test
  public void t042_verifyNewUser() throws Exception {
    // Title
    setTestTitle("Verify a new user has been added");

    // Verify
    verifyView("CrtUsr", "test sel");

    // Check contents
    checkCriterionContents("Nom", "test selenium");

    // Check contents
    checkSelectorContents("Sta", "Yes");

    // Check contents
    checkCriterionContents("Nam", "test");

    // Check contents
    checkCriterionContents("Eml", "test@almis.com");

    // Check contents
    checkSelectorContents("Thm", "grass");

    // Check contents
    checkSelectorContents("Pro", "TS1 - Profile changed");
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
    writeText("Eml", "testUpd@almis.com", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the user has been updated
   * @throws Exception
   */
  @Test
  public void t044_verifyUpdatedUser() throws Exception {
    // Title
    setTestTitle("Verify the user has been updated");

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

    addNewUser();

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

    delete("CrtUsr", "test sel", "tools", "users");
  }

  /**
   * Verify the user has been deleted
   * @throws Exception
   */
  @Test
  public void t051_verifyDeletedUser() throws Exception {
    // Title
    setTestTitle("Verify the user has been deleted");

    verifyDeleted("test sel", "tools", "users");
  }

  /**
   * Delete a database connection
   * @throws Exception
   */
  @Test
  public void t052_deleteDatabase() throws Exception {
    // Title
    setTestTitle("Delete a database connection");

    delete("CrtAls", "DBSTest", "tools", "databases");
  }

  /**
   * Verify the database connection has been deleted
   * @throws Exception
   */
  @Test
  public void t053_verifyDeletedDatabase() throws Exception {
    // Title
    setTestTitle("Verify the database connection has been deleted");

    verifyDeleted("DBSTest", "tools", "databases");
  }

  /**
   * Delete a profile
   * @throws Exception
   */
  @Test
  public void t054_deleteProfile() throws Exception {
    // Title
    setTestTitle("Delete a profile");

    delete("CrtPro", "TS1", "tools", "profiles");
  }

  /**
   * Verify the profile has been deleted
   * @throws Exception
   */
  @Test
  public void t055_verifyDeletedProfile() throws Exception {
    // Title
    setTestTitle("Verify the profile has been deleted");

    verifyDeleted("TS1", "tools", "profiles");
  }

  /**
   * Delete a module
   * @throws Exception
   */
  @Test
  public void t056_deleteModule() throws Exception {
    // Title
    setTestTitle("Delete a module");

    delete("CrtMod", "Inf", "tools", "modules");
  }

  /**
   * Verify the module has been deleted
   * @throws Exception
   */
  @Test
  public void t057_verifyDeletedModule() throws Exception {
    // Title
    setTestTitle("Verify the module has been deleted");

    verifyDeleted("Inf", "tools", "modules");
  }

  /**
   * Delete a site
   * @throws Exception
   */
  @Test
  public void t058_deleteSite() throws Exception {
    // Title
    setTestTitle("Delete a site");

    delete("CrtSit", "Site", "tools", "sites");
  }

  /**
   * Verify the site has been deleted
   * @throws Exception
   */
  @Test
  public void t059_verifyDeletedSite() throws Exception {
    // Title
    setTestTitle("Verify the site has been deleted");

    verifyDeleted("Site", "tools", "sites");
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
    writeText("Nam", "Theme test", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the new theme has been added
   * @throws Exception
   */
  @Test
  public void t062_verifyNewTheme() throws Exception {
    // Title
    setTestTitle("Verify the new theme has been added");

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
    writeText("Nam", "Theme changed", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the theme has been updated
   * @throws Exception
   */
  @Test
  public void t064_verifyUpdatedTheme() throws Exception {
    // Title
    setTestTitle("Verify the theme has been updated");

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

    delete("CrtNam", "Theme changed", "tools", "themes");
  }

  /**
   * Verify the theme has been deleted
   * @throws Exception
   */
  @Test
  public void t066_verifyDeletedTheme() throws Exception {
    // Title
    setTestTitle("Verify the theme has been deleted");

    verifyDeleted("Theme changed", "tools", "themes");
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
    writeText("KeyNam", "testKey", true);

    // Insert text
    writeText("KeyVal", "0", true);

    // Select on selector
    selectContain("Act",  "Yes", true);

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the sequence has been added
   * @throws Exception
   */
  @Test
  public void t072_verifyNewSequence() throws Exception {
    // Title
    setTestTitle("Verify the sequence has been added");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test", false);

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
    suggest("CrtKeyNam", "test", "test", false);

    // Wait for button
    searchAndWait();

    // Click on row
    clickRowContents("testKey");

    // Insert text
    writeText("KeyVal", "1", true);

    // Select on selector
    selectContain("Act",  "No", true);

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the sequence has been updated
   * @throws Exception
   */
  @Test
  public void t074_verifyUpdatedSequence() throws Exception {
    // Title
    setTestTitle("Verify the sequence has been updated");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test", false);

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
    suggest("CrtKeyNam", "test", "test", false);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents("test");

    // Click on delete button
    clickButton("ButGrdKeyLstDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify the sequence has been deleted
   * @throws Exception
   */
  @Test
  public void t076_verifyDeletedSequence() throws Exception {
    // Title
    setTestTitle("Verify the sequence has been deleted");

    verifyDeleted("test", "tools", "sequences");
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
    suggest("IdeOpe",  "test", "test", true);

    // Select on selector
    suggest("Opt",  "application-info", "application-info", true);

    // Select text
    selectContain("AccMod", "Restricted", true);

    // Select text
    selectContain("Act", "Yes", true);

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a screen restriction has been added
   * @throws Exception
   */
  @Test
  public void t082_verifyNewRestriction() throws Exception {
    // Title
    setTestTitle("Verify a screen restriction has been added");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggestMultiple("CrtOpe", "test", "test", false);

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info", false);

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
    suggestMultiple("CrtOpe", "test", "test", false);

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info", false);

    // Wait for button
    searchAndWait();

    // Click on row
    clickRowContents("application-info");

    // Select on selector
    selectContain("AccMod",  "Restricted", true);

    // Select on selector
    selectContain("Act",  "No", true);

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a screen restriction has been updated
   * @throws Exception
   */
  @Test
  public void t084_verifyUpdatedRestriction() throws Exception {
    // Title
    setTestTitle("Verify a screen restriction has been updated");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggestMultiple("CrtOpe", "test", "test", false);

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info", false);

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
    suggestMultiple("CrtOpe", "test", "test", false);

    // Suggest
    suggestMultiple("CrtOpc", "application-info", "application-info", false);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents("application-info");

    // Click on delete button
    clickButton("ButGrdDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a screen restriction has been deleted
   * @throws Exception
   */
  @Test
  public void t086_verifyDeletedRestriction() throws Exception {
    // Title
    setTestTitle("Verify a screen restriction has been deleted");

    verifyDeleted("application-info", "settings", "security", "screen-access");
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
    writeText("SrvNam", "test server", false);

    // Insert text
    writeText("Hst", "localhost", false);

    // Check box
    clickCheckbox("Ath", false);

    // Insert text
    writeText("EmlUsr", "test", false);

    // Insert text
    writeText("EmlPwd", "test", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify an email server has been added
   * @throws Exception
   */
  @Test
  public void t092_verifyNewEmailServer() throws Exception {
    // Title
    setTestTitle("Verify an email server has been added");

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
    suggest("CrtNam", "test", "test", false);

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
    writeText("SrvNam", "server update", false);

    // Insert text
    writeText("EmlUsr", "test2", false);

    // Insert text
    writeText("EmlPwd", "test2", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify an email server has been updated
   * @throws Exception
   */
  @Test
  public void t094_verifyUpdatedEmailServer() throws Exception {
    // Title
    setTestTitle("Verify an email server has been updated");

    // Wait for button
    t104_verifyUpdatedEmailServerNoAuth();

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
  }

  /**
   * Verify an email server has been deleted
   * @throws Exception
   */
  @Test
  public void t096_verifyDeletedEmailServer() throws Exception {
    // Title
    setTestTitle("Verify an email server has been deleted");

    // Verify deleted
    verifyDeleted("server update", "tools", "email-servers");
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
    writeText("SrvNam", "test server", false);

    // Insert text
    writeText("Hst", "localhost", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify an email server without authentication has been added
   * @throws Exception
   */
  @Test
  public void t102_verifyNewEmailServerNoAuth() throws Exception {
    // Title
    setTestTitle("Verify an email server without authentication has been added");

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
    writeText("SrvNam", "server update", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify an email server without authentication has been updated
   * @throws Exception
   */
  @Test
  public void t104_verifyUpdatedEmailServerNoAuth() throws Exception {
    // Title
    setTestTitle("Verify an email server without authentication has been updated");

    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest("CrtNam", "server update", "server update", false);

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
  }

  /**
   * Verify an email server without authentication has been deleted
   * @throws Exception
   */
  @Test
  public void t106_verifyDeletedEmailServerNoAuth() throws Exception {
    // Title
    setTestTitle("Verify an email server without authentication has been deleted");

    // Verify deleted email server
    verifyDeleted("server update", "tools", "email-servers");
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
    suggest("Scr",  "Dbs", "Dbs", true);

    // Select on selector
    suggest("IdeOpe",  "test", "test", true);

    // Select on selector
    suggest("Nam",  "ButPrn", "ButPrn", true);

    // Select text
    suggest("Atr", "visible", "Visible", true);

    // Select text
    writeText("Val", "true", true);

    // Select text
    selectContain("Act", "Yes", true);

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a screen configuration has been added
   * @throws Exception
   */
  @Test
  public void t112_verifyNewScreenConfiguration() throws Exception {
    // Title
    setTestTitle("Verify a screen configuration has been added");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtScr", "Dbs", "Dbs", false);

    // Suggest
    suggest("CrtUsr", "test", "test", false);

    // Select
    selectContain("CrtAct", "Yes", false);

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
    suggest("CrtScr", "Dbs", "Dbs", false);

    // Suggest
    suggest("CrtUsr", "test", "test", false);

    // Select
    selectContain("CrtAct", "Yes", false);

    // Wait for button
    searchAndWait();

    // Click on row
    clickRowContents("Dbs");

    // Select on selector
    writeText("Val",  "false", true);

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a screen configuration has been updated
   * @throws Exception
   */
  @Test
  public void t114_verifyUpdatedScreenConfiguration() throws Exception {
    // Title
    setTestTitle("Verify a screen configuration has been updated");

    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtScr", "Dbs", "Dbs", false);

    // Suggest
    suggest("CrtUsr", "test", "test", false);

    // Select
    selectContain("CrtAct", "Yes", false);

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
    suggest("CrtScr", "Dbs", "Dbs", false);

    // Suggest
    suggest("CrtUsr", "test", "test", false);

    // Select
    selectContain("CrtAct", "Yes", false);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents("Dbs");

    // Click on delete button
    clickButton("ButGrdDel");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  /**
   * Verify a screen configuration has been deleted
   * @throws Exception
   */
  @Test
  public void t116_verifyDeletedScreenConfiguration() throws Exception {
    // Title
    setTestTitle("Verify a screen configuration has been deleted");

    verifyDeleted("Dbs", "settings", "screen-configuration");
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
    writeText("CrtFil", "awe", false);

    // Search and wait
    searchAndWait();

    // Click on row
    clickRowContents("awe-boot");

    // Click on button
    clickButton("ButViw", true);

    // Wait for button
    waitForButton("ButBck");

    // Check text
    waitForText("visible-text", "[INFO ]");
  }

  /**
   * Broadcast message to a user
   * @throws Exception
   */
  @Test
  public void t122_broadcastMessage() throws Exception {
    // Title
    setTestTitle("Broadcast message to a user");

    // Go to log screen
    gotoScreen("tools", "broadcast-messages");

    // Suggest
    suggest("MsgTar", "test", "test", false);

    // Write on criterion
    writeText("MsgDes", "This is a broadcast message test", false);

    // Search and wait
    clickButton("ButSnd");

    // Accept message
    acceptMessage("info");

    // Accept message
    acceptMessage("success");
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
    writeText("CrtTxt", "Texto de prueba", false);

    // Wait for reset button
    clickButton("ButEnc");

    // Check criterion contents
    checkCriterionContents("CrtEnc", "dOakAf2lwfqAke4O41A0Ww==");
  }
}
