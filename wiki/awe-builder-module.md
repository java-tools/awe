Almis Web Engine > **[Advanced Development Guide](advanced-developer-guide.md)**

---

# **AWE Builder Module**

## Table of Contents

* **[Introduction](#introduction)**
* **[Configuration](#configuration)**
* **[Client action builders](#client-action-builders)**
  * **[CSS action builders](#css-action-builders)**
  * **[Grid action builders](#grid-action-builders)**
  * **[Chart action builders](#chart-action-builders)**
* **[Screen builders](#screen-builders)**

##  **Introduction**

Builder module is a set of Java utilities designed to cover some functionalities:

- Make easier the developer task of sending actions to the frontend.
- Allow to define dynamic screens in Java instead of XML files.

##  **Configuration**

Add the following dependency into your maven `pom.xml` file:

```xml
<dependency>
  <groupId>com.almis.awe</groupId>
  <artifactId>awe-builder-spring-boot-starter</artifactId>
</dependency>
```

And there you go!

##  **Client action builders**

Client action builders helps the developer in the task of sending specific client actions to the frontend. 

####  **`screen` action builder**

This action makes the application to move to another screen.

Usage:

```java
serviceData.addClientAction(new ScreenActionBuilder("another-screen-option").build());
```

This sample creates a client action which will try to navigate to the option defined as `another-screen-option`.

####  **`fill` action builder**

This action sends a list of data to a component:

Usage:

```java
DataList dataList = new DataList();
// ...
serviceData.addClientAction(new FillActionBuilder("my-grid", dataList).build());
```

This sample sends `dataList` to `"my-grid"` component.

####  **`select` action builder**

This action sends a list of selected values to a component.

Usage:

```java
List<String> values = new ArrayList();
// ...
serviceData.addClientAction(new SelectActionBuilder("my-select", values).build());
```

This sample sends a list of selected values to `"my-select"` component.

####  **`message` action builder**

This action allows sending a message to the user. Message type can be one of `OK`, `WARNING`, `ERROR` or `INFO`.

Usage:

```java
serviceData.addClientAction(new MessageActionBuilder(AnswerType.OK, "message title", "message description").build());
```

This sample sends a message to the frontend.

####  **`dialog` action builder**

This action opens a modal window in the screen.

Usage:

```java
serviceData.addClientAction(new DialogActionBuilder("my-dialog").build());
```

This sample opens the modal screen named `"my-dialog"`.

####  **`confirm` action builder**

This action opens a modal window with a button to confirm or cancel action.

Usage:

```java
serviceData.addClientAction(new ConfirmActionBuilder("messageId").build());
```

This sample opens a modal confirm dialog using the `message` tag defined as `message-id`.

An alternate usage of this builder is the following one:

```java
serviceData.addClientAction(new ConfirmActionBuilder("message title", "message description").build());
```

Instead of using a defined `message` tag, this action uses the `title` and `description` parameter defined on it.

####  **`get-file` action builder**

This action asks a file to be downloaded.

Usage:

```java
FileData fileData = new FileData();
//...
serviceData.addClientAction(new DownloadActionBuilder(fileData).build());
```

This sample try to download the file defined in the `fileData` bean.

###  **CSS action builders**

####  **`add-class` action builder**

This action search for a CSS class in the screen and if it finds it, adds some CSS classes to the element.

Usage:

```java
serviceData.addClientAction(new AddCssClassActionBuilder(".selector", "class1", "class2", "class3").build());
```

This sample search for the `.selector` CSS selector, and adds the "class1", "class2" and "class3" classes into the element if found.

####  **`remove-class` action builder**

This action search for a CSS class in the screen and if it finds it, removes some CSS classes from the element.

Usage:

```java
serviceData.addClientAction(new RemoveCssClassActionBuilder(".selector", "class1", "class2", "class3").build());
```

This sample search for the `.selector` CSS selector, and removes the "class1", "class2" and "class3" classes from the element if found.

###  **Grid action builders**

####  **`add-columns` action builder**

This action adds some columns on a grid.

Usage:

```java
List<Column> columnList = new ArrayList<>();
//...
serviceData.addClientAction(new AddColumnsActionBuilder("my-grid", columnList).build());
```

This sample adds a list of columns on the `"my-grid"` grid.

####  **`replace-columns` action builder**

This action **replaces all columns on the grid** with the defined column list.

Usage:

```java
List<Column> columnList = new ArrayList<>();
//...
serviceData.addClientAction(new ReplaceColumnsActionBuilder("my-grid", columnList).build());
```

This sample replaces all columns on the `"my-grid"` grid.

####  **`update-cell` action builder**

This action updates a grid cell data.

Usage:

```java
ComponentAddress address = new ComponentAddress("view", "component", "row", "column");
CellData cellData = new CellData(value);
//...
serviceData.addClientAction(new UpdateCellActionBuilder("my-grid", cellData).build());
```

This sample changes the cell `column` at row `row` on `component` grid with `cellData` value.

This builder also allows to define data as `JsonNode` instead of `CellData`:

```java
ComponentAddress address = new ComponentAddress("view", "component", "row", "column");
ObjectNode nodeData = JsonNodeFactory.instance.objectNode();
nodeData.put("icon", "fa-empire");
nodeData.put("style", "text-danger");
nodeData.put("value", "fa-empire");
//...
serviceData.addClientAction(new UpdateCellActionBuilder(address, nodeData).build());
```

###  **Chart action builders**

####  **`add-chart-series` action builder**

This action allows to add series to a chart

Usage:

```java
List<ChartSerie> serieList = new ArrayList<>();
//...
serviceData.addClientAction(new AddChartSeriesActionBuilder("my-chart", serieList).build());
```

This sample adds a list of series on the `"my-chart"` chart.

####  **`replace-chart-series` action builder**

This action allows to **replace all series** from a chart

Usage:

```java
List<ChartSerie> serieList = new ArrayList<>();
//...
serviceData.addClientAction(new ReplaceChartSeriesActionBuilder("my-chart", serieList).build());
```

This sample replaces all series on the `"my-chart"` chart.

####  **`remove-chart-series` action builder**

This action allows to remove some series from a chart.

Usage:

```java
List<ChartSerie> serieList = new ArrayList<>();
//...
serviceData.addClientAction(new RemoveChartSeriesActionBuilder("my-chart", serieList).build());
```

This sample remove all series defined on serieList from the `"my-chart"` chart.

####  **`add-points` action builder**

This action sends a list of serie data to a chart:

Usage:

```java
DataList dataList = new DataList();
// ...
serviceData.addClientAction(new AddPointsActionBuilder("my-chart", dataList).build());
```

This sample sends `dataList` points to `"my-chart"` component.
 
> **Note:** Each datalist column name must match a chart serie id.

##  **Screen builders**

This set of builders is designed to generate java-defined screens instead of defining them in XML format.

**WORK IN PROGRESS**