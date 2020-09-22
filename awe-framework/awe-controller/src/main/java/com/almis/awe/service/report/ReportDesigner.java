package com.almis.awe.service.report;


import com.almis.ade.api.bean.component.Image;
import com.almis.ade.api.bean.component.*;
import com.almis.ade.api.bean.component.grid.GridHeader;
import com.almis.ade.api.bean.component.grid.ReportColumn;
import com.almis.ade.api.bean.component.grid.ReportGrid;
import com.almis.ade.api.bean.component.grid.ReportHeader;
import com.almis.ade.api.bean.input.DataBean;
import com.almis.ade.api.bean.input.PrintBean;
import com.almis.ade.api.bean.section.PageFooter;
import com.almis.ade.api.bean.section.PageHeader;
import com.almis.ade.api.bean.style.StyleTemplate;
import com.almis.ade.api.enumerate.ColumnType;
import com.almis.ade.api.enumerate.LayoutType;
import com.almis.ade.api.enumerate.PagingType;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.chart.Chart;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import com.almis.awe.model.entities.screen.component.grid.GroupHeader;
import com.almis.awe.model.type.TotalizeStyleType;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

/**
 * Generate the component controllers of the screen
 */
public class ReportDesigner extends ServiceConfig {

  // Autowired services
  private final QueryService queryService;

  @Value("${settings.dataSuffix:.data}")
  private String dataSuffix;

  @Value("${application.data.pixelsPerCharacter:7}")
  private Integer pixelsPerCharacter;

  // Report colors
  // TODO: Make them customizable
  private static final Color REPORT_HEADER_TEXT_COLOR = new Color(100, 100, 100);
  private static final Color REPORT_HEADER_LINE_COLOR = new Color(200, 200, 200);
  private static final Integer REPORT_HEADER_TEXT_SIZE = 12;
  private static final Integer REPORT_FOOTER_TEXT_SIZE = 10;
  private static final Integer CRITERIA_GAP = 4;
  private static final Integer TOP_PADDING = 10;
  private static final Integer TOP_PADDING_SMALL = 5;
  private static final Integer MAX_GRID_FONT_SIZE = 10;
  private static final Integer MIN_GRID_FONT_SIZE = 5;
  private static final Float GRID_FONT_CORRECTION_RATIO = 1.7f;

  /**
   * Autowired constructor
   *
   * @param queryService Query service
   */
  public ReportDesigner(QueryService queryService) {
    this.queryService = queryService;
  }

  /**
   * Design the report
   *
   * @param reportStructure Report structure
   * @param parameters      Screen parameters
   * @return Print bean designed
   * @throws AWException Error designing report
   */
  public PrintBean getPrintDesign(List<Element> reportStructure, ObjectNode parameters) throws AWException {
    Layout layout = new Layout("report-layout", LayoutType.MULTIPAGE);
    PrintBean printBean = new PrintBean()
      .setDetail(layout);

    // Set orientation if defined
    if (parameters.has(AweConstants.PRINT_ORIENTATION) && !parameters.get(AweConstants.PRINT_ORIENTATION).isNull()) {
      printBean.setOrientation(PageOrientation.valueOf(parameters.get(AweConstants.PRINT_ORIENTATION).asText()));
    }

    // For each element, generate report bean
    for (Element element : reportStructure) {
      if (element instanceof Screen) {
        getElementDesign(printBean, (Screen) element);
      } else if (element instanceof Grid) {
        getElementDesign(printBean, (Grid) element, parameters);
      } else if (element instanceof Chart) {
        getElementDesign(printBean, (Chart) element, parameters);
      } else if (element instanceof Criteria) {
        initializeCriteriaLayout(layout);
        getElementDesign(printBean, (Criteria) element, parameters);
      } else {
        getElementDesign(printBean, element);
      }
    }

    // Default orientation
    if (printBean.getOrientation() == null) {
      printBean.setOrientation(PageOrientation.PORTRAIT);
    }

    // Retrieve print bean generated
    return printBean;
  }

