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

    // Select text
    writeText("Ord", "3");

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
    selectContain("MdlPrfLst", "IdePro", "ADM - Administrator");

    // Save line
    saveRow("MdlPrfLst");

    // Check row values
    checkRowContentsGrid("MdlPrfLst","ADM - Administrator");

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
    selectContain("Dct",  "Jdbc");

    // Insert text
    writeText("Dbc", "jdbc:hsqldb:file:awe-tests/awe-boot/target/db/awe-boot");

    // Select on selector
    selectContain("Typ",  "Development");

    // Insert text
    writeText("Des", "This is a test case of new DataBase");

    // Select on selector
    selectContain("Dbt",  "HSQL");

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
}
