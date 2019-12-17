package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Calendar;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SchedulerTestsIT extends SeleniumUtilities {

  /**
   * Log into the application
   *
   * @throws Exception
   */
  @Test
  public void t000_loginTest() throws Exception {
    checkLogin("test", "test", "#ButUsrAct span.info-text", "Manager (test)");
  }

  /**
   * Log out from the application
   *
   * @throws Exception
   */
  @Test
  public void t999_logoutTest() throws Exception {
    checkLogout(".slogan", "Almis Web Engine");
  }

  /**
   * Go to a screen to add a new option
   *
   * @param options
   */
  private void addNew(String checkButton, String... options) {
    // Go to screen
    gotoScreen(options);

    // Click on new button
    clickButton("ButNew", true);

    // Wait for button
    waitForButton(checkButton);
  }

  /**
   * Check on list the new values
   */
  private void verifyNew(String suggest, String search) {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(suggest, search, search);

    // Search on grid
    searchAndWait();

    // Click row
    checkRowContents(search);
  }

  /**
   * Check on list the new values
   */
  private void verifyUpdate(String suggest, String search, String... checkContents) {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(suggest, search, search);

    // Search on grid
    searchAndWait();

    // Check row contents
    checkRowContents(checkContents);
  }

  /**
   * Go to a screen to update the option
   *
   * @param criterion   Criterion to search option
   * @param search      Search string
   * @param checkButton Check button to verify update screen loaded
   * @param options     Option navigation
   */
  private void update(String criterion, String search, String checkButton, String... options) {
    // Go to screen
    gotoScreen(options);

    // Update option
    update(criterion, search, checkButton);
  }

  /**
   * Update an option
   *
   * @param criterion   Criterion to search option
   * @param search      Search string
   * @param checkButton Check button to verify update screen loaded
   */
  private void update(String criterion, String search, String checkButton) {
    // Wait for button
    clickButton("ButRst");

    // Suggest on column selector
    suggest(criterion, search, search);

    // Search on grid
    searchAndWait();

    // Click row
    clickRowContents(search);

    // Click on new button
    clickButton("ButUpd", true);

    // Wait for button
    waitForButton(checkButton);
  }

  /**
   * Delete from a screen
   *
   * @param criterion Criterion to search
   * @param search    Search text
   */
  private void delete(String criterion, String search) {
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
   * Delete from a screen
   *
   * @param criterion Criterion to search
   * @param search    Search text
   * @param options   Screen options
   */
  private void delete(String criterion, String search, String... options) {
    // Go to screen
    gotoScreen(options);

    // Delete
    delete(criterion, search);
  }

  /**
   * Change task data
   *
   * @param title       Task title
   * @param description Task description
   */
  private void changeTaskData(String title, String description) {
    // Insert text
    writeText("Nam", title);

    // Insert text
    writeText("Des", description);
  }

  /**
   * Change task launch type
   *
   * @param number Repeat number
   * @param type   Repeat type
   */
  private void changeTaskLaunch(Integer number, String type) {
    clickTab("update-scheduler-task", "ENUM_TASK_STEP_LAUNCH");

    // Insert text
    writeText("RptNum", number.toString());

    // Suggest on selector
    suggest("RptTyp", type, type);

    // Calculate fire times
    clickButton("ButUpdateFireTimes", true);
  }

  /**
   * Verify deleted
   *
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
   * Generate a new scheduler server
   *
   * @throws Exception Error on test
   */
  @Test
  public void t001_newSchedulerServer() throws Exception {
    // Title
    setTestTitle("Generate a new scheduler server");

    // New server
    addNew("ButCnf", "scheduler", "scheduler-servers");

    // Insert text
    writeText("Nom", "Test Server");

    // Insert text
    writeText("Hst", "127.0.0.1");

    // Insert text
    writeText("Prt", "21212");

    // Suggest on selector
    suggest("Pro", "ftp", "ftp");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyNew("CrtSrv", "Test Server");
  }

  /**
   * Generate a new scheduler calendar
   *
   * @throws Exception Error on test
   */
  @Test
  public void t002_newSchedulerCalendar() throws Exception {
    // Title
    setTestTitle("Generate a new scheduler calendar");

    // New server
    addNew("ButCnf", "scheduler", "scheduler-calendars");

    // Insert text
    writeText("Nom", "Test Calendar");

    // Insert text
    writeText("Des", "Test Calendar Description");

    // Suggest on selector
    suggest("Act", "Yes", "Yes");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyNew("CrtCal", "Test Calendar");
  }

  /**
   * Generate a new manual task
   *
   * @throws Exception Error on test
   */
  @Test
  public void t003_newManualTask() throws Exception {
    // Title
    setTestTitle("Generate a new manual task");

    // New task
    addNew("ButHlp", "scheduler", "scheduler-tasks");

    // FILL TASK DATA
    changeTaskData("Test manual task", "Test manual task description");

    // Suggest on selector
    suggest("TypExe", "Maintain", "Maintain");

    // Suggest on selector
    suggest("maintain", "waitSomeSeconds", "waitSomeSeconds");

    // Check
    clickCheckbox("LchDepWrn");

    // Check
    clickCheckbox("LchDepErr");

    // Check
    clickCheckbox("LchSetWrn");

    // Click on next step
    clickButton("FwStep1");

    // FILL PARAMETERS

    // Click on row
    clickRowContents("ParameterList", "secondsToWait");

    // Insert text
    writeText("ParameterList", "ParVal", "45");

    // Save row
    saveRow("ParameterList");

    // Click on next step
    clickButton("FwStep2");

    // FILL SCHEDULE

    // Click on next step
    clickButton("FwStep3");

    // FILL DEPENDENCIES

    // Click on next step
    clickButton("FwStep4");

    // FILL REPORT

    // Suggest on selector
    suggest("RepTyp", "Broadcast", "Broadcast");

    // Suggest on selector
    suggestMultipleList("RepSndSta", "Warning", "Error");

    // Suggest on selector
    suggestMultiple("RepUsrDst", "test", "test");

    // Insert text
    writeText("RepMsg", "Test broadcast message");

    // Store and confirm
    clickButtonAndConfirm("Finish");

    // Check element
    verifyNew("CrtTsk", "Test manual task");
  }

  /**
   * Generate a new scheduled task
   *
   * @throws Exception Error on test
   */
  @Test
  public void t004_newScheduledTask() throws Exception {
    // Title
    setTestTitle("Generate a new scheduled task");

    // New task
    addNew("ButHlp", "scheduler", "scheduler-tasks");

    // FILL TASK DATA
    changeTaskData("Test scheduled task", "Test scheduled task description");

    // Insert text
    writeText("NumStoExe", "2");

    // Insert text
    writeText("TimOutExe", "59");

    // Suggest on selector
    suggest("TypExe", "Maintain", "Maintain");

    // Suggest on selector
    suggest("maintain", "waitSomeSeconds", "waitSomeSeconds");

    // Check
    clickCheckbox("LchDepWrn");

    // Check
    clickCheckbox("LchDepErr");

    // Check
    clickCheckbox("LchSetWrn");

    // Click on next step
    clickButton("FwStep1");

    // FILL PARAMETERS

    // Click on row
    clickRowContents("ParameterList", "secondsToWait");

    // Insert text
    writeText("ParameterList", "ParVal", "3");

    // Save row
    saveRow("ParameterList");

    // Click on next step
    clickButton("FwStep2");

    // FILL SCHEDULE

    // Suggest on selector
    suggest("TypLch", "Scheduled", "Scheduled");

    // Suggest on selector
    suggest("RptTyp", "Seconds", "Seconds");

    // Insert text
    writeText("RptNum", "1200");

    // Calculate fire times
    clickButton("ButUpdateFireTimes", true);

    // Click on next step
    clickButton("FwStep3");

    // FILL DEPENDENCIES

    // Click on next step
    clickButton("FwStep4");

    // FILL REPORT

    // Suggest on selector
    suggest("RepTyp", "Broadcast", "Broadcast");

    // Suggest on selector
    suggestMultipleList("RepSndSta", "Warning", "Stopped");

    // Suggest on selector
    suggestMultiple("RepUsrDst", "test", "test");

    // Insert text
    writeText("RepMsg", "Test broadcast message");

    // Store and confirm
    clickButtonAndConfirm("Finish");

    // Check element
    verifyNew("CrtTsk", "Test scheduled task");
  }

  /**
   * Generate a new file triggered task
   *
   * @throws Exception Error on test
   */
  @Test
  public void t005_newFileTask() throws Exception {
    // Title
    setTestTitle("Generate a new file triggered task");

    // New task
    addNew("ButHlp", "scheduler", "scheduler-tasks");

    // FILL TASK DATA
    changeTaskData("Test file task", "Test file task description");

    // Insert text
    writeText("NumStoExe", "15");

    // Insert text
    writeText("TimOutExe", "60");

    // Suggest on selector
    suggest("Act", "No", "No");

    // Suggest on selector
    suggest("TypExe", "Maintain", "Maintain");

    // Suggest on selector
    suggest("maintain", "waitSomeSeconds", "waitSomeSeconds");

    // Check
    clickCheckbox("LchDepWrn");

    // Check
    clickCheckbox("LchDepErr");

    // Check
    clickCheckbox("LchSetWrn");

    // Click on next step
    clickButton("FwStep1");

    // FILL PARAMETERS

    // Click on row
    clickRowContents("ParameterList", "secondsToWait");

    // Insert text
    writeText("ParameterList", "ParVal", "45");

    // Save row
    saveRow("ParameterList");

    // Click on next step
    clickButton("FwStep2");

    // FILL SCHEDULE

    // Suggest on selector
    suggest("TypLch", "File", "File");

    // Suggest on selector
    suggest("RptTyp", "Hours", "Hours");

    // Insert text
    writeText("RptNum", "1");

    // Suggest on selector
    suggest("LchSrv", "Test Server", "Test Server");

    // Insert text
    writeText("LchPth", "/test");

    // Insert text
    writeText("LchPat", ".*\\.txt");

    // Click on next step
    clickButton("FwStep3");

    // FILL DEPENDENCIES

    // Click on next step
    clickButton("FwStep4");

    // FILL REPORT

    // Suggest on selector
    suggest("RepTyp", "Broadcast", "Broadcast");

    // Suggest on selector
    suggestMultipleList("RepSndSta", "Error", "Stopped");

    // Suggest on selector
    suggestMultiple("RepUsrDst", "test", "test");

    // Insert text
    writeText("RepMsg", "Test broadcast message");

    // Store and confirm
    clickButtonAndConfirm("Finish");

    // Check element
    verifyNew("CrtTsk", "Test file task");
  }

  /**
   * Update a scheduler server
   *
   * @throws Exception Error on test
   */
  @Test
  public void t011_updateSchedulerServer() throws Exception {
    // Title
    setTestTitle("Update a scheduler server");

    // Go to server to update
    update("CrtSrv", "Test Server", "ButCnf", "scheduler", "scheduler-servers");

    // Insert text
    writeText("Nom", "Test Server updated");

    // Suggest on selector
    suggest("Pro", "Folder", "Folder");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyNew("CrtSrv", "Test Server updated");
  }

  /**
   * Update a scheduler calendar
   *
   * @throws Exception Error on test
   */
  @Test
  public void t012_updateSchedulerCalendar() throws Exception {
    // Title
    setTestTitle("Update a scheduler calendar");

    // New server
    update("CrtCal", "Test Calendar","ButCnf", "scheduler", "scheduler-calendars");

    // Insert text
    writeText("Nom", "Test Calendar updated");

    // Insert text
    writeText("Des", "Test Calendar Description updated");

    // Suggest on selector
    suggest("Act", "Yes", "Yes");

    // Add dates
    clickButton("ButDatAdd");

    // Select date
    selectDay("GrdDatLst", "Dat", 1);

    // Write name
    writeText("GrdDatLst", "Nam", "First day of month");

    // Add dates
    clickButton("ButDatAdd");

    // Select date
    selectDay("GrdDatLst", "Dat", 3);

    // Write name
    writeText("GrdDatLst", "Nam", "Third day of month");

    // Save row
    saveRow();

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyNew("CrtCal", "Test Calendar updated");
  }

  /**
   * Update the manual task
   *
   * @throws Exception Error on test
   */
  @Test
  public void t021_updateManualTask() throws Exception {
    // Title
    setTestTitle("Update the manual task");

    // New task
    update("CrtTsk", "Test manual", "ButHlp", "scheduler", "scheduler-tasks");

    // UPDATE TASK DATA
    changeTaskData("Test manual task updated", "Test new manual task description");

    // Insert text
    writeText("NumStoExe", "60");

    // Insert text
    writeText("TimOutExe", "120");

    // Check
    clickCheckbox("LchDepWrn");

    // Check
    clickCheckbox("LchDepErr");

    // Check
    clickCheckbox("LchSetWrn");

    // Click on tab
    clickTab("update-scheduler-task", "ENUM_TASK_STEP_PARAMS");

    // UPDATE PARAMETERS

    // Click on row
    clickRowContents("ParameterList", "secondsToWait");

    // Insert text
    writeText("ParameterList", "ParVal", "33");

    // Save row
    saveRow("ParameterList");

    // Click on tab
    clickTab("update-scheduler-task", "ENUM_TASK_STEP_REPORT");

    // UPDATE REPORT

    // Suggest on selector
    suggest("RepTyp", "E-mail", "E-mail");

    // Suggest on selector
    suggestMultiple("RepSndSta", "Ok", "Ok");

    // Suggest on selector
    suggestMultiple("RepEmaDst", "test", "test");

    // Insert text
    writeText("RepTit", "Test email title");

    // Insert text
    writeText("RepMsg", "Test email message");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test manual", "Test manual task updated");
  }

  /**
   * Update the scheduled task (minutes)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t031_updateScheduledTaskMinutes() throws Exception {
    // Title
    setTestTitle("Update the scheduled task in minutes");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (minutes)", "Test scheduled task description (minutes)");

    // UPDATE TASK LAUNCH
    changeTaskLaunch(4, "Minutes");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (minutes)");
  }

  /**
   * Update the scheduled task (hours)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t032_updateScheduledTaskHours() throws Exception {
    // Title
    setTestTitle("Update the scheduled task in hours");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (hours)", "Test scheduled task description (hours)");

    // UPDATE TASK LAUNCH
    changeTaskLaunch(6, "Hours");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (hours)");
  }

  /**
   * Update the scheduled task (days)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t033_updateScheduledTaskDays() throws Exception {
    // Title
    setTestTitle("Update the scheduled task in days");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (days)", "Test scheduled task description (days)");

    // UPDATE TASK LAUNCH
    changeTaskLaunch(8, "Days");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (days)");
  }

  /**
   * Update the scheduled task (months)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t034_updateScheduledTaskMonths() throws Exception {
    // Title
    setTestTitle("Update the scheduled task in months");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (months)", "Test scheduled task description (months)");

    // UPDATE TASK LAUNCH
    changeTaskLaunch(10, "Months");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (months)");
  }

  /**
   * Update the scheduled task (years)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t035_updateScheduledTaskYears() throws Exception {
    // Title
    setTestTitle("Update the scheduled task in years");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (years)", "Test scheduled task description (years)");

    // UPDATE TASK LAUNCH
    changeTaskLaunch(12, "Years");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (years)");
  }

  /**
   * Update the scheduled task (once)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t036_updateScheduledTaskOnce() throws Exception {
    // Title
    setTestTitle("Update the scheduled task once");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (once)", "Test scheduled task description (once)");

    // UPDATE TASK LAUNCH
    clickTab("update-scheduler-task", "ENUM_TASK_STEP_LAUNCH");

    // Suggest on selector
    suggest("RptTyp", "Once", "Once");

    // Select day
    selectDay("schExeDate", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

    // Write hour
    writeText("schExeTime", "23:59:59");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (once)");
  }

  /**
   * Update the scheduled task (custom)
   *
   * @throws Exception Error on test
   */
  @Test
  public void t037_updateScheduledTaskCustom() throws Exception {
    // Title
    setTestTitle("Update the scheduled task custom");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task (custom)", "Test scheduled task description (custom)");

    // UPDATE TASK LAUNCH
    clickTab("update-scheduler-task", "ENUM_TASK_STEP_LAUNCH");

    // Suggest on selector
    suggest("RptTyp", "Custom", "Custom");

    // Suggest on selector
    suggest("IdeCal", "Test Calendar", "Test Calendar");

    // Select years
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    suggestMultipleList("years", String.valueOf(currentYear), String.valueOf(currentYear + 1));

    // Select months
    suggestMultipleList("months", "October", "November", "December");

    // Select days
    suggestMultipleList("days", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

    // Select hours
    suggestMultipleList("hours", "00:00", "1:00", "2:00");

    // Select minutes
    suggestMultipleList("minutes", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    // Select seconds
    suggestMultipleList("seconds", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    // Select initial date
    selectDay("IniDat", getTodayDay());

    // Select end date
    selectDate("EndDat", "01/12/" + (currentYear + 5));

    // Calculate fire times
    clickButton("ButUpdateFireTimes", true);

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task (custom)");
  }

  /**
   * Update the scheduled task dependencies
   *
   * @throws Exception Error on test
   */
  @Test
  public void t038_updateScheduledTaskDependencies() throws Exception {
    // Title
    setTestTitle("Update the scheduled task dependencies");

    // New task
    update("CrtTsk", "Test scheduled", "ButHlp");

    // UPDATE TASK DATA
    changeTaskData("Test scheduled task dependencies", "Test scheduled task description dependencies");

    // UPDATE TASK DEPENDENCIES
    clickTab("update-scheduler-task", "ENUM_TASK_STEP_DEPENDENCIES");

    // Click on add button
    clickButton("ButAddDependency");

    // Suggest on grid
    suggest("DependencyList", "DepTskIde", "Test manual", "Test manual");

    // Save row
    saveRow("DependencyList");

    // Store and confirm
    clickButtonAndConfirm("ButCnf");

    // Check element
    verifyUpdate("CrtTsk", "Test scheduled", "Test scheduled task dependencies");
  }

  /**
   * Activate a task
   *
   * @throws Exception Error on test
   */
  @Test
  public void t051_activateTask() throws Exception {
    // Title
    setTestTitle("Activate task");

    // Wait for button
    clickButton("ButRst");

    // Search on grid
    searchAndWait();

    // Select the scheduled task
    clickRowContents("Test file");

    // Execute task now
    clickButton("ButAct");

    // Check success execution
    checkNotVisible("#ButAct");
    checkVisible("#ButDea");
  }

  /**
   * Run a task immediately
   *
   * @throws Exception Error on test
   */
  @Test
  public void t052_runTaskNow() throws Exception {
    // Title
    setTestTitle("Run task immediately");

    // Select the scheduled task
    clickRowContents("Test scheduled");

    // Execute task now
    clickButton("ButRun");

    // Wait 5 seconds to finish the task
    pause(5000);

    // Check success execution
    checkVisible("[column-id='ExeStaIco']:first-child span.text-success");
  }

  /**
   * Deactivate a calendar
   *
   * @throws Exception Error on test
   */
  @Test
  public void t053_deactivateCalendar() throws Exception {
    // Title
    setTestTitle("Deactivate calendar");

    // Delete the task
    gotoScreen("scheduler", "scheduler-calendars");

    // Wait for button
    clickButton("ButRst");

    // Search on grid
    searchAndWait();

    // Select the scheduled task
    clickRowContents("Test Calendar");

    // Execute task now
    clickButton("ButDea");

    // Check success execution
    checkNotVisible("#ButDea");
    checkVisible("#ButAct");
  }

  /**
   * Deactivate a calendar
   *
   * @throws Exception Error on test
   */
  @Test
  public void t054_activateCalendar() throws Exception {
    // Title
    setTestTitle("Activate calendar");

    // Wait for button
    clickButton("ButRst");

    // Search on grid
    searchAndWait();

    // Select the scheduled task
    clickRowContents("Test Calendar");

    // Execute task now
    clickButton("ButAct");

    // Check success execution
    checkNotVisible("#ButAct");
    checkVisible("#ButDea");
  }

  /**
   * Test scheduler management screen
   *
   * @throws Exception
   */
  @Test
  public void t061_testSchedulerManagementScreen() throws Exception {
    // Title
    setTestTitle("Test scheduler management screen");

    // Delete the task
    gotoScreen("scheduler", "scheduler-management");

    // Wait for button
    waitForButton("clearAndStopScheduler");

    // Click button
    clickButton("clearAndStopScheduler", true);

    // Wait for message
    checkAndCloseMessage("success");

    // Wait for button
    waitForButton("restartScheduler");

    // Click button
    clickButton("restartScheduler", true);

    // Wait for message
    checkAndCloseMessage("success");

    // Wait for button
    waitForButton("stopScheduler");

    // Click button
    clickButton("stopScheduler", true);

    // Wait for message
    checkAndCloseMessage("success");

    // Wait for button
    waitForButton("startScheduler");

    // Click button
    clickButton("startScheduler", true);

    // Wait for message
    checkAndCloseMessage("success");
  }

  /**
   * Delete the manual task
   *
   * @throws Exception
   */
  @Test
  public void t901_deleteManualTask() throws Exception {
    // Title
    setTestTitle("Delete the manual task");

    // Delete the task
    delete("CrtTsk", "Test manual", "scheduler", "scheduler-tasks");

    // Verify deleted user
    verifyDeleted("Test manual");
  }

  /**
   * Delete the scheduled task
   *
   * @throws Exception
   */
  @Test
  public void t902_deleteScheduledTask() throws Exception {
    // Title
    setTestTitle("Delete the scheduled task");

    // Delete the user
    delete("CrtTsk", "Test scheduled");

    // Verify deleted user
    verifyDeleted("Test scheduled");
  }

  /**
   * Delete the file task
   *
   * @throws Exception
   */
  @Test
  public void t903_deleteFileTask() throws Exception {
    // Title
    setTestTitle("Delete the file task");

    // Delete the user
    delete("CrtTsk", "Test file");

    // Verify deleted user
    verifyDeleted("Test file");
  }

  /**
   * Delete the scheduler calendar
   *
   * @throws Exception
   */
  @Test
  public void t911_deleteSchedulerCalendar() throws Exception {
    // Title
    setTestTitle("Delete the scheduler calendar");

    // Delete the user
    delete("CrtCal", "Test Calendar", "scheduler", "scheduler-calendars");

    // Verify deleted user
    verifyDeleted("Test Calendar");
  }

  /**
   * Delete the scheduler server
   *
   * @throws Exception
   */
  @Test
  public void t921_deleteSchedulerServer() throws Exception {
    // Title
    setTestTitle("Delete the scheduler server");

    // Delete the user
    delete("CrtSrv", "Test Server", "scheduler", "scheduler-servers");

    // Verify deleted user
    verifyDeleted("Test Server");
  }
}
