### Almis Web Engine - **[Home](../readme.md)** - **[Selenium tests](selenium-tests-guide.md)**

---

# **Selenium Tests Guide**

## Table of Contents

* **[Basic instructions](#basic-instructions)**
  * [Go to a new page](#go-to-a-new-page)
  * [Click on a button](#click-on-a-button)
  * [Wait for a grid load](#wait-for-a-grid-to-load)
  * [Check and close a message](#check-and-close-a-message)
  * [Accept a confirm dialog](#accept-a-confirm-dialog)
* **[Criteria](#criteria)**
  * [Input](#input)
  * [Textarea](#textarea)
  * [Date](#date)
     * [Pick a specific date in the datepicker](#pick-a-specific-date-in-the-datepicker)
     * [Pick a day from the current month](#pick-a-day-from-the-current-month)
     * [Pick a month in the month-selector](#pick-a-month-from-the-month-selector)
     * [Pick a year in the year-selector](#pick-a-year-from-the-year-selector)
  * [Time](#time)
  * [Select](#select)
  * [Suggest](#suggest)
  * [Select multiple](#select-multiple)
  * [Suggest multiple](#suggest-multiple)
  * [Tabs](#tabs)
     * [Check active tab](#check-active-tab)
     * [Click on a tab](#click-on-a-tab)
  * [Checkbox](#checkbox)
  * [Radio button](#radio-button)
  * [Text view](#text-view)
  * [Verify criteria values](#verify-criteria-values)
* **[Grid cells](#grid-cells)**
  * [Input column](#input-column)
  * [Textarea column](#textarea-column)
  * [Date column](#date-column)
     * [Pick a specific date in the datepicker in a column](#pick-a-specific-date-in-the-datepicker-in-a-column)
     * [Pick a day from the current month in a column](#pick-a-day-from-the-current-month-in-a-column)
  * [Time column](#time-column)
  * [Select column](#select-column)
  * [Suggest column](#suggest-column)
  * [Select multiple column](#select-multiple-column)
  * [Suggest multiple column](#suggest-multiple-column)
  * [Checkbox column](#checkbox-column)
  * [Save button](#save-button)
  * [Check a row value](#check-a-row-value)
  * [Click on a row](#click-on-a-row)
  * [Expand or collapse a row](#expand-or-collapse-a-row)
  * [Context menu on a row](#context-menu-on-a-row)
  * [Context menu option](#context-menu-option)

---

## Basic instructions

Some of the most useful commands are explained below. Although they are useful to solve common test cases, it is reminded that these are just basic commands, and should be used with more specific ones in order to solve more specific use cases. Below, a sample test is described, which belongs to a generic AWE screen test.

> **Note:** Elements between braces (*{}*) are **identifiers** (option identifier, button identifier, etc) and **must be replaced** in all cases.

### Test start

First, it is recommended to set a timeout limit for all the tests (in milliseconds):

| Command             | Target             | Value    |
| ------------------- | ------------------ | -------- |
| setTimeout          | *{test-timeout}*   |          |
| waitForNotVisible   | id=loading-bar     |          |

Where **test-timeout** is the time you want to wait (in milliseconds) for an operation until the test failure. Recommended value is 20000 (20 seconds).

> **Note:** The waitForNotVisible (loading-bar) action is to avoid issues with previously launched tests.

### Go to a new page

This test allows you to open a screen directly from the menu option. If the previous test lets you in the screen you want to open, **don't launch the first action (clickAndWait)**, as it will fail (AWE doesn't load the same page twice, so the clickAndWait action will give a timeout for response).

| Command               | Target             | Value    |
| --------------------- | ------------------ | -------- |
| waitForElementPresent | name=*{option-id}* |          |
| clickAndWait          | name=*{option-id}* |          |
| waitForNotVisible     | id=loading-bar     |          |

Where **option-id** is the option identifier in the menu file (public.xml and private.xml).

> **Note:** The waitForElementPresent (name=*{option-id}*) action is to check that the option is available.

### Click on a button 

| Command               | Target                             | Value    |
| --------------------- | ---------------------------------- | -------- |
| waitForVisible        | css=#*{button-id}*:not([disabled]) |          |
| pause                 | 250                                |          |
| click                 | css=#*{button-id}*:not([disabled]) |          |

Where **button-id** is the button identifier you want to click.

### Wait for a grid to load

| Command               | Target                                   | Value    |
| --------------------- | ---------------------------------------- | -------- |
| waitForVisible        | css=[grid-id='*{grid-id}*'] .grid-loader |          |
| waitForNotVisible     | css=[grid-id='*{grid-id}*'] .grid-loader |          |

Where **grid-id** is the grid identifier you want to wait to be loaded.

> **Note:** [grid-id='{grid-id}'] is **required** only if there are *more than one grid* in the screen (even if the other grids are hidden or in other tabs).

### Check and close a message

| Command                  | Target                                               | Value    |
| ------------------------ | ---------------------------------------------------- | -------- |
| waitForElementPresent    | css=.alert-zone .alert-*{message-type}* button.close |          |
| click                    | css=.alert-zone .alert-*{message-type}* button.close |          |
| waitForElementNotPresent | css=.alert-zone .alert-*{message-type}*              |          |

Where **message-type** depends on the message you want to verify. You have the following message types:

* **success** - green message, when all is ok.
* **warning** - yellow message, there is a warning.
* **info** - blue message, a notification to the user.
* **danger** - red message, the application has failed somehow.

### Accept a confirm dialog

| Command               | Target                           | Value    |
| --------------------- | -------------------------------- | -------- |
| waitForVisible        | css=#confirm-accept              |          |
| click                 | css=#confirm-accept              |          |
| waitForNotVisible     | css=#confirm-accept              |          |

## Criteria

The following points describe how to fill the different type of criteria available in AWE screens:

### Input

| Command               | Target                                   | Value           |
| --------------------- | ---------------------------------------- | --------------- |
| waitForElementPresent | css=[criterion-id='*{input-id}*'] input  |                 |
| type                  | css=[criterion-id='*{input-id}*'] input  | *{input-value}* |
| sendKeys              | css=[criterion-id='*{input-id}*'] input  | ${KEY_TAB}      |

Where **input-id** is the identifier of the text criterion, and **input-value** is the value you want to put in the criterion. 

> **Note:** **${KEY_TAB}** is a selenium special variable to refer a TAB key press.

### Textarea

| Command               | Target                                        | Value              |
| --------------------- | --------------------------------------------- | ------------------ |
| waitForElementPresent | css=[criterion-id='*{textarea-id}*'] textarea |                    |
| type                  | css=[criterion-id='*{textarea-id}*'] textarea | *{textarea-value}* |
| sendKeys              | css=[criterion-id='*{textarea-id}*'] textarea | ${KEY_TAB}         |

Where **textarea-id** is the identifier of the textarea criterion, and **textarea-value** is the value you want to put in the criterion.

### Date

#### Pick a specific date in the datepicker

| Command                 | Target                                                   | Value            |
| ----------------------- | -------------------------------------------------------- | ---------------- |
| waitForElementPresent   | css=[criterion-id='*{date-id}*'] input                   |                  |
| type                    | css=[criterion-id='*{date-id}*'] input                   | *{date-to-pick}* |
| click                   | css=[criterion-id='*{date-id}*'] input                   |                  |
| waitForElementPresent   | css=.datepicker td.day.active:contains('*{day-picked}*') |                  |
| sendKeys                | css=[criterion-id='*{date-id}*'] input                   | ${KEY_TAB}       |
| waitForElementNotPresent| css=.datepicker                                          |                  |

Where **date-id** is the identifier of the date criterion, **date-to-pick** is the date you want to put in the criterion, and **day-picked** is the day of the *date-to-pick* variable.

#### Pick a day from the current month

| Command                 | Target                                             | Value          |
| ----------------------- | -------------------------------------------------- | -------------- |
| waitForElementPresent   | css=[criterion-id='*{date-id}*'] input             |                |
| click                   | css=[criterion-id='*{date-id}*'] input             |                |
| waitForElementPresent   | css=.datepicker td.day:contains('*{day-to-pick}*') |                |
| click                   | css=.datepicker td.day:contains('*{day-to-pick}*') |                |
| waitForElementNotPresent| css=.datepicker                                    |                |

Where **date-id** is the identifier of the date criterion and **day-to-pick** is the day of the current month you want to put in the criterion.

#### Pick a month in the month-selector

| Command                 | Target                                             | Value          |
| ----------------------- | -------------------------------------------------- | -------------- |
| waitForElementPresent   | css=[criterion-id='*{date-id}*'] input             |                |
| click                   | css=[criterion-id='*{date-id}*'] input             |                |
| waitForElementPresent   | css=.datepicker span.month:contains('*{month-to-pick}*') |                |
| click                   | css=.datepicker span.month:contains('*{month-to-pick}*') |                |
| waitForElementNotPresent| css=.datepicker                                    |                |

Where **date-id** is the identifier of the date criterion and **month-to-pick** is the month you want to put in the criterion.

#### Pick a year in the year-selector

| Command                 | Target                                             | Value          |
| ----------------------- | -------------------------------------------------- | -------------- |
| waitForElementPresent   | css=[criterion-id='*{date-id}*'] input             |                |
| click                   | css=[criterion-id='*{date-id}*'] input             |                |
| waitForElementPresent   | css=.datepicker span.year:contains('*{year-to-pick}*') |                |
| click                   | css=.datepicker span.year:contains('*{year-to-pick}*') |                |
| waitForElementNotPresent| css=.datepicker                                    |                |

Where **date-id** is the identifier of the date criterion and **year-to-pick** is the year you want to put in the criterion.

### Time

Pick a time in the timepicker.

| Command               | Target                                  | Value           |
| --------------------- | --------------------------------------- | --------------- |
| waitForElementPresent | css=[criterion-id='*{time-id}*'] input  |                 |
| type                  | css=[criterion-id='*{time-id}*'] input  | *{time-value}*  |
| sendKeys              | css=[criterion-id='*{time-id}*'] input  | ${KEY_TAB}      |

Where **time-id** is the identifier of the time criterion and **time-value** is the time you want to put in the criterion.

### Select

| Command                  | Target                                                              | Value  |
| ------------------------ | ------------------------------------------------------------------- | ------ |
| waitForElementPresent    | css=[criterion-id='*{select-id}*'] .select2-choice                  |        |
| clickAt                  | css=[criterion-id='*{select-id}*'] .select2-choice                  |        |
| waitForElementPresent    | css=#select2-drop .select2-result-label:contains('*{select-text}*') |        |
| clickAt                  | css=#select2-drop .select2-result-label:contains('*{select-text}*') |        |
| waitForElementNotPresent | css=#select2-drop                                                   |        |

Where **select-id** is the identifier of the select criterion, and **select-text** is the value you want to find in the select result list. 

### Suggest

| Command                  | Target                                                        | Value            |
| ------------------------ | ------------------------------------------------------------- | ---------------- |
| waitForElementPresent    | css=[criterion-id='*{suggest-id}*'] .select2-choice           |                  |
| clickAt                  | css=[criterion-id='*{suggest-id}*'] .select2-choice           |                  |
| waitForElementPresent    | css=#select2-drop .select2-input                              |                  |
| sendKeys                 | css=#select2-drop .select2-input                              | *{suggest-text}* |
| waitForElementPresent    | css=#select2-drop .select2-match:contains('*{suggest-text}*') |                  |
| clickAt                  | css=#select2-drop .select2-match:contains('*{suggest-text}*') |                  |
| waitForElementNotPresent | css=#select2-drop                                             |                  |

Where **suggest-id** is the identifier of the suggest criterion, and **suggest-text** is the value you want to find in the suggest result list. 

### If interested in clear this type of components shoud use next target:

| Command                  | Target                                                              | Value  |
| ------------------------ | ------------------------------------------------------------------- | ------ |
| waitForElementPresent    | css=[criterion-id='*{criterion-id}*'] .select2-choice                 |        |
| clickAt                  | css=[criterion-id='*{criterion-id}*'] .select2-search-choice-close    |        |

### Select multiple

| Command                  | Target                                                               | Value           |
| ------------------------ | -------------------------------------------------------------------- | --------------- |
| waitForElementPresent    | css=[criterion-id='*{select-id}*'] .select2-input                    |                 |
| sendKeys                 | css=[criterion-id='*{select-id}*'] .select2-input                    | *{select-text}* |
| waitForElementPresent    | css=#select2-drop .select2-match:contains('*{select-text}*')         |                 |
| clickAt                  | css=#select2-drop .select2-match:contains('*{select-text}*')         |                 |
| waitForElementNotPresent | css=#select2-drop                                                    |                 |

Where **select-id** is the identifier of the select criterion, and **select-text** is the value you want to find in the select result list. 

### Suggest multiple
 
| Command                  | Target                                                        | Value            |
| ------------------------ | ------------------------------------------------------------- | ---------------- |
| waitForElementPresent    | css=[criterion-id='*{suggest-id}*'] .select2-input            |                  |
| sendKeys                 | css=[criterion-id='*{suggest-id}*'] .select2-input            | *{suggest-text}* |
| waitForElementPresent    | css=#select2-drop .select2-match:contains('*{suggest-text}*') |                  |
| pause           | 250 |            |
| clickAt                  | css=#select2-drop .select2-match:contains('*{suggest-text}*') |                  |
| waitForElementNotPresent | css=#select2-drop                                             |                  |

Where **suggest-id** is the identifier of the suggest criterion, and **suggest-text** is the value you want to find in the suggest result list.

### Tabs

#### Check active tab

Command to check if active tab is the one with the requested text.

| Command                  | Target                                               | Value           |
| ------------------------ | ---------------------------------------------------- | --------------- |
| waitForElementPresent    | css=#*{tab-id}* li.active a:contains('*{tab-text}*') |                 |

Where **tab-id** is the identifier of the tab, and **tab-text** is the tab text to be matched.

#### Click on a tab

| Command  | Target                                     | Value  |
| -------- | ------------------------------------------ | ------ |
| click    | css=#*{tab-id}* a:contains('*{tab-text}*') |        |

Where **tab-id** is the identifier of the tab, and **tab-text** is the tab text to be matched.

### Checkbox

Click on a checkbox.

| Command               | Target                                     | Value           |
| --------------------- | ------------------------------------------ | --------------- |
| waitForElementPresent | css=[criterion-id='*{checkbox-id}*'] input |                 |
| clickAt               | css=[criterion-id='*{checkbox-id}*'] input |                 |

Where **checkbox-id** is the identifier of the checkbox.

### Radio button

Click on a radio button.

| Command               | Target                                  | Value           |
| --------------------- | --------------------------------------- | --------------- |
| waitForElementPresent | css=[criterion-id='*{radio-id}*'] input |                 |
| clickAt               | css=[criterion-id='*{radio-id}*'] input |                 |

Where **radio-id** is the identifier of the radio button.

### Text view

Check if a text view component contains a text

| Command               | Target                                  | Value           |
| --------------------- | --------------------------------------- | --------------- |
| waitForElementPresent | css=[criterion-id='*{text-view-id}*'] span.text-value:contains('*{text-view-value}*')|                 |

Where **text-view-id** is the identifier of the text-view component, and **text-view-value** is the value we want to check.


### Verify criteria values

In general, all criteria types are verified in the same way, checking the value as follows:

| Command     | Target              | Value               |
| ------------| ------------------- | ------------------- |
| verifyValue | id=*{criterion-id}* | *{criterion-value}* |

> **Note:** If we are not sure which is the value the criterion has, we can check it using the developer tools and viewing the model of the criterion. In general, for composed criteria (select/suggest) is the "value" we return in the corresponding query/enumerated. For radio and checkbox criteria the value is 1 when it is checked and 0 when it is not.


## Grid cells

There are some considerations to know about accessing grid cells:
* All grid content is always under a grid-id attribute: [grid-id='*{grid-id}*']
* All cells inside a grid have a row and a column attribute to identify them: [row-id='*{row-id}*'] [column-id='*{column-name}*']
* Cell criteria identifiers are defined as **column-id** instead of **criterion-id**: [column-id='*{column-name}*'].

### Input column

| Command               | Target                                                     | Value           |
| --------------------- | ---------------------------------------------------------- | --------------- |
| waitForElementPresent | css=[grid-id='*{grid-id}*'] [column-id='*{input-id}*'] input  |                 |
| type                  | css=[grid-id='*{grid-id}*'] [column-id='*{input-id}*'] input  | *{input-value}* |
| sendKeys              | css=[grid-id='*{grid-id}*'] [column-id='*{input-id}*'] input  | ${KEY_TAB}      |

Where **grid-id** is the container grid identifier, **input-id** is the of the text criterion identifier, and **input-value** is the value you want to put in the criterion.

### Textarea column

| Command               | Target                                                          | Value              |
| --------------------- | --------------------------------------------------------------- | ------------------ |
| waitForElementPresent | css=[grid-id='*{grid-id}*'] [column-id='*{textarea-id}*'] textarea |                    |
| type                  | css=[grid-id='*{grid-id}*'] [column-id='*{textarea-id}*'] textarea | *{textarea-value}* |
| sendKeys              | css=[grid-id='*{grid-id}*'] [column-id=*{textarea-id}*'] textarea | ${KEY_TAB}         |

Where **grid-id** is the container grid identifier, **textarea-id** is the of the textarea criterion identifier, and **textarea-value** is the value you want to put in the criterion.

### Date column

#### Pick a specific date in the datepicker in a column

| Command                 | Target                                                   | Value            |
| ----------------------- | -------------------------------------------------------- | ---------------- |
| waitForElementPresent   | css=[grid-id='*{grid-id}*'] [column-id='*{date-id}*'] input |                  |
| sendKeys                | css=[grid-id='*{grid-id}*'] [column-id='*{date-id}*'] input | *{date-to-pick}* |
| click                   | css=[grid-id='*{grid-id}*'] [column-id='*{date-id}*'] input |                  |
| waitForElementPresent   | css=.datepicker td.day.active:contains('*{day-picked}*') |                  |
| sendKeys                | css=[grid-id='*{grid-id}*'] [column-id='*{date-id}*'] input | ${KEY_TAB}       |
| waitForElementNotPresent| css=.datepicker                                          |                  |

Where **grid-id** is the container grid identifier, **date-id** is the identifier of the date criterion, **date-to-pick** is the date you want to put in the criterion, and **day-picked** is the day of the *date-to-pick* variable.

#### Pick a day from the current month in a column

| Command                 | Target                                                   | Value          |
| ----------------------- | -------------------------------------------------------- | -------------- |
| waitForElementPresent   | css=[grid-id='*{grid-id}*'] [column-id='*{date-id}*'] input |                |
| click                   | css=[grid-id='*{grid-id}*'] [column-id='*{date-id}*'] input |                |
| waitForElementPresent   | css=.datepicker td.day:contains('*{day-to-pick}*')       |                |
| click                   | css=.datepicker td.day:contains('*{day-to-pick}*')       |                |
| waitForElementNotPresent| css=.datepicker                                          |                |

Where **grid-id** is the container grid identifier, **date-id** is the identifier of the date criterion and **day-to-pick** is the day of the current month you want to put in the criterion.

### Time column

Pick a time in the timepicker.

| Command               | Target                                                    | Value          |
| --------------------- | --------------------------------------------------------- | -------------- |
| waitForElementPresent | css=[grid-id='*{grid-id}*'] [column-id='*{time-id}*'] input  |                |
| type                  | css=[grid-id='*{grid-id}*'] [column-id='*{time-id}*'] input  | *{time-value}* |
| sendKeys              | css=[grid-id='*{grid-id}*'] [column-id='*{time-id}*'] input  | ${KEY_TAB}     |

Where **grid-id** is the container grid identifier, **time-id** is the identifier of the time criterion and **time-value** is the time you want to put in the criterion.

### Select column

| Command                  | Target                                                               | Value  |
| ------------------------ | -------------------------------------------------------------------- | ------ |
| waitForElementPresent    | css=[grid-id='*{grid-id}*'] [column-id='*{select-id}*'] .select2-choice |        |
| clickAt                  | css=[grid-id='*{grid-id}*'] [column-id='*{select-id}*'] .select2-choice |        |
| waitForElementPresent    | css=#select2-drop .select2-result-label:contains('*{select-text}*')  |        |
| clickAt                  | css=#select2-drop .select2-result-label:contains('*{select-text}*')  |        |
| waitForElementNotPresent | css=#select2-drop                                                    |        |

Where **grid-id** is the container grid identifier, **select-id** is the identifier of the select criterion, and **select-text** is the value you want to find in the select result list. 

### Suggest column

| Command                  | Target                                                                | Value            |
| ------------------------ | --------------------------------------------------------------------- | ---------------- |
| waitForElementPresent    | css=[grid-id='*{grid-id}*'] [column-id='*{suggest-id}*'] .select2-choice |                  |
| clickAt                  | css=[grid-id='*{grid-id}*'] [column-id='*{suggest-id}*'] .select2-choice |                  |
| waitForElementPresent    | css=#select2-drop .select2-input                                      |                  |
| sendKeys                 | css=#select2-drop .select2-input                                      | *{suggest-text}* |
| waitForElementPresent    | css=#select2-drop .select2-match:contains('*{suggest-text}*')         |                  |
| clickAt                  | css=#select2-drop .select2-match:contains('*{suggest-text}*')         |                  |
| waitForElementNotPresent | css=#select2-drop                                                     |                  |

Where **grid-id** is the container grid identifier, **suggest-id** is the identifier of the suggest criterion, and **suggest-text** is the value you want to find in the suggest result list. 

### Select multiple column

| Command                  | Target                                                               | Value           |
| ------------------------ | -------------------------------------------------------------------- | --------------- |
| waitForElementPresent    | css=[grid-id='*{grid-id}*'] [column-id='*{select-id}*'] .select2-input  |                 |
| sendKeys                 | css=[grid-id='*{grid-id}*'] [column-id='*{select-id}*'] .select2-input  | *{select-text}* |
| waitForElementPresent    | css=#select2-drop .select2-match:contains('*{select-text}*')         |                 |
| clickAt                  | css=#select2-drop .select2-match:contains('*{select-text}*')         |                 |
| waitForElementNotPresent | css=#select2-drop                                                    |                 |

Where **grid-id** is the container grid identifier, **select-id** is the identifier of the select criterion, and **select-text** is the value you want to find in the select result list. 

### Suggest multiple column
 
| Command                  | Target                                                               | Value            |
| ------------------------ | -------------------------------------------------------------------- | ---------------- |
| waitForElementPresent    | css=[grid-id='*{grid-id}*'] [column-id='*{suggest-id}*'] .select2-input |                  |
| sendKeys                 | css=[grid-id='*{grid-id}*'] [column-id='*{suggest-id}*'] .select2-input | *{suggest-text}* |
| waitForElementPresent    | css=#select2-drop .select2-match:contains('*{suggest-text}*')        |                  |
| clickAt                  | css=#select2-drop .select2-match:contains('*{suggest-text}*')        |                  |
| waitForElementNotPresent | css=#select2-drop                                                    |                  |

Where **grid-id** is the container grid identifier, **suggest-id** is the identifier of the suggest criterion, and **suggest-text** is the value you want to find in the suggest result list.

### Checkbox column

Click on a checkbox.

| Command               | Target                                                       | Value |
| --------------------- | ------------------------------------------------------------ | ----- |
| waitForElementPresent | css=[grid-id='*{grid-id}*'] [column-id='*{checkbox-id}*'] input |       |
| clickAt               | css=[grid-id='*{grid-id}*'] [column-id='*{checkbox-id}*'] input |       |

Where **grid-id** is the container grid identifier, **checkbox-id** is the identifier of the checkbox.

### Save button

| Command               | Target                                                     | Value |
| --------------------- | ---------------------------------------------------------- | ----- |
| waitForVisible        | css=[grid-id='*{grid-id}*'] .grid-row-save                 |       |
| clickAt               | css=[grid-id='*{grid-id}*'] .grid-row-save                 |       |
| waitForElementPresent | css=[grid-id='*{grid-id}*'] .grid-row-save:not([disabled]) |       |

Where **grid-id** is the grid identifier.

### Check a row value

| Command               | Target                                                                         | Value |
| --------------------- | ------------------------------------------------------------------------------ | ----- |
| waitForElementPresent | css=[grid-id='*{grid-id}*'] [row-id='*{row-id}*'] [column-id='*{column-name}*']:contains('*{test-value}*') | |

Where **grid-id** is the grid identifier, **row-id** is the row identifier, **column-name** is the name of the column you want to check the value and **test-value** is the value you match with the column value

> **Note:** You can check all rows from a grid removing the [row-id='*{row-id}*'] part

### Click on a row

| Command | Target                                                                       | Value |
| --------| ---------------------------------------------------------------------------- | ----- |
| clickAt | css=[grid-id='*{grid-id}*'] [row-id='*{row-id}*'] [column-id='*{column-name}*'] |       |

Where **grid-id** is the identifier of the grid, **row-id** is the row identifier and **column-name** is the name of the column you want to click.

### Expand or collapse a row

| Command | Target                                                                       | Value |
| --------| ---------------------------------------------------------------------------- | ----- |
| clickAt | css=[grid-id='*{grid-id}*'] [row-id='*{row-id}*'] i.tree-icon |       |

Where **grid-id** is the identifier of the grid, **row-id** is the row identifier.

> **Note:** If you click on an already expanded row, it will collapse

### Context menu on a row

Shows a context menu on a grid row.

| Command       | Target                                                                       | Value |
| ------------- | ---------------------------------------------------------------------------- | ----- |
| contextMenuAt | css=[grid-id='*{grid-id}*'] [row-id='*{row-id}*'] [column-id='*{column-name}*'] |       |

Where **grid-id** is the identifier of the grid, **row-id** is the row identifier and **column-name** is the name of the column where you want to show the context menu.

### Context menu option

Click on a context menu option

| Command               | Target                                  | Value |
| --------------------- | --------------------------------------- | ----- |
| waitForElementPresent | css=[name='*{context-menu-option-id}*'] |       |
| mouseOver             | css=[name='*{context-menu-option-id}*'] |       |
| clickAt               | css=[name='*{context-menu-option-id}*'] |       |

Where **context-menu-option-id** is the identifier of the context menu option.