  /**
   * Initialize criteria layout
   *
   * @param layout Multipage layout
   */
  private void initializeCriteriaLayout(Layout layout) {
    if (layout.getElements().isEmpty() ||
      !"criteria-layout".equalsIgnoreCase(layout.getElements().get(layout.getElements().size() - 1).getIdentifier())) {
      Layout criteriaLayout = new Layout("criteria-layout", LayoutType.HORIZONTAL).setGap(CRITERIA_GAP);
      criteriaLayout.getStyle()
        .setTopPadding(TOP_PADDING)
        .setBottomPadding(TOP_PADDING);
      layout.addElement(criteriaLayout);
    }
  }

  /**
   * Get element design for screen
   *
   * @param printBean Print bean
   * @param element   Element
   */
  private void getElementDesign(PrintBean printBean, Screen element) {
    String title = getLocale(element.getLabel());
    // Generate layouts
    PageHeader header = new PageHeader("page-header");
    PageFooter footer = new PageFooter("page-footer");

    // Generate header
    Text headerTitle = new Text("report-header")
      .setValue(title);
    headerTitle.getStyle()
      .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
      .bold()
      .setFontSize(REPORT_HEADER_TEXT_SIZE)
      .setForegroundColor(REPORT_HEADER_TEXT_COLOR);
    header.setLine(new Line("header-line")
      .setLineColor(REPORT_HEADER_LINE_COLOR));
    header.setTitle(headerTitle);

    // Generate footer
    Paging footerPaging = new Paging("report-pagination", PagingType.X_SLASH_Y);
    footerPaging.getStyle()
      .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
      .setFontSize(REPORT_FOOTER_TEXT_SIZE)
      .setForegroundColor(REPORT_HEADER_TEXT_COLOR);
    footer.setLine(new Line("footer-line")
      .setLineColor(REPORT_HEADER_LINE_COLOR));
    footer.setPaging(footerPaging);

    // Add to print bean
    printBean
      .setPageHeader(header)
      .setPageFooter(footer);
  }

  /**
   * Get element design for criteria
   *
   * @param printBean  Print bean
   * @param element    Element
   * @param parameters Parameters
   */
  private void getElementDesign(PrintBean printBean, Criteria element, ObjectNode parameters) {
    Layout layout = (Layout) ((Layout) printBean.getDetail()).getElements().get(((Layout) printBean.getDetail()).getElements().size() - 1);

    // Add criterion element to layout as text
    Criterion criterionElement = new Criterion(element.getId());

    // Get criterion data
    JsonNode data = parameters.get(element.getId() + dataSuffix);

    if (data != null) {
      String criterionValue = data.get(AweConstants.JSON_TEXT_PARAMETER).asText();
      if (criterionValue != null && !criterionValue.isEmpty()) {
        // Configure criterion label
        String criterionLabel = null;
        if (element.getLabel() != null) {
          criterionLabel = getLocale(element.getLabel());
        }

        if (criterionLabel != null) {
          // Set criterion values
          criterionElement.setTitle(criterionLabel)
            .setValue(criterionValue);

          // Add element to layout
          layout.addElement(criterionElement)
            .getStyle()
            .setTopPadding(TOP_PADDING_SMALL);
        }
      }
    }
  }

  /**
   * Get element design for grid
   *
   * @param printBean  Print bean
   * @param element    Element
   * @param parameters Parameters
   */
  private void getElementDesign(PrintBean printBean, Grid element, ObjectNode parameters) throws AWException {
    Layout layout = (Layout) printBean.getDetail();
    Layout gridLayout = new Layout("grid-layout", LayoutType.VERTICAL);
    layout.addElement(gridLayout);

    // Add grid element to layout
    ReportGrid gridElement = new ReportGrid(element.getId());

    // Set grid title
    if (element.getLabel() != null) {
      Text gridTitleText = new Text(element.getId() + "-title")
        .setValue(getLocale(element.getLabel()))
        .setStyle(stl.style(StyleTemplate.TITLE_STYLE));
      gridElement.setTitle(gridTitleText);
    }

    // Get grid data
    ObjectNode visibleColumns = getVisibleColumns(element, parameters);

    // Generate columns
    List<String> fields = setGridColumns(element, gridElement, visibleColumns);

    // Set grid orientation and font size
    calculateGridOrientationAndFont(gridElement, printBean);

    // Add data to grid
    setGridData(element, gridElement, fields, parameters);

    // Add grid to layout
    gridLayout.addElement(gridElement)
      .getStyle()
      .setTopPadding(TOP_PADDING);
  }

