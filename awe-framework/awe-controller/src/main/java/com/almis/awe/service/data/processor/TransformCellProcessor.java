package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.queries.OutputField;
import com.almis.awe.model.type.CellDataType;
import com.almis.awe.model.type.TransformType;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.NumericUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.security.EncodeUtil;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * TransformCellProcessor class
 */
public class TransformCellProcessor implements CellProcessor {
  private OutputField field;

  /**
   * Set transform field
   * @param field Field to be transformed
   * @return Processor
   */
  public TransformCellProcessor setField(OutputField field) {
    this.field = field;
    return this;
  }

  /**
   * Retrieve column identifier
   * @return Column identifier
   */
  public String getColumnIdentifier() {
    return field.getIdentifier();
  }

  /**
   * Process cell
   * @param cell cell to be processed
   * @throws AWException AWE exception
   */
  public CellData process(CellData cell) throws AWException {
    String transformed = cell.getStringValue();
    if (transformed != null && !transformed.isEmpty()) {
      // Transform value if needed
      switch (TransformType.valueOf(field.getTransform())) {
        case DATE:
          transformed = processDate(cell);
          break;
          
        case DATE_MS:
          transformed = processDateMs(cell);
          break;
          
        case TIME:
          transformed = processTime(cell);
          break;
          
        case TIMESTAMP:
          transformed = processTimestamp(cell);
          break;

        case TIMESTAMP_MS:
          transformed = processTimestampMs(cell);
          break;
          
        case JS_DATE:
          transformed = processJavascriptDate(cell);
          break;
          
        case JS_TIMESTAMP:
          transformed = processJavascriptTimestamp(cell);
          break;
          
        case GENERIC_DATE:
          transformed = processGenericDate(cell);
          break;
          
        case DATE_RDB:
          transformed = processRDBDate(cell);
          break;
          
        case NUMBER:
          transformed = processNumber(cell);
          break;

        case NUMBER_PLAIN:
          transformed = processNumberPlain(cell);
          break;

        case BOOLEAN:
          transformed = processBoolean(cell);
          break;

        case TEXT_HTML:
          transformed = StringUtil.toHTMLText(transformed);
          break;

        case TEXT_UNILINE:
          transformed = StringUtil.toUnilineText(transformed);
          break;

        case TEXT_PLAIN:
          transformed = StringUtil.toPlainText(transformed);
          break;

        case MARKDOWN_HTML:
          transformed = StringUtil.evalMarkdown(transformed);
          break;

        case DECRYPT:
          transformed = EncodeUtil.decryptRipEmd160(transformed);
          break;

        case ARRAY:
          cell.setValue(StringUtil.toArrayNode(field.getPattern(), transformed));
          break;

        case LIST:
          cell.setValue(StringUtil.toStringList(field.getPattern(), transformed));
          break;

        default:
          // Do nothing
      }

      if (transformed == null) {
        // Set string value
        cell.setNull();

        // Set noprint
        cell.setPrintable(false);
      } else {
        // Set string value
        cell.setStringValue(transformed);

        // Set as printable
        cell.setPrintable(!field.isNoprint());
      }
    }

    // Store transform in row
    return cell;
  }

  /**
   * Process cell as date
   * @param cell Cell
   * @return Transformation
   */
  private String processDate(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.dat2WebDate(date);
    }
    return transformed;
  }

  /**
   * Process cell as date in milliseconds
   * @param cell Cell
   * @return Transformation
   */
  private String processDateMs(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date.getTime());
      cell.setSendStringValue(false);
      transformed = String.valueOf(date.getTime());
    }
    return transformed;
  }

  /**
   * Process cell as time
   * @param cell Cell
   * @return Transformation
   */
  private String processTime(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.dat2WebTime(date);
    }
    return transformed;
  }

  /**
   * Process cell as timestamp
   * @param cell Cell
   * @return Transformation
   */
  private String processTimestamp(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.dat2WebTimestamp(date);
    }
    return transformed;
  }

  /**
   * Process cell as timestamp with milliseconds
   * @param cell Cell
   * @return Transformation
   */
  private String processTimestampMs(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.dat2WebTimestampMs(date);
    }
    return transformed;
  }

  /**
   * Process cell as javascript date
   * @param cell Cell
   * @return Transformation
   */
  private String processJavascriptDate(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.dat2JsDate(date);
    }
    return transformed;
  }

  /**
   * Process cell as javascript timestamp
   * @param cell Cell
   * @return Transformation
   */
  private String processJavascriptTimestamp(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.dat2JsTimestamp(date);
    }
    return transformed;
  }

  /**
   * Process cell as a generic date
   * @param cell Cell
   * @return Transformation
   */
  private String processGenericDate(@NotNull CellData cell) {
    String transformed = DateUtil.generic2Date(cell.getStringValue(), field.getFormatFrom(), field.getFormatTo());
    cell.setValue(transformed);
    cell.setSendStringValue(true);
    return transformed;
  }

  /**
   * Process cell as rdb date
   * @param cell Cell
   * @return Transformation
   */
  private String processRDBDate(@NotNull CellData cell) {
    String transformed = cell.getStringValue();
    Date date = cell.getDateValue();
    if (date != null) {
      cell.setValue(date);
      cell.setSendStringValue(true);
      transformed = DateUtil.rdbDate2String(date);
    }
    return transformed;
  }


  /**
   * Process cell as number
   * @param cell Cell
   * @return Transformation
   */
  private String processNumber(@NotNull CellData cell)  {
    Double numericValue = Double.parseDouble(cell.getStringValue());
    if (cell.getType().equals(CellDataType.STRING)) {
      cell.setValue(numericValue);
    }
    cell.setSendStringValue(true);
    return NumericUtil.applyPattern(field.getPattern(), numericValue);
  }

  /**
   * Process cell as number
   * @param cell Cell
   * @return Transformation
   */
  private String processNumberPlain(@NotNull CellData cell)  {
    Double numericValue = Double.parseDouble(cell.getStringValue());
    cell.setValue(numericValue);
    return NumericUtil.applyRawPattern(field.getPattern(), numericValue);
  }

  /**
   * Process cell as number
   * @param cell Cell
   * @return Transformation
   */
  private String processBoolean(@NotNull CellData cell)  {
    switch (cell.getType()) {
      case STRING:
        cell.setValue(Boolean.parseBoolean(cell.getStringValue()));
        break;
      case LONG:
      case DECIMAL:
      case FLOAT:
      case DOUBLE:
      case INTEGER:
        cell.setValue(cell.getIntegerValue() != 0);
        break;
      case BOOLEAN:
        break;
      default:
        cell.setValue(cell.getObjectValue() != null);
        break;
    }
    return cell.getStringValue();
  }
}
