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
   * Test screen configuration usage (hide ButPrn button on databases screen)
   * @throws Exception Error on test
   */
  @Test
  public void t001_screenConfigurationUsage() throws Exception {
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
   * @throws Exception Error on test
   */
  @Test
  public void t002_selectTestModule() throws Exception {
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
   * @throws Exception Error on test
   */
  @Test
  public void t003_screenModulesUsage() throws Exception {
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
   * Test criteria: initialization
   * @throws Exception Error on test
   */
  @Test
  public void t010_criteriaTest() throws Exception {
    // Title
    setTestTitle("Test criteria: Initialization");

    // Go to screen
    gotoScreen("test", "criteria", "criteria-test");

    // Wait for value
    checkCriterionContents("Tar", "checkbox off");

    // Wait for button
    waitForButton("ButRst");

    // Check selector
    checkSelectContents("Sug", "test (Manager)");

    // Click button
    clickButton("ButRst");

    // Check selector
    checkSelectContents("Sug", "");
  }

  /**
   * Test criteria: text criteria
   * @throws Exception Error on test
   */
  @Test
  public void t011_criteriaTestText() throws Exception {
    // Title
    setTestTitle("Test criteria: Text criteria");

    // Write text on criterion
    writeText("Txt", "Texto Normal");

    // Check text on criterion
    checkText("label[for=Unt]", "Texto Normal");

    // Write text on criterion
    writeText("TxtReq", "Text Required");

  }

  /**
   * Test criteria: numeric criteria
   * @throws Exception Error on test
   */
  @Test
  public void t012_criteriaTestNumeric() throws Exception {
    // Title
    setTestTitle("Test criteria: Numeric criteria");

    // Write text on numeric
    writeText("Num", "10000");

    // Check text on criterion
    checkText("label[for=Unt]", "Numeric");

    // Write text on numeric
    writeText("NumReq", "-20000");

  }

  /**
   * Test criteria: date and time criteria
   * @throws Exception Error on test
   */
  @Test
  public void t013_criteriaTestDate() throws Exception {
    // Title
    setTestTitle("Test criteria: Date and time criteria");

    // Select a date
    selectDate("Cal", "23/10/1978");

    // Select a date
    selectDate("CalReq", "23/10/1978");

    // Write hour
    writeText("Tim", "12:23:41");

    // Write hour
    writeText("TimReq", "12:23:41");

    // Click on date
    clickDate("FilCal");

    // Click on selector
    click(".datepicker td.day:not(.disabled)");

    // Click on date
    clickDate("FilCalReq");

    // Click on selector
    click(".datepicker td.day:not(.disabled)");

    // Check date contents
    checkCriterionContents("Cal", "23/10/1978");

    // Check time contents
    checkCriterionContents("TimReq", "12:23:41");
  }

  /**
   * Test criteria: Suggest and select criteria
   * @throws Exception Error on test
   */
  @Test
  public void t014_criteriaTestSuggestSelect() throws Exception {
    // Title
    setTestTitle("Test criteria: Suggest and select criteria");

    // Select on selector
    suggest("Sug", "test", "test");

    // Wait for loader
    waitForLoadingBar();

    // Select on selector
    selectContain("SelDep", "Yes");

    // Wait for loader
    waitForLoadingBar();

    // Pause
    pause(250);

    // Select on selector
    selectContain("SelDepDep", "Yes");

    // Select on selector
    suggest("SugReq", "a", "a");

    // Select on selector
    selectContain("Sel", "No");

    // Select on selector
    suggest("SugNum", "1", "1");

    // Select on selector
    selectContain("Sel", "Yes");

    // Select on selector
    selectContain("SelReq", "Administrator");

    // Check select
    checkSelectContents("SelReq", "Administrator");
  }

  /**
   * Test criteria: textarea criteria
   * @throws Exception Error on test
   */
  @Test
  public void t015_criteriaTestTextarea() throws Exception {
    // Title
    setTestTitle("Test criteria: Textarea criteria");

    // Write text
    writeText("Tar", "Area de Texto");

    // Write text
    writeText("TarReq", "Area de Texto");

    // Check text
    checkCriterionContents("Tar", "Area de Texto");

    // Check text
    checkCriterionContents("TarReq", "Area de Texto");
  }

  /**
   * Test criteria: suggest multiple criteria
   * @throws Exception Error on test
   */
  @Test
  public void t016_criteriaTestSelectSuggestMultiple() throws Exception {

    // Title
    setTestTitle("Test criteria: Select and suggest multiple");

    // Select on selector
    suggestMultiple("SelMul", "e", "e");

    // Verify text
    checkMultipleSelectorContents("SelMul", "test (test@test.com)");

    // Select on selector
    suggestMultiple("SelMulReq", "e", "e");

    // Verify text
    checkMultipleSelectorContents("SelMulReq", "General");

    // Select on selector
    suggestMultiple("SugMul", "e", "e");

    // Verify text
    checkMultipleSelectorContents("SugMul", "test (test@test.com)");

    // Select on selector
    suggestMultiple("SugMulReq", "e", "e");

    // Verify text
    checkMultipleSelectorContents("SugMulReq", "test (test@test.com)");
  }

  /**
   * Test criteria: Checkbox and radio
   * @throws Exception Error on test
   */
  @Test
  public void t017_criteriaTestCheckboxRadio() throws Exception {

    // Title
    setTestTitle("Test criteria: Checkbox and radio");

    // Click checkbox
    clickCheckbox("ChkBoxVa1");

    // Check text on criterion
    checkText("label[for=Unt]", "Inf");

    // Click checkbox
    clickCheckbox("ChkBoxVa2");

    // Check text on criterion
    checkText("[criterion-id='Unt'] .unit", "Inf");

    // Click checkbox
    clickCheckbox("ChkBoxVa5");

    // Click checkbox
    clickCheckbox("RadBox1");

    // Check text on criterion
    checkText("[criterion-id='Unt'] .unit", "EUR");

    // Click checkbox
    clickCheckbox("RadBox3");

    // Check text on criterion
    checkText("[criterion-id='Unt'] .unit", "USD");

    // Click checkbox
    clickCheckbox("RadBox4");

    // Click checkbox
    clickCheckbox("RadBox25");
  }

  /**
   * Test criteria: Dependencies
   * @throws Exception Error on test
   */
  @Test
  public void t018_criteriaTestDependencies() throws Exception {

    // Title
    setTestTitle("Test criteria: Dependencies");

    // Write text
    writeText("Txt", "radios");

    // Click checkbox
    clickCheckbox("ChkBoxVa21");

    // Wait for value
    checkCriterionContents("Tar", "checkbox on");

    // Click checkbox
    clickCheckbox("ChkBoxVa21");

    // Wait for value
    checkCriterionContents("Tar", "checkbox off");

    // Click button
    clickButton("ButCnf");

    // Wait for error message
    checkVisible("div.error-container");

    // Check suggest value
    checkSelectContents("SugRea", "test (Manager)");

    // Click button
    clickButton("ButRst");

    // Check value
    checkCriterionContents("Num", "-123.456,10 EUR");

    // Check value
    checkCriterionContents("NumReq", "-123.456,10 EUR");

    // Check checked
    checkCheckboxRadio(true, "ChkBoxVa5", "RadBox5", "ChkBoxVa22", "ChkBoxVa24", "RadBox24");

    // Check not checked
    checkCheckboxRadio(false, "ChkBoxVa1", "ChkBoxVa2", "RadBox4", "RadBox25");
  }

  /**
   * Criteria reset test
   * @throws Exception Error on test
   */
  @Test
  public void t020_criteriaReset() throws Exception {
    // Title
    setTestTitle("Criteria reset");

    // Go to screen
    gotoScreen("test", "criteria", "criteria-reset");

    // Write text
    writeText("CrtTst", "test");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");

    // Click button
    clickButton("ButRstTar");

    // Check text
    checkCriterionContents("CrtTst", "1");

    // Click button
    clickButton("ButRst");

    // Check text
    checkCriterionContents("CrtTst", "xml");

    // Click button
    clickButton("ButTxt");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "otra cosa");

    // Click button
    clickButton("ButRstSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");

    // Click button
    clickButton("ButTxt");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "otra cosa");

    // Click button
    clickButton("ButRstTarSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");

    // Click button
    clickButton("ButRst");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "");

    // Check text
    checkCriterionContents("CrtTstHid", "RstTst");

    // Click button
    clickButton("ButTxt");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "otra cosa");

    // Click button
    clickButton("ButRstSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");
  }

  /**
   * Test buttons: dependencies
   * @throws Exception Error on test
   */
  @Test
  public void t030_buttonTest() throws Exception {
    // Title
    setTestTitle("Button test: dependencies");

    // Go to screen
    gotoScreen("test", "button-test");

    // Wait for button
    waitForButton("ButTxt");

    // Wait for button
    waitForButton("ButIco");

    // Verify that button is not visible
    checkNotVisible("#ButCnfTs1");

    // Verify that button is not visible
    checkNotVisible("#ButCnfTs2");

    // Select on selector
    selectContain("ButSel", "No");

    // Wait for button
    waitForButton("ButTxt");

    // Wait for button
    waitForButton("ButIco");

    // Verify that button is disabled
    checkVisible("#ButCnfTs1[disabled]");

    // Wait for button
    waitForButton("ButCnfTs2");

    // Select on selector
    selectContain("ButSel", "Yes");

    // Wait for button
    waitForButton("ButTxt");

    // Wait for button
    waitForButton("ButIco");

    // Wait for button
    waitForButton("ButCnfTs1");

    // Verify that button is not visible
    checkNotVisible("#ButCnfTs2");
  }

  /**
   * Test buttons: actions
   * @throws Exception Error on test
   */
  @Test
  public void t031_buttonTestActions() throws Exception {
    // Title
    setTestTitle("Button test: actions");

    // Write text
    writeText("ButVal", "");

    // Click on button
    clickButton("ButSetVa1");

    // Check text
    checkCriterionContents("ButVal", "Valor1");

    // Click on button
    clickButton("ButSetVa2");

    // Check text
    checkCriterionContents("ButVal", "Valor2");

    // Click on button
    clickButton("ButSetVa3");

    // Check text
    checkCriterionContents("ButVal", "Valor3");
  }

  /**
   * Grid test: base grid
   * @throws Exception Error on test
   */
  @Test
  public void t041_gridTestBase() throws Exception {
    // Title
    setTestTitle("Grid test: base");

    // Go to screen
    gotoScreen("test", "matrix", "matrix-test");

    // Click row contents
    clickRowContents("GrdSta", "awedb1");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_MULTISELECT");

    // Verify that button is present
    checkPresence("[grid-id='GrdMus']");

    // Verify that button is not visible
    checkNotVisible("[grid-id='GrdMus']");
  }

  /**
   * Grid test: base grid context menu
   * @throws Exception Error on test
   */
  @Test
  public void t042_gridTestBaseContextMenu() throws Exception {
    // Title
    setTestTitle("Grid test: base grid with context menu");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_STATIC");

    // Context menu on grid
    contextMenuRowContents("GrdSta", "awedb1");

    // Wait for button
    waitForContextButton("CtxGrdStaAdd");

    // Click on component mask
    click("div.component-mask");

    // Wait for context menu to hide
    checkNotVisible(".context-menu");

    // Context menu on grid
    contextMenuRowContents("GrdSta", "awedb2");

    // Wait for button
    waitForContextButton("CtxGrdStaDel");

    // Click on component mask
    click("div.component-mask");

    // Wait for context menu to hide
    checkNotVisible(".context-menu");

    // Click on viewport
    click("[grid-id='GrdSta'] div.ui-grid-viewport");

    // Click row contents
    clickRowContents("GrdSta", "awedb2");
  }

  /**
   * Grid test: multiselect grid
   * @throws Exception Error on test
   */
  @Test
  public void t051_gridTestMultiselect() throws Exception {
    // Title
    setTestTitle("Grid test: Multiselect");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_MULTISELECT");

    // Click on grid
    clickRowContents("GrdMus", "AWE DB 2");

    // Click on grid
    clickRowContents("GrdMus", "AWE DB 1");

    // Click on grid
    clickRowContents("GrdMus", "AWE DB 3");

    // Check row contents
    checkRowContentsGrid("GrdMus", "awedb2");
  }

  /**
   * Grid test: Editable grid
   * @throws Exception Error on test
   */
  @Test
  public void t061_gridTestEditable() throws Exception {
    // Title
    setTestTitle("Grid test: Editable");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_EDITABLE");

    // Click on grid
    clickRowContents("GrdEdi", "adminflare");

    // Click on date
    clickDate("GrdEdi", "FilDat");

    // Click on selector
    click(".datepicker td.day:not(.disabled)");

    // Get selector text
    String date = getText("GrdEdi", "FilDat");

    // Save row
    saveRow("GrdEdi");

    // Check date on second row
    checkCellContents("GrdEdi", "2", "Dat", date);

    // Click on grid
    clickRowContents("GrdEdi", "asphalt");

    // Click on grid
    clickRowContents("GrdEdi", "clean");

    // Click on date
    selectDate("GrdEdi", "Dat", "23/10/1978");

    // Get date
    date = getText("GrdEdi", "Dat");

    // Save row
    saveRow("GrdEdi");

    // Check date on second row
    checkCellContents("GrdEdi", "2", "FilDat", date);

    // Context menu
    contextMenuRowContents("GrdEdi", "asphalt");

    // Wait for context button
    clickContextButton("CtxGrdEdiAddSel", "CtxGrdEdiAddUpp");

    // Save row
    saveRow("GrdEdi");

    // Click on grid
    checkRowContentsGrid("GrdEdi", "-123.456,10 $");

    // Check cell contents
    checkCellContents("GrdEdi", "1", "Txt", "adminflare");

    // Check cell contents
    checkCellContents("GrdEdi", "2", "Txt", "asphalt");
  }

  /**
   * Grid test: Multioperation grid
   * @throws Exception Error on test
   */
  @Test
  public void t071_gridTestMultiOperation() throws Exception {
    // Title
    setTestTitle("Grid test: Multioperation");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_MULTIOPTION");

    // Click on button
    clickButton("ButGrdMuoAdd");

    // Save row
    saveRow("GrdMuo");

    // Check icon
    checkVisible("[grid-id='GrdMuo'] [column-id='RowIco'] span.fa.fa-plus");

    // Click on a cell
    clickCell("GrdMuo", "1", "Des2");

    // Write on text
    writeText("GrdMuo", "Des2", "asdasda");

    // Save row
    saveRow("GrdMuo");

    // Check icon
    checkVisible("[grid-id='GrdMuo'] [column-id='RowIco'] span.fa.fa-edit");

    // Context menu on grid
    contextMenu("GrdMuo", "3", "Des2");

    // Click on context button
    clickContextButton("CtxGrdMuoDel");

    // Check icon
    checkVisible("[grid-id='GrdMuo'] [column-id='RowIco'] span.fa.fa-trash");
  }

  /**
   * Grid test: Tree grid
   * @throws Exception Error on test
   */
  @Test
  public void t081_gridTestTree() throws Exception {
    // Title
    setTestTitle("Grid test: Tree grid");

    // Manage grid
    manageTreeGrid("ENUM_MATRIX_TREEGRID", "TreGrd");
  }

  /**
   * Grid test: Editable tree grid
   * @throws Exception Error on test
   */
  @Test
  public void t082_gridTestTreeEditable() throws Exception {
    // Title
    setTestTitle("Grid test: Editable tree grid");

    // Manage grid
    manageTreeGrid("ENUM_MATRIX_TREEGRID_EDITABLE", "TreGrdEdi");

    // Context menu
    contextMenu("TreGrdEdi", "ProGeneral-ModBase", "TreGrdEdi_Nam");

    // Select context menu option
    clickContextButton("CtxTreGrdEdiAddSel", "CtxTreGrdEdiAddChl");

    // Pause
    pause(500);

    // Click on a cell
    clickCell("TreGrdEdi", "ProGeneral-ModBase", "TreGrdEdi_Nam");

    // Click on a cell
    clickCell("TreGrdEdi", "new-row-0", "TreGrdEdi_Nam");

    // Click on a cell
    clickCell("TreGrdEdi", "ProGeneral-ModBase", "TreGrdEdi_Nam");

    // Click on a cell
    clickCell("TreGrdEdi", "new-row-0", "TreGrdEdi_Nam");

    // Save row
    saveRow("TreGrdEdi");

    // Click on button
    clickTreeButton("TreGrdEdi", "ProOperator");

    // Context menu
    contextMenu("TreGrdEdi", "ProOperator", "TreGrdEdi_Nam");

    // Select context menu option
    clickContextButton("CtxTreGrdEdiDel");

    // Check not visible
    checkNotVisible("[tree-grid-id='TreGrdEdi'] [row-id='ProOperator']");
  }

  /**
   * Grid test: Loading tree grid
   * @throws Exception Error on test
   */
  @Test
  public void t083_gridTestLoadingTree() throws Exception {
    // Title
    setTestTitle("Grid test: Loading tree grid");

    // Manage loading tree grid
    manageLoadingTreeGrid("ENUM_MATRIX_TREEGRID_LOADNODE", "TreGrdLoa");
  }

  /**
   * Grid test: Loading tree grid
   * @throws Exception Error on test
   */
  @Test
  public void t084_gridTestEditableLoadingTree() throws Exception {
    // Title
    setTestTitle("Grid test: Loading editable tree grid");

    // Manage loading tree grid
    manageLoadingTreeGrid("ENUM_MATRIX_TREEGRID_EDITABLE_LOADNODE", "TreGrdLoaEdi");

    // Context menu
    contextMenu("TreGrdLoaEdi", "ProGeneral-ModBase", "TreGrdLoaEdi_Nam");

    // Select context menu option
    clickContextButton("CtxTreGrdLoaEdiAddSel", "CtxTreGrdLoaEdiAddChl");

    // Check new row visible
    checkVisible("[tree-grid-id='TreGrdLoaEdi'] [row-id='new-row-0']");

    // Pause
    pause(250);

    // Save row
    saveRow("TreGrdLoaEdi");

    // Click on button
    clickTreeButton("TreGrdLoaEdi", "ProOperator");

    // Context menu
    contextMenu("TreGrdLoaEdi", "ProOperator", "TreGrdLoaEdi_Nam");

    // Select context menu option
    clickContextButton("CtxTreGrdLoaEdiDel");

    // Check visible
    checkVisible("[tree-grid-id='TreGrdLoaEdi'] .DELETE [row-id='ProOperator']");
  }

  /**
   * Grid test: Having query
   * @throws Exception Error on test
   */
  @Test
  public void t090_gridTestHaving() throws Exception {
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
   * @throws Exception Error on test
   */
  @Test
  public void t100_chartTest() throws Exception {
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
   * @throws Exception Error on test
   */
  @Test
  public void t110_wizardTest() throws Exception {
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
   * @throws Exception Error on test
   */
  @Test
  public void t120_sqlExtractorEngine() throws Exception {
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
   * @throws Exception Error on test
   */
  @Test
  public void t130_fileManager() throws Exception {
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

  /**
   * Open and close some tree grid leafs
   * @param gridTab Tab where the grid is
   * @param gridId Grid identifier
   */
  private void manageTreeGrid(String gridTab, String gridId) {
    // Click on tab
    clickTab("TabSelMat", gridTab);

    // Click on button
    clickTreeButton(gridId, "ProAdministrator");

    // Click on button
    clickTreeButton(gridId, "ProGeneral");

    // Click on button
    clickTreeButton(gridId, "ProOperator");

    // Check not visible
    checkNotVisible("[tree-grid-id='"+ gridId +"'] [row-id='ProAdministrator-ModBase'] i.tree-icon");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator-ModBase");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator-ModBase");

    // Click on button
    clickTreeButton(gridId, "ProGeneral");

    // Check visible
    checkVisible("[tree-grid-id='"+ gridId +"'] [row-id='ProGeneral-ModBase'] i.tree-icon");
  }

  /**
   * Open and close some tree grid leafs on a loading treegrid
   * @param gridTab Tab where the grid is
   * @param gridId Grid identifier
   */
  private void manageLoadingTreeGrid(String gridTab, String gridId) {
    // Click on tab
    clickTab("TabSelMat", gridTab);

    // Click on button
    clickTreeButton(gridId, "ProAdministrator");

    // Click on button
    clickTreeButton(gridId, "ProGeneral");

    // Click on button
    clickTreeButton(gridId, "ProOperator");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator-ModBase");

    // Click on button
    clickTreeButton(gridId, "ProGeneral-ModBase");

    // Check visible
    checkVisible("[tree-grid-id='" + gridId + "'] [row-id='ProOperator-ModBase']");
  }
}
