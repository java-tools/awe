package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class CellDataTest extends TestUtil {

  // Prepare
  Double doubleValue = 23.0D;
  Integer integerValue = 23;
  Long longValue = 23L;
  Float floatValue = 23.0F;
  BigDecimal decimal = new BigDecimal(23);
  BigDecimal bigDecimal = new BigDecimal(234234223);
  Date dateValue = new GregorianCalendar(1978, Calendar.OCTOBER, 23).getTime();
  Date bigDateValue = new GregorianCalendar(2022, Calendar.JANUARY, 11).getTime();
  Boolean booleanValue = true;
  Boolean nullBoolean = null;
  Integer nullInteger = null;
  String textValue = "tutu";
  String anotherTextValue = "lala";
  ArrayNode jsonValue = JsonNodeFactory.instance.arrayNode()
    .add(longValue)
    .add(integerValue)
    .add(textValue);
  ObjectNode anotherJsonValue = JsonNodeFactory.instance.objectNode()
    .put("long", longValue)
    .put("integer", integerValue)
    .put("text", textValue)
    .putPOJO("pojo", new CellData(longValue));

  /**
   * Test of cell data null
   * @throws Exception Test error
   */
  @Test
  public void testCellDataNull() throws Exception {
    // Run
    CellData decimalData = new CellData(decimal);
    CellData nullData = new CellData();
    CellData anotherNullData = nullData.copy();
    CellData nullDataBoolean = new CellData(booleanValue);
    nullDataBoolean.setValue(nullBoolean);
    CellData nullDataInteger = new CellData(integerValue);
    nullDataInteger.setValue(nullInteger);

    // Assert
    assertTrue(nullData.equals(anotherNullData));
    assertNull(nullData.getValue());
    assertNull(nullData.getObjectValue());
    assertNull(nullData.getDoubleValue());
    assertNull(nullData.getIntegerValue());
    assertEquals(nullData, SerializationUtils.deserialize(SerializationUtils.serialize(nullData)));
    assertEquals(0, nullData.compareTo(anotherNullData));
    assertEquals(0, nullDataInteger.compareTo(nullDataBoolean));
    assertTrue(nullDataInteger.compareTo(decimalData) < 0);

    assertTrue(nullData.isEmpty());
    assertTrue(nullDataInteger.isEmpty());
  }

  /**
   * Test of cell data
   * @throws Exception Test error
   */
  @Test
  public void testCellDataBigDecimal() throws Exception {
    // Run
    CellData decimalData = new CellData(decimal);
    CellData bigDecimalData = new CellData(bigDecimal);

    // Assert
    assertFalse(decimalData.equals(bigDecimalData));
    assertEquals(decimal, decimalData.getValue());
    assertEquals(decimal, decimalData.getObjectValue());
    assertEquals(doubleValue, decimalData.getDoubleValue(), 0.00001);
    assertEquals(integerValue, decimalData.getIntegerValue());
    assertEquals(decimalData, SerializationUtils.deserialize(SerializationUtils.serialize(decimalData)));
    assertTrue(decimalData.compareTo(bigDecimalData) < 0);
    assertFalse(decimalData.isEmpty());
  }

  /**
   * Test of cell data float
   * @throws Exception Test error
   */
  @Test
  public void testCellDataFloat() throws Exception {
    // Run
    CellData floatData = new CellData(floatValue);
    CellData anotherFloatData = new CellData(decimal.floatValue());
    CellData nullData = new CellData();

    // Assert
    assertTrue(floatData.equals(anotherFloatData));
    assertEquals(floatValue, floatData.getValue());
    assertEquals(floatValue, floatData.getObjectValue());
    assertEquals(doubleValue, floatData.getDoubleValue(), 0.00001);
    assertEquals(integerValue, floatData.getIntegerValue());
    assertEquals(floatData, SerializationUtils.deserialize(SerializationUtils.serialize(floatData)));
    assertEquals(0, floatData.compareTo(anotherFloatData));
    assertTrue(floatData.compareTo(nullData) > 0);
    assertFalse(floatData.isEmpty());
  }

  /**
   * Test of cell data long
   * @throws Exception Test error
   */
  @Test
  public void testCellDataLong() throws Exception {
    // Run
    CellData longData = new CellData(longValue);
    CellData bigLongData = new CellData(bigDecimal.longValue());

    // Assert
    assertFalse(longData.equals(bigLongData));
    assertEquals(longValue, longData.getValue());
    assertEquals(longValue, longData.getObjectValue());
    assertEquals(doubleValue, longData.getDoubleValue(), 0.00001);
    assertEquals(integerValue, longData.getIntegerValue());
    assertEquals(longData, SerializationUtils.deserialize(SerializationUtils.serialize(longData)));
    assertTrue(bigLongData.compareTo(longData) > 0);
    assertFalse(bigLongData.isEmpty());
  }

  /**
   * Test of cell data double
   * @throws Exception Test error
   */
  @Test
  public void testCellDataDouble() throws Exception {
    // Run
    CellData doubleData = new CellData(doubleValue);
    CellData bigDoubleData = new CellData(bigDecimal.doubleValue());

    // Assert
    assertFalse(doubleData.equals(bigDoubleData));
    assertEquals(doubleValue, doubleData.getValue());
    assertEquals(doubleValue, doubleData.getObjectValue());
    assertEquals(doubleValue, doubleData.getDoubleValue(), 0.00001);
    assertEquals(integerValue, doubleData.getIntegerValue());
    assertEquals(doubleData, SerializationUtils.deserialize(SerializationUtils.serialize(doubleData)));
    assertTrue(doubleData.compareTo(bigDoubleData) < 0);
    assertFalse(doubleData.isEmpty());
  }

  /**
   * Test of cell data integer
   * @throws Exception Test error
   */
  @Test
  public void testCellDataInteger() throws Exception {
    // Run
    CellData integerData = new CellData(integerValue);
    CellData bigIntegerData = new CellData(bigDecimal.intValue());
    CellData nullData = new CellData();

    // Assert
    assertFalse(integerData.equals(bigIntegerData));
    assertEquals(integerValue, integerData.getValue());
    assertEquals(integerValue, integerData.getObjectValue());
    assertEquals(doubleValue, integerData.getDoubleValue(), 0.00001);
    assertEquals(integerValue, integerData.getIntegerValue());
    assertEquals(integerData, SerializationUtils.deserialize(SerializationUtils.serialize(integerData)));
    assertTrue(bigIntegerData.compareTo(integerData) > 0);
    assertTrue(nullData.compareTo(integerData) < 0);
    assertFalse(bigIntegerData.isEmpty());
    assertFalse(bigIntegerData.compareTo(new CellData(textValue)) > 0);
  }

  /**
   * Test of cell data string
   * @throws Exception Test error
   */
  @Test
  public void testCellDataString() throws Exception {
    // Run
    CellData textData = new CellData(textValue);
    CellData anotherTextData = new CellData(anotherTextValue);

    // Assert
    assertFalse(textData.equals(anotherTextData));
    assertEquals(textValue, textData.getValue());
    assertEquals(textValue, textData.getObjectValue());
    assertEquals(textValue, textData.getStringValue());
    assertNull(textData.getDoubleValue());
    assertNull(textData.getIntegerValue());
    assertEquals(textData, SerializationUtils.deserialize(SerializationUtils.serialize(textData)));
    assertTrue(textData.compareTo(anotherTextData) > 0);
    assertFalse(textData.isEmpty());
  }

  /**
   * Test of cell data string
   * @throws Exception Test error
   */
  @Test
  public void testCellDataJson() throws Exception {
    // Run
    CellData jsonData = new CellData(jsonValue);
    CellData anotherJsonData = new CellData(anotherJsonValue);

    // Assert
    assertFalse(jsonData.equals(anotherJsonData));
    assertEquals(jsonValue, jsonData.getValue());
    assertEquals(jsonValue, jsonData.getObjectValue());
    assertEquals(jsonValue.toString(), jsonData.getStringValue());
    assertNull(jsonData.getDoubleValue());
    assertNull(jsonData.getIntegerValue());
    assertEquals(0, jsonData.compareTo((CellData) SerializationUtils.deserialize(SerializationUtils.serialize(jsonData))));
    assertEquals(0, anotherJsonData.compareTo((CellData) SerializationUtils.deserialize(SerializationUtils.serialize(anotherJsonData))));
    assertTrue(jsonData.compareTo(anotherJsonData) < 0);
    assertFalse(jsonData.isEmpty());
  }

  /**
   * Test of cell data date
   * @throws Exception Test error
   */
  @Test
  public void testCellDataDate() throws Exception {
    // Run
    CellData dateData = new CellData(dateValue);
    CellData bigDateData = new CellData(bigDateValue);
    CellData dateTextData = new CellData(DateUtil.jsonDate(dateValue));
    CellData anotherDateTextData = new CellData(DateUtil.dat2WebTimestamp(dateValue));

    // Assert
    assertTrue(dateData.equals(dateTextData));
    assertEquals(DateUtil.jsonDate(dateValue), dateData.getValue());
    assertEquals(dateValue, dateData.getObjectValue());
    assertEquals(DateUtil.dat2WebTimestamp(dateValue), dateData.getStringValue());
    assertEquals(dateValue, dateData.getDateValue());
    assertNull(dateData.getDoubleValue());
    assertNull(dateData.getIntegerValue());
    assertEquals(dateData, SerializationUtils.deserialize(SerializationUtils.serialize(dateData)));
    assertEquals(dateTextData, SerializationUtils.deserialize(SerializationUtils.serialize(dateTextData)));

    assertEquals(DateUtil.jsonDate(dateValue), dateTextData.getValue());
    assertEquals(dateValue, dateTextData.getObjectValue());
    assertEquals(DateUtil.dat2WebTimestamp(dateValue), dateTextData.getStringValue());
    assertEquals(dateValue, dateTextData.getDateValue());
    assertEquals(dateValue, anotherDateTextData.getDateValue());
    assertNull(dateTextData.getDoubleValue());
    assertNull(dateTextData.getIntegerValue());

    assertTrue(dateData.compareTo(bigDateData) < 0);
    assertFalse(dateData.isEmpty());
  }

  /**
   * Test of cell data date
   * @throws Exception Test error
   */
  @Test
  public void testCellDataCellData() throws Exception {
    // Run
    CellData dateData = new CellData(dateValue);
    CellData dateDataCopy = new CellData(dateData);

    // Assert
    assertTrue(dateData.equals(dateDataCopy));
    assertEquals(DateUtil.jsonDate(dateValue), dateDataCopy.getValue());
    assertEquals(dateValue, dateDataCopy.getObjectValue());
    assertEquals(DateUtil.dat2WebTimestamp(dateValue), dateDataCopy.getStringValue());
    assertEquals(dateValue, dateDataCopy.getDateValue());
    assertNull(dateDataCopy.getDoubleValue());
    assertNull(dateDataCopy.getIntegerValue());
    assertEquals(dateDataCopy, SerializationUtils.deserialize(SerializationUtils.serialize(dateDataCopy)));
    assertEquals(0, dateData.compareTo(dateDataCopy));
  }

  /**
   * Test of cell data integer
   * @throws Exception Test error
   */
  @Test
  public void testCellDataIntegerTransform() throws Exception {
    // Run
    CellData integerData = new CellData(integerValue);
    integerData.setSendStringValue(true);
    integerData.setStringValue(integerValue.toString());

    // Assert
    assertEquals(integerValue.toString(), integerData.getValue());
    assertEquals(integerValue, integerData.getObjectValue());
    assertEquals(doubleValue, integerData.getDoubleValue(), 0.00001);
    assertEquals(integerValue, integerData.getIntegerValue());
    assertEquals(integerData, SerializationUtils.deserialize(SerializationUtils.serialize(integerData)));
    assertFalse(integerData.isEmpty());
  }

  /**
   * Test of cell data double
   * @throws Exception Test error
   */
  @Test
  public void testCellDataObject() throws Exception {
    // Run
    Column column = Column.builder()
      .name("campo1")
      .charLength(20)
      .build();
    CellData columnData = new CellData(column);
    CellData anotherColumnData = columnData.copy();

    // Assert
    assertTrue(columnData.equals(anotherColumnData));
    assertSame(column, columnData.getValue());
    assertSame(column, columnData.getObjectValue());
    assertNull(columnData.getDoubleValue());
    assertNull(columnData.getIntegerValue());
    assertEquals(0, columnData.compareTo((CellData) SerializationUtils.deserialize(SerializationUtils.serialize(columnData))));
    assertEquals(0, columnData.compareTo(anotherColumnData));
    assertFalse(columnData.isEmpty());
  }

}