package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(Alphanumeric.class)
public class CriteriaAndMatrixTestsIT extends SeleniumUtilities {

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
   * Select test module on select criterion
   */
  @Test
  public void t002_selectTestModule() {
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
   * Test criteria: initialization
   */
  @Test
  public void t010_criteriaTest() {
    // Title
    setTestTitle("Test criteria: Initialization");

    // Go to screen
    gotoScreen("test", "criteria", "criteria-test");

    // Wait for button
    waitForButton("ButRst");

    // Wait for value
    checkCriterionContents("Tar", "checkbox off");

    // Check selector
    checkSelectContents("Sug", "test (Manager)");

    // Click button
    clickButton("ButRst");

    // Check selector
    checkSelectContents("Sug", "");
  }

  /**
   * Test criteria: text criteria
   */
  @Test
  public void t011_criteriaTestText() {
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
   */
  @Test
  public void t012_criteriaTestNumeric() {
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
   */
  @Test
  public void t013_criteriaTestDate() {
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
   */
  @Test
  public void t014_criteriaTestSuggestSelect() {
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
   */
  @Test
  public void t015_criteriaTestTextarea() {
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
   */
  @Test
  public void t016_criteriaTestSelectSuggestMultiple() {

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
   */
  @Test
  public void t017_criteriaTestCheckboxRadio() {

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
    clickCheckbox("RadBox4");

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
   */
  @Test
  public void t018_criteriaTestDependencies() {

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
    checkCheckboxRadio(true, "ChkBoxVa5", "RadBox1", "ChkBoxVa22", "ChkBoxVa24", "RadBox22");

    // Check not checked
    checkCheckboxRadio(false, "ChkBoxVa1", "ChkBoxVa2", "RadBox4", "RadBox25");
  }

  /**
   * Criteria reset test
   */
  @Test
  public void t020_criteriaReset() {
    // Title
    setTestTitle("Criteria reset");

    // Go to screen
    gotoScreen("test", "criteria", "criteria-reset");

    // Wait for button
    waitForButton("ButRst");

    // Write text
    writeText("CrtTst", "test");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");

    // Click button
    clickButton("ButRstTar");

    // Wait for button
    waitForButton("ButRst");

    // Check text
    checkCriterionContents("CrtTst", "1");

    // Click button
    clickButton("ButRst");

    // Wait for button
    waitForButton("ButTxt");

    // Check text
    checkCriterionContents("CrtTst", "xml");

    // Click button
    clickButton("ButTxt");

    // Wait for button
    waitForButton("ButRstSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "otra cosa");

    // Click button
    clickButton("ButRstSpe");

    // Wait for button
    waitForButton("ButTxt");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");

    // Click button
    clickButton("ButTxt");

    // Wait for button
    waitForButton("ButRstTarSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "otra cosa");

    // Click button
    clickButton("ButRstTarSpe");

    // Wait for button
    waitForButton("ButRst");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");

    // Click button
    clickButton("ButRst");

    // Wait for button
    waitForButton("ButTxt");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "");

    // Check text
    checkCriterionContents("CrtTstHid", "RstTst");

    // Click button
    clickButton("ButTxt");

    // Wait for button
    waitForButton("ButRstSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "otra cosa");

    // Click button
    clickButton("ButRstSpe");

    // Wait for button
    waitForButton("ButRstSpe");

    // Check text
    checkCriterionContents("CrtTstTxtHid", "RstTst");
  }

  /**
   * Test buttons: dependencies
   */
  @Test
  public void t030_buttonTest() {
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
   */
  @Test
  public void t031_buttonTestActions() {
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
   */
  @Test
  public void t041_gridTestBase() {
    // Title
    setTestTitle("Grid test: base");

    // Go to screen
    gotoScreen("test", "matrix", "matrix-test");

    // Wait for button
    waitForButton("ButPrn");

    // Click row contents
    clickRowContents("GrdSta", "awedb1");

    // Wait for button
    waitForButton("ButPrn");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_MULTISELECT");

    // Verify that button is present
    checkPresence("[grid-id='GrdMus']");

    // Verify that button is not visible
    checkNotVisible("[grid-id='GrdMus']");
  }

  /**
   * Grid test: base grid context menu
   */
  @Test
  public void t042_gridTestBaseContextMenu() {
    // Title
    setTestTitle("Grid test: base grid with context menu");

    // Wait for button
    waitForButton("ButPrn");

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
   */
  @Test
  public void t051_gridTestMultiselect() {
    // Title
    setTestTitle("Grid test: Multiselect");

    // Wait for button
    waitForButton("ButPrn");

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
   */
  @Test
  public void t061_gridTestEditable() {
    // Title
    setTestTitle("Grid test: Editable");

    // Wait for button
    waitForButton("ButPrn");

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
   */
  @Test
  public void t071_gridTestMultiOperation() {
    // Title
    setTestTitle("Grid test: Multioperation");

    // Wait for button
    waitForButton("ButPrn");

    // Click on tab
    clickTab("TabSelMat", "ENUM_MATRIX_MULTIOPTION");

    // Click on button
    clickButton("ButGrdMuoAdd");

    // Wait for visible
    waitForCssSelector("[grid-id='GrdMuo'] [column-id='RowIco'] span.fa.fa-plus");

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
   */
  @Test
  public void t081_gridTestTree() {
    // Title
    setTestTitle("Grid test: Tree grid");

    // Manage grid
    manageTreeGrid("ENUM_MATRIX_TREEGRID", "TreGrd");
  }

  /**
   * Grid test: Editable tree grid
   */
  @Test
  public void t082_gridTestTreeEditable() {
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
   */
  @Test
  public void t083_gridTestLoadingTree() {
    // Title
    setTestTitle("Grid test: Loading tree grid");

    // Manage loading tree grid
    manageLoadingTreeGrid("ENUM_MATRIX_TREEGRID_LOADNODE", "TreGrdLoa");
  }

  /**
   * Grid test: Loading tree grid
   */
  @Test
  public void t084_gridTestEditableLoadingTree() {
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
   * Open and close some tree grid leafs
   *
   * @param gridTab Tab where the grid is
   * @param gridId  Grid identifier
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
    checkNotVisible("[tree-grid-id='" + gridId + "'] [row-id='ProAdministrator-ModBase'] i.tree-icon");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator-ModBase");

    // Click on button
    clickTreeButton(gridId, "ProAdministrator-ModBase");

    // Click on button
    clickTreeButton(gridId, "ProGeneral");

    // Check visible
    checkVisible("[tree-grid-id='" + gridId + "'] [row-id='ProGeneral-ModBase'] i.tree-icon");
  }

  /**
   * Open and close some tree grid leafs on a loading treegrid
   *
   * @param gridTab Tab where the grid is
   * @param gridId  Grid identifier
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
