package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import com.almis.awe.model.entities.screen.component.widget.WidgetParameter;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class WidgetTest extends TestUtil {

  /**
   * Test of cell data null
   * @throws Exception Test error
   */
  @Test
  public void testWidgetParameter() throws Exception {
    // Prepare
    WidgetParameter parameter = (WidgetParameter) new WidgetParameter()
      .setValue("33")
      .setName("number");
    WidgetParameter booleanParameter = (WidgetParameter) parameter.copy()
      .setValue("true")
      .setType("BOOLEAN");
    WidgetParameter nullParameter = (WidgetParameter) parameter.copy()
      .setName("null");

    DependencyElement dependencyElement = new DependencyElement();

    WidgetParameter arrayParameter = WidgetParameter.builder().build();
    List<Element> elementList = Arrays.asList(parameter, booleanParameter, dependencyElement, nullParameter);
    arrayParameter
      .setElementList(elementList)
      .setType("ARRAY");

    WidgetParameter objectParameter = (WidgetParameter) WidgetParameter.builder().build()
      .setElementList(elementList)
      .setType("OBJECT");

    // Run
    assertEquals("33", ((WidgetParameter) parameter.setType("LABEL")).getParameterValue());
    assertEquals("33", ((WidgetParameter) parameter.setType("STRING")).getParameterValue());
    assertEquals(33, ((WidgetParameter) parameter.setType("INTEGER")).getParameterValue());
    assertEquals(33L, ((WidgetParameter) parameter.setType("LONG")).getParameterValue());
    assertEquals(33.0F, ((WidgetParameter) parameter.setType("FLOAT")).getParameterValue());
    assertEquals(33.0D, ((WidgetParameter) parameter.setType("DOUBLE")).getParameterValue());
    assertEquals(true, booleanParameter.getParameterValue());
    assertNull(nullParameter.getParameterValue());
    assertNull(((WidgetParameter) parameter.setType("NULL")).getParameterValue());
    assertEquals(elementList.size() - 1, ((List) arrayParameter.getParameterValue()).size());
    assertEquals(elementList.size() - 2, ((Map) objectParameter.getParameterValue()).size());
    assertEquals(AweConstants.TEMPLATE_EMPTY, objectParameter.getTemplate());
    assertEquals(AweConstants.NO_TAG, objectParameter.getComponentTag());
  }
}