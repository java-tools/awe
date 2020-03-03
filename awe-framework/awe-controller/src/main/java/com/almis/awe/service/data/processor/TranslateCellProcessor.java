package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweContextAware;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
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
   *
   * @param field Output field
   * @return TranslateCellProcessor
   * @throws AWException AWE exception
   */
  public TranslateCellProcessor setField(OutputField field) throws AWException {
    this.field = field;
    translateEnumerated = getElements().getEnumerated(field.getTranslate()).copy();
    return this;
  }

  /**
   * Set Awe Elements (Set in first place always)
   * @param elements awe elements
   * @return translate cell processor
   */
  public TranslateCellProcessor setElements(AweElements elements) {
    this.elements = elements;
    return this;
  }

  /**
   * Retrieve Awe Elements
   *
   * @return AWE elements
   */
  private AweElements getElements() {
    if (elements == null) {
      throw new NullPointerException("Awe Elements not defined");
    }
    return elements;
  }

  /**
   * Retrieve column identifier
   *
   * @return column identifier
   */
  public String getColumnIdentifier() {
    return field.getIdentifier();
  }

  /**
   * Process cell
   *
   * @param cell cell data
   * @throws AWException AWE exception
   */
  public CellData process(CellData cell) throws AWException {
    // Get value
    String value = cell.getStringValue();

    // Get translated label
    String label = translateEnumerated.findLabel(value);
    cell.setValue(getElements().getLocaleWithLanguage(label, getElements().getLanguage()));

    // Store computed in row
    return cell;
  }
}