  /**
   * Add sort data to parameters
   *
   * @param reportGrid Report grid
   */
  private Integer getGridWidth(ReportGrid reportGrid) {
    int gridWidth = 0;
    for (GridHeader header : reportGrid.getGridHeaders()) {
      if (header instanceof ReportColumn) {
        ReportColumn reportColumn = (ReportColumn) header;
        gridWidth += reportColumn.getWidth() == null ? 0 : reportColumn.getWidth();
      } else if (header instanceof ReportHeader) {
        ReportHeader reportHeader = (ReportHeader) header;
        for (ReportColumn column : reportHeader.getColumns()) {
          gridWidth += column.getWidth() == null ? 0 : column.getWidth();
        }
      }
    }
    return gridWidth;
  }


  /**
   * Add sort data to parameters
   *
   * @param reportGrid Report grid
   */
  private void calculateGridOrientationAndFont(ReportGrid reportGrid, PrintBean printBean) {
    Integer gridWidth = getGridWidth(reportGrid);
    int gridPageSize = PageOrientation.LANDSCAPE.equals(printBean.getOrientation()) ?
      Image.Size.FULL_SIZE_LANDSCAPE.getSize() : Image.Size.FULL_SIZE.getSize();
    if (gridWidth > Image.Size.FULL_SIZE.getSize()) {
      if (printBean.getOrientation() == null) {
        printBean.setOrientation(PageOrientation.LANDSCAPE);
        gridPageSize = Image.Size.FULL_SIZE_LANDSCAPE.getSize();
      }
      float fontSize = MAX_GRID_FONT_SIZE - (gridWidth * GRID_FONT_CORRECTION_RATIO / gridPageSize);
      reportGrid.setFontSize(Math.max(Math.round(fontSize), MIN_GRID_FONT_SIZE));
    } else {
      reportGrid.setFontSize(MAX_GRID_FONT_SIZE);
    }
  }

  /**
   * Get visible columns
   *
   * @param grid       Grid element
   * @param parameters Parameters
   * @return Visible columns
   */
  private ObjectNode getVisibleColumns(Grid grid, ObjectNode parameters) {
    return (ObjectNode) parameters.get(grid.getId() + dataSuffix).get(AweConstants.JSON_VISIBLE_COLUMNS);
  }

  /**
   * Add sort data to parameters
   *
   * @param grid       Grid element
   * @param parameters Parameters
   */
  private void addSortData(Grid grid, ObjectNode parameters) {
    ArrayNode sortData = (ArrayNode) parameters.get(grid.getId() + dataSuffix).get(AweConstants.COMPONENT_SORT);
    parameters.set(AweConstants.COMPONENT_SORT, sortData);
  }

  /**
   * Fill grid data
   *
   * @param grid           Grid element
   * @param reportGrid     Report grid
   * @param visibleColumns Visible columns
   */
  private List<String> setGridColumns(Grid grid, ReportGrid reportGrid, ObjectNode visibleColumns) {
    List<String> fieldList = new ArrayList<>();
    for (Element element : grid.getElementList()) {
      if (element instanceof Column) {
        Column column = (Column) element;
        if (isPrintable(column, visibleColumns)) {
          String label = visibleColumns.get(column.getName()).asText();
          reportGrid.addGridHeader(getReportColumn(column, label, fieldList));
        }
      } else if (element instanceof GroupHeader) {
        GroupHeader groupHeader = (GroupHeader) element;
        ReportHeader reportHeader = new ReportHeader(groupHeader.getLabel())
          .setLabel(getLocale(groupHeader.getLabel()));
        for (Column column : groupHeader.getChildrenByType(Column.class)) {
          if (isPrintable(column, visibleColumns)) {
            String label = visibleColumns.get(column.getName()).asText();
            reportHeader.addColumn(getReportColumn(column, label, fieldList));
          }
        }
        reportGrid.addGridHeader(reportHeader);
      }
    }
    return fieldList;
  }

