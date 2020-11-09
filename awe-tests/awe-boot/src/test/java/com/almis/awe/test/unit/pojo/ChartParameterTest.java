package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.entities.screen.component.chart.ChartParameter;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * DataList, DataListUtil and DataListBuilder tests
 *
 * @author pgarcia
 */
public class ChartParameterTest extends TestUtil {

  /**
   * Test of cell data null
   *
   * @throws Exception Test error
   */
  @Test
  public void testChartParameter() throws Exception {
    // Prepare
    Map<String, Object> model = new HashMap<>();
    ChartParameter parameter = (ChartParameter) new ChartParameter()
      .setValue("33")
      .setType(null);

    List<String> values = new ArrayList<>();
    values.add("1");
    values.add("3");
    values.add("4");
    values.add("4");
    Map<String, Object> map = new HashMap<>();
    model.put("list", values);
    model.put("object", map);

    parameter.addParameterModel(model);
    parameter.addParameterModel(null);

    // Run
    assertEquals("33", parameter.getParameterValue(model));
    assertEquals("33", ((ChartParameter) parameter.setType("STRING")).getParameterValue(model));
    assertEquals(33, ((ChartParameter) parameter.setType("INTEGER")).getParameterValue(model));
    assertEquals(33L, ((ChartParameter) parameter.setType("LONG")).getParameterValue(model));
    assertEquals(33.0F, ((ChartParameter) parameter.setType("FLOAT")).getParameterValue(model));
    assertEquals(33.0D, ((ChartParameter) parameter.setType("DOUBLE")).getParameterValue(model));
    assertTrue((Boolean) ((ChartParameter) parameter.setValue("true").setType("BOOLEAN")).getParameterValue(model));
    assertFalse((Boolean) ((ChartParameter) parameter.setValue("false").setType("BOOLEAN")).getParameterValue(model));
    assertNull(((ChartParameter) parameter.setType("NULL")).getParameterValue(model));
    assertEquals(values, ((ChartParameter) parameter.setName("list").setType("ARRAY")).getParameterValue(model));
    assertEquals(map, ((ChartParameter) parameter.setName("object").setType("OBJECT")).getParameterValue(model));

    assertEquals(values, ((ChartParameter) parameter.setName("list").setParameterList(Arrays.asList(
      new ChartParameter().setValue("1"),
      new ChartParameter().setValue("3"),
      new ChartParameter().setValue("4"),
      new ChartParameter().setValue("4")
    )).setType("ARRAY")).getParameterValue(model));
    assertEquals(map, ((ChartParameter) parameter.setName("object").setType("OBJECT")).getParameterValue(model));

    // Test add parameter model with null model
    parameter.addParameterModel(null);
  }
}