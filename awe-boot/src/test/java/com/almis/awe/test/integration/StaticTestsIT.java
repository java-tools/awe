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

  @Test
  public void t001_newSite() throws Exception {
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

  @Test
  public void t002_verifyNewSite() throws Exception {
    // Verify
    verifyView("CrtSit", "Site test");

    // Check row contents
    checkRowContents("Base");

    // Check row contents
    checkRowContents("awedb");
  }

  @Test
  public void t003_updateSite() throws Exception {
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

  @Test
  public void t004_verifyUpdatedSite() throws Exception {
    // Verify
    verifyView("CrtSit", "Site changed");

    // Check row contents
    checkRowContents("Base");

    // Check row contents
    checkRowContents("awedb2");
  }

  @Test
  public void t011_newModule() throws Exception {
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

  @Test
  public void t012_verifyNewModule() throws Exception {
    // Verify
    verifyView("CrtMod", "Inf");

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

  @Test
  public void t014_verifyUpdatedModule() throws Exception {
    // Verify
    verifyView("CrtMod", "Inf Changed");

    // Check criterion
    checkCriterionContents("Nam", "Inf Changed");

    // Check criterion
    checkCriterionContents("Thm", "grass");

    // Check criterion
    checkSelectorContents("Scr", "Usr");

    // Check row contents
    checkRowContents("test");

    // Check row contents
    checkRowContents("ADM");

    // Check row contents
    checkRowContents("Site changed");
  }

  @Test
  public void t021_newProfile() throws Exception {
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

  @Test
  public void t022_verifyNewProfile() throws Exception {
    // Verify
    verifyView("CrtPro", "TS1");

    // Check contents
    checkCriterionContents("Acr", "TS1");

    // Check contents
    checkCriterionContents("Nam", "Test profile");

    // Check contents
    checkSelectorContents("Act", "Yes");
  }

  @Test
  public void t023_updateProfile() throws Exception {
    // Go to update
    update("CrtPro", "TS1", "tools", "profiles");

    // Insert text
    writeText("Nam", "Profile changed", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  @Test
  public void t024_verifyUpdatedProfile() throws Exception {
    // Verify
    verifyView("CrtPro", "TS1");

    // Check contents
    checkCriterionContents("Acr", "TS1");

    // Check criterion
    checkCriterionContents("Nam", "Profile changed");

    // Check contents
    checkSelectorContents("Act", "Yes");
  }

  @Test
  public void t031_newDatabase() throws Exception {
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

  @Test
  public void t032_verifyNewDatabase() throws Exception {
    // Verify
    verifyView("CrtAls", "DBSTest");

    // Check contents
    checkCriterionContents("Als", "DBSTest");

    // Check row contents
    checkRowContents("Site changed");

    // Check row contents
    checkRowContents("Inf Changed");

    // Check row contents
    checkRowContents("5");
  }

  @Test
  public void t033_updateDatabase() throws Exception {
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

  @Test
  public void t034_verifyUpdatedDatabase() throws Exception {
    // Verify
    verifyView("CrtAls", "DBSTest Changed");

    // Check contents
    checkCriterionContents("Als", "DBSTest Changed");

    // Check row contents
    checkRowContents("Test");
  }

  @Test
  public void t041_newUser() throws Exception {
    addNewUser();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  @Test
  public void t042_verifyNewUser() throws Exception {
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

  @Test
  public void t043_updateUser() throws Exception {
    // Go to update
    update("CrtUsr", "test sel", "tools", "users");

    // Insert text
    writeText("Eml", "testUpd@almis.com", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  @Test
  public void t044_verifyUpdatedUser() throws Exception {
    // Verify
    verifyView("CrtUsr", "test sel");

    // Check contents
    checkCriterionContents("Eml", "testUpd@almis.com");
  }

  @Test
  public void t045_newDuplicatedUser() throws Exception {
    addNewUser();

    // Store and confirm
    clickButtonAndConfirm("ButCnf", "warning");
  }

  @Test
  public void t050_deleteUser() throws Exception {
    delete("CrtUsr", "test sel", "tools", "users");
  }

  @Test
  public void t051_verifyDeletedUser() throws Exception {
    verifyDeleted("test sel", "tools", "users");
  }

  @Test
  public void t052_deleteDatabase() throws Exception {
    delete("CrtAls", "DBSTest Changed", "tools", "databases");
  }

  @Test
  public void t053_verifyDeletedDatabase() throws Exception {
    verifyDeleted("DBSTest Changed", "tools", "databases");
  }

  @Test
  public void t054_deleteProfile() throws Exception {
    delete("CrtPro", "TS1", "tools", "profiles");
  }

  @Test
  public void t055_verifyDeletedProfile() throws Exception {
    verifyDeleted("TS1", "tools", "profiles");
  }

  @Test
  public void t056_deleteModule() throws Exception {
    delete("CrtMod", "Inf", "tools", "modules");
  }

  @Test
  public void t057_verifyDeletedModule() throws Exception {
    verifyDeleted("Inf", "tools", "modules");
  }

  @Test
  public void t058_deleteSite() throws Exception {
    delete("CrtSit", "Site", "tools", "sites");
  }

  @Test
  public void t059_verifyDeletedSite() throws Exception {
    verifyDeleted("Site", "tools", "sites");
  }

  @Test
  public void t061_newTheme() throws Exception {
    addNew("tools", "themes");

    // Insert text
    writeText("Nam", "Theme test", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  @Test
  public void t062_verifyNewTheme() throws Exception {
    // Verify
    verifyView("CrtNam", "test");

    // Check contents
    checkCriterionContents("Nam", "Theme test");
  }

  @Test
  public void t063_updateTheme() throws Exception {
    // Go to update
    update("CrtNam", "test", "tools", "themes");

    // Insert text
    writeText("Nam", "Theme changed", false);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");
  }

  @Test
  public void t064_verifyUpdatedTheme() throws Exception {
    // Verify
    verifyView("CrtNam", "Theme changed");

    // Check contents
    checkCriterionContents("Nam", "Theme changed");
  }

  @Test
  public void t065_deleteTheme() throws Exception {
    delete("CrtNam", "Theme changed", "tools", "themes");
  }

  @Test
  public void t066_verifyDeletedTheme() throws Exception {
    verifyDeleted("Theme changed", "tools", "themes");
  }

  @Test
  public void t071_newSequence() throws Exception {
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

  @Test
  public void t072_verifyNewSequence() throws Exception {
    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test", false);

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("testKey");
  }

  @Test
  public void t073_updateSequence() throws Exception {
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

  @Test
  public void t074_verifyUpdatedSequence() throws Exception {
    // Wait for button
    clickButton("ButRst");

    // Suggest
    suggest("CrtKeyNam", "test", "test", false);

    // Wait for button
    searchAndWait();

    // Check contents
    checkRowContents("testKey");

    // Check contents
    checkRowContents("1");

    // Check contents
    checkRowContents("No");
  }

  @Test
  public void t075_deleteSequence() throws Exception {
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

  @Test
  public void t076_verifyDeletedSequence() throws Exception {
    verifyDeleted("test", "tools", "sequences");
  }

}