  /**
   * Generate report column and retrieve it
   *
   * @param column    Column
   * @param label     Column label
   * @param fieldList Grid field list
   * @return Report column
   */
  private ReportColumn getReportColumn(Column column, String label, List<String> fieldList) {
    ReportColumn reportColumn = new ReportColumn(column.getName())
      .setLabel(label)
      .setField(column.getName())
      .setWidth(getColumnWidth(column));
    HorizontalTextAlignment alignment = getColumnAlignment(column);
    if (alignment != null) {
      reportColumn.setAlign(alignment);
    }
    if (column.getType() != null) {
      reportColumn.setType(ColumnType.valueOf(column.getType().toUpperCase()));
    }
    if (column.getComponentType() != null &&
      ("icon".equalsIgnoreCase(column.getComponentType()) || "image".equalsIgnoreCase(column.getComponentType()))) {
      reportColumn.setType(ColumnType.valueOf(column.getComponentType().toUpperCase()));
    }
    fieldList.add(column.getName());
    return reportColumn;
  }


  /**
   * @param column         Column
   * @param visibleColumns Visible columns
   * @return Column is printable
   */
  private boolean isPrintable(Column column, ObjectNode visibleColumns) {
    return visibleColumns.has(column.getName()) && column.isPrintable();
  }

  /**
   * Get column width
   *
   * @param column Column
   * @return Column is printable
   */
  private Integer getColumnWidth(Column column) {
    Integer columnWidth = null;
    if (column.getCharLength() != null) {
      columnWidth = column.getCharLength() * pixelsPerCharacter;
    } else if (column.getWidth() != null) {
      columnWidth = column.getWidth().equalsIgnoreCase("*") ? 300 : Integer.parseInt(column.getWidth());
    }
    return columnWidth;
  }

  /**
   * Get column alignment
   *
   * @param column Column
   * @return Column is printable
   */
  private HorizontalTextAlignment getColumnAlignment(Column column) {
    HorizontalTextAlignment textAlignment = null;
    if (column.getAlign() != null) {
      textAlignment = HorizontalTextAlignment.valueOf(column.getAlign().toUpperCase());
    }
    return textAlignment;
  }

  /**
   * Fill grid data
   *
   * @param grid       Grid element
   * @param reportGrid Report grid
   * @param parameters Parameters
   */
  private void setGridData(Grid grid, ReportGrid reportGrid, List<String> fields, ObjectNode parameters) throws AWException {
    // Retrieve columns
    reportGrid.setFields(fields);

    if (grid.isLoadAll() || grid.isEditable() || grid.isMultioperation()) {
      // Get data from parameters
      reportGrid.setData(getGridDataParameters(grid, reportGrid, fields, parameters));
    } else {
      // Get data from query
      reportGrid.setData(getGridDataQuery(grid, reportGrid, fields, parameters));
    }
  }

  /**
   * Set report data from parameters
   *
   * @param reportGrid Report grid
   * @param fields     Fields
   * @param parameters Parameters
   * @return report data
   */
  private List<List<Object>> getGridDataParameters(Grid grid, ReportGrid reportGrid, List<String> fields, ObjectNode parameters) {
    List<List<Object>> data = new ArrayList<>();
    boolean firstRow = true;
    ArrayNode firstFieldData = JsonNodeFactory.instance.arrayNode();
    if (!fields.isEmpty()) {
      JsonNode firstField = parameters.get(fields.get(0));
      if (firstField.isArray()) {
        firstFieldData = (ArrayNode) firstField;
      }
    }

    // Add _style_ to fields if it exists
    if (parameters.has(AweConstants.DATALIST_STYLE_FIELD) || grid.isShowTotals()) {
      addStyleField(reportGrid, fields);
    }

    // Retrieve data from parameters
    for (int rowIndex = 0, rowTotal = firstFieldData.size(); rowIndex < rowTotal; rowIndex++) {
      List<Object> rowData = new ArrayList<>();
      data.add(rowData);
      for (String field : fields) {
        ArrayNode fieldData = (ArrayNode) Optional.ofNullable(parameters.get(field)).orElse(JsonNodeFactory.instance.arrayNode());
        ColumnType type = storeParameterRowData(fieldData.get(rowIndex), rowData);

        // Update column type on first row
        if (firstRow) {
          updateColumnType(reportGrid, field, type);
        }
      }
      firstRow = false;
    }

    // Retrieve footer row if exists
    if (grid.isShowTotals()) {
      ObjectNode totalRows = (ObjectNode) parameters.get(grid.getId() + dataSuffix).get("footer");
      totalRows.set(AweConstants.DATALIST_STYLE_FIELD, JsonNodeFactory.instance.objectNode().put(AweConstants.JSON_VALUE_PARAMETER, TotalizeStyleType.TOTAL.toString()));
      List<Object> footerData = new ArrayList<>();
      data.add(footerData);
      fields.forEach(field -> storeParameterRowData(totalRows.get(field), footerData));
    }

    return data;
  }

