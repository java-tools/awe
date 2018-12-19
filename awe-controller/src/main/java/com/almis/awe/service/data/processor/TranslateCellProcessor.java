/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweContextAware;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
import com.almis.awe.model.entities.queries.Field;
import com.almis.awe.model.entities.queries.OutputField;

/**
 * TransformCellProcessor class
 */
public class TranslateCellProcessor implements CellProcessor, AweContextAware {
  private OutputField field;
  private EnumeratedGroup translateEnumerated;
  private AweElements elements;

  /**
   * Set transform field (needs AweElements)
   * @param field Output field
   * @return TranslateCellProcessor
   * @throws com.almis.awe.exception.AWException
   */
  public TranslateCellProcessor setField(OutputField field) throws AWException {
    this.field = field;
    translateEnumerated = new EnumeratedGroup(getElements().getEnumerated(field.getTranslate()));
    return this;
  }

  /**
   * Set Awe Elements (Set in first place always)
   * @return
   */
  public TranslateCellProcessor setElements(AweElements elements) {
    this.elements = elements;
    return this;
  }

  /**
   * Retrieve Awe Elements
   * @return
   */
  private AweElements getElements() {
    if (elements == null) {
      throw new NullPointerException("Awe Elements not defined");
    }
    return elements;
  }

  /**
   * Retrieve column identifier
   * @return
   */
  public String getColumnIdentifier() {
    String identifier = null;
    if (field.getAlias() != null) {
      return field.getAlias();
    } else if (field instanceof Field) {
      Field fieldObject = (Field) field;
      identifier = fieldObject.getId();
    }
    return identifier;
  }

  /**
   * Process cell
   * @param cell
   * @throws com.almis.awe.exception.AWException
   */
  public CellData process(CellData cell) throws AWException {
    // Get value
    String value = cell.getStringValue();

    // Get translated label
    String label = translateEnumerated.findLabel(value);
    cell.setValue(getElements().getLocale(label));

    // Store computed in row
    return cell;
  }
}