  /**
   * Set report data from a query
   *
   * @param grid       Grid
   * @param reportGrid Report grid
   * @param fields     Fields
   * @param parameters Parameter
   * @return report data
   */
  private List<List<Object>> getGridDataQuery(Grid grid, ReportGrid reportGrid, List<String> fields, ObjectNode parameters) throws AWException {
    List<List<Object>> data = new ArrayList<>();
    boolean firstRow = true;

    // Set sort and direction
    addSortData(grid, parameters);

    // Launch query and get data
    DataList queryData = queryService.launchQuery(grid.getTargetAction(), parameters).getDataList();

    // Add _style_ to fields if it exists
    if (!queryData.getRows().isEmpty()) {
      Map<String, CellData> row = queryData.getRows().get(0);
      if (row.containsKey(AweConstants.DATALIST_STYLE_FIELD)) {
        addStyleField(reportGrid, fields);
      }
    }

    // Retrieve data from parameters
    for (Map<String, CellData> row : queryData.getRows()) {
      List<Object> rowData = new ArrayList<>();
      data.add(rowData);
      for (String field : fields) {
        CellData cell = row.get(field);
        ColumnType type = addRowData(cell, rowData);

        // Update column type on first row
        if (firstRow) {
          updateColumnType(reportGrid, field, type);
        }
      }
      firstRow = false;
    }

    return data;
  }

  /**
   * Add style field to report grid
   *
   * @param reportGrid Report grid
   * @param fields     Fields
   */
  private void addStyleField(ReportGrid reportGrid, List<String> fields) {
    reportGrid.setStyleColumn(new ReportColumn("style-field")
      .setField(AweConstants.DATALIST_STYLE_FIELD)
      .setType(ColumnType.STRING));
    fields.add(AweConstants.DATALIST_STYLE_FIELD);
  }

  /**
   * Add row data and retrieve data type
   *
   * @param cell    Cell data
   * @param rowData Row data
   * @return Column type
   */
  private ColumnType addRowData(CellData cell, List<Object> rowData) {
    ColumnType type;
    CellData safeCell = Optional.ofNullable(cell).orElse(new CellData());
    switch (safeCell.getType()) {
      case OBJECT:
      case JSON:
        rowData.add(new DataBean((ObjectNode) safeCell.getObjectValue()));
        type = ColumnType.OBJECT;
        break;
      case NULL:
        rowData.add(new DataBean(JsonNodeFactory.instance.objectNode()
          .put(AweConstants.JSON_VALUE_PARAMETER, "")
          .put(AweConstants.JSON_LABEL_PARAMETER, "")
        ));
        type = ColumnType.STRING;
        break;
      case FLOAT:
      case DOUBLE:
      case DECIMAL:
        rowData.add(new DataBean(JsonNodeFactory.instance.objectNode()
          .put(AweConstants.JSON_VALUE_PARAMETER, safeCell.getDoubleValue())
          .put(AweConstants.JSON_LABEL_PARAMETER, safeCell.getStringValue())
        ));
        type = ColumnType.DOUBLE;
        break;
      case INTEGER:
      case LONG:
        rowData.add(new DataBean(JsonNodeFactory.instance.objectNode()
          .put(AweConstants.JSON_VALUE_PARAMETER, safeCell.getIntegerValue())
          .put(AweConstants.JSON_LABEL_PARAMETER, safeCell.getStringValue())
        ));
        type = ColumnType.INTEGER;
        break;
      default:
        rowData.add(new DataBean(JsonNodeFactory.instance.objectNode()
          .put(AweConstants.JSON_VALUE_PARAMETER, safeCell.getStringValue())
          .put(AweConstants.JSON_LABEL_PARAMETER, safeCell.getStringValue())
        ));
        type = ColumnType.valueOf(safeCell.getType().toString());
        break;
    }

    return type;
  }

  /**
   * Update column type on data retrieval
   *
   * @param reportGrid Report grid
   * @param field      Field to update
   * @param type       Type
   */
  private void updateColumnType(ReportGrid reportGrid, String field, ColumnType type) {
    for (GridHeader gridHeader : reportGrid.getGridHeaders()) {
      if (gridHeader instanceof ReportColumn) {
        updateType((ReportColumn) gridHeader, field, type);
      } else if (gridHeader instanceof ReportHeader) {
        ReportHeader header = (ReportHeader) gridHeader;
        for (ReportColumn column : header.getColumns()) {
          updateType(column, field, type);
        }
      }
    }
  }

  /**
   * Update a column type
   *
   * @param column Report column
   * @param field  Field to update
   * @param type   Type
   */
  private void updateType(ReportColumn column, String field, ColumnType type) {
    if (field.equalsIgnoreCase(column.getField()) && column.getType() == null) {
      column.setType(type);
    }
  }

  /**
   * Store parameter data
   *
   * @param data Parameter data
   * @param row  Row
   */
  private ColumnType storeParameterRowData(JsonNode data, List<Object> row) {
    ColumnType type;
    JsonNode emptyData = JsonNodeFactory.instance.objectNode()
      .put(AweConstants.JSON_LABEL_PARAMETER, "")
      .putNull(AweConstants.JSON_VALUE_PARAMETER);
    JsonNode safeData = Optional.ofNullable(data).orElse(emptyData);
    JsonNode value = safeData.get(AweConstants.JSON_VALUE_PARAMETER);
    if (value.isDouble()) {
      type = ColumnType.DOUBLE;
    } else if (value.isFloat()) {
      type = ColumnType.FLOAT;
    } else if (value.isLong()) {
      type = ColumnType.LONG;
    } else if (value.isInt()) {
      type = ColumnType.INTEGER;
    } else if (value.isBoolean()) {
      type = ColumnType.BOOLEAN;
    } else if (value.isBigDecimal()) {
      type = ColumnType.BIGDECIMAL;
    } else if (value.isBigInteger()) {
      type = ColumnType.BIGINTEGER;
    } else {
      type = ColumnType.STRING;
    }
    row.add(new DataBean((ObjectNode) safeData));
    return type;
  }

  /**
   * Get element design for chart
   *
   * @param printBean  Print bean
   * @param element    Element
   * @param parameters Parameters
   */
  private void getElementDesign(PrintBean printBean, Chart element, ObjectNode parameters) {
    Layout layout = (Layout) printBean.getDetail();

    // Add chart element to layout
    Image chartElement = new Image(element.getId());
    Layout chartLayout = Elements.layout(element.getId() + "-layout", LayoutType.VERTICAL);
    chartLayout.addElement(chartElement)
      .getStyle()
      .setTopPadding(TOP_PADDING_SMALL);
    layout.addElement(chartLayout);

    // Get chart data
    String image = parameters.get(element.getId()).get(AweConstants.JSON_IMAGE_PARAMETER).asText();

    // Configure chart
    chartElement.setSVGImage(image)
      .setSize(Image.Size.FULL_SIZE.getSize())
      .setScale(AweConstants.SVG_SCALE)
      .getStyle()
      .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
      .setHorizontalImageAlignment(HorizontalImageAlignment.CENTER);

    // Set as landscape if defined
    if (PageOrientation.LANDSCAPE.equals(printBean.getOrientation())) {
      chartElement
        .setSize(Image.Size.FULL_SIZE_LANDSCAPE.getSize())
        .setScale(AweConstants.SVG_SCALE_LANDSCAPE);
    }
  }

  /**
   * Get element design for standard element (new page)
   *
   * @param printBean Print bean
   * @param element   Element
   * @return Print bean
   */
  private PrintBean getElementDesign(PrintBean printBean, Element element) {
    Layout layout = (Layout) printBean.getDetail();
    layout.addElement(new PageBreak(element.getId()));

    // Return print bean
    return printBean;
  }
}