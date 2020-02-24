package com.almis.awe.test.unit.pojo;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.FilterColumn;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.model.type.CellDataType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.test.bean.NoAttributes;
import com.almis.awe.test.bean.Planet;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * DataList, DataListUtil and DataListBuilder tests
 *
 * @author pgarcia
 */
public class DataListTest extends TestUtil {

  /**
   * Test of check public addresses
   *
   * @throws Exception Test error
   */
  @Test
  public void testBuilderFilterDataList() throws Exception {
    // Prepare
    DataListBuilder builder = new DataListBuilder();
    builder.addColumn("test1", Arrays.asList("value1", "value2", "asdzz3", "val", "aaaaa5", "tutu"), "STRING");
    builder.addColumn("test2", Arrays.asList("asasa1", "value2", "asdzz3", "val", "value5"), "STRING");
    builder.addColumn("test3", Arrays.asList("asasa1", "value2", "value3", "val", "aaaaa5", "lere"), "STRING");
    builder.filter("test2", "val");

    // Run
    DataList output = builder.build();

    // Assert
    assertEquals(3, output.getRows().size());
    assertEquals(3L, output.getRecords());
  }

  /**
   * Test of check public addresses
   *
   * @throws Exception Test error
   */
  @Test
  public void testBuilderFilterListDataList() throws Exception {
    // Prepare
    DataListBuilder builder = new DataListBuilder();
    builder.addColumn("test1", Arrays.asList("value1", "value2", "asdzz3", "val", "aaaaa5", "tutu"), "STRING");
    builder.addColumn("test2", Arrays.asList("asasa1", "value2", "asdzz3", "val", "value5"), "STRING");
    builder.addColumn("test3", Arrays.asList("asasa1", "value2", "value3", "val", "aaaaa5", "lere"), "STRING");
    builder.filter(Collections.singletonList(new FilterColumn("test2", "as")));

    // Run
    DataList output = builder.build();

    // Assert
    assertEquals(2, output.getRows().size());
    assertEquals(2L, output.getRecords());
  }

  /**
   * Test of check data list utilities
   *
   * @throws Exception Test error
   */
  @Test
  public void testDataListUtilFilterDataList() throws Exception {
    // Prepare
    DataListBuilder builder = new DataListBuilder();
    builder.addColumn("test1", Arrays.asList("value1", "value2", "asdzz3", "val", "aaaaa5", "tutu"), "STRING");
    builder.addColumn("test2", Arrays.asList("asasa1", "value2", "asdzz3", "val", "value5"), "STRING");
    builder.addColumn("test3", Arrays.asList("asasa1", "value2", "value3", "val", "aaaaa5", "lere"), "STRING");

    // Run
    DataList output = builder.build();
    DataListUtil.filter(output, new FilterColumn("test2", "value2"), new FilterColumn("test1", "val"));

    // Assert
    assertEquals(2, output.getRows().size());
    assertEquals(2L, output.getRecords());
  }

  /**
   * Test of sort data list of integers
   * with null values at FIRST and DESC sort
   *
   * @throws Exception Test error
   */
  @Test
  public void testSortIntegerDescNullsFirst() throws Exception {
    // Prepare datalist
    DataList dataList = new DataListBuilder().addColumn("col1", Arrays.asList(1, 2, null, null, 5), CellDataType.INTEGER.toValue()).build();
    // Prepare sort column list
    List<SortColumn> sortColumnList = Collections.singletonList(new SortColumn("col1", "desc"));
    // Run
    DataListUtil.sort(dataList, sortColumnList, true);
    // Assert
    assertNull(dataList.getRows().get(0).get("col1").getObjectValue());
    assertEquals(1, dataList.getRows().get(dataList.getRows().size()-1).get("col1").getObjectValue());
  }

  /**
   * Test of sort data list of integers
   * with null values at LAST and ASC sort
   *
   * @throws Exception Test error
   */
  @Test
  public void testSortIntegerAscNullsLast() throws Exception {
    // Prepare datalist
    DataList dataList = new DataListBuilder().addColumn("col1", Arrays.asList(1, 2, null, null, 5), CellDataType.INTEGER.toValue()).build();
    // Prepare sort column list
    List<SortColumn> sortColumnList = Collections.singletonList(new SortColumn("col1", "asc"));
    // Run
    DataListUtil.sort(dataList, sortColumnList);
    // Assert
    assertEquals(1, dataList.getRows().get(0).get("col1").getObjectValue());
    assertNull(dataList.getRows().get(dataList.getRows().size()-1).get("col1").getObjectValue());
  }

  /**
   * Test of sort data list of Double
   * with null values at LAST and ASC sort
   *
   * @throws Exception Test error
   */
  @Test
  public void testSortDoubleAscNullsLast() throws Exception {
    // Prepare datalist
    DataList dataList = new DataListBuilder().addColumn("col1", Arrays.asList(1.01, null, 2.223, null, 5.001), CellDataType.DOUBLE.toValue()).build();
    // Prepare sort column list
    List<SortColumn> sortColumnList = Collections.singletonList(new SortColumn("col1", "asc"));
    // Run
    DataListUtil.sort(dataList, sortColumnList);
    // Assert
    assertEquals(1.01, dataList.getRows().get(0).get("col1").getObjectValue());
    assertNull(dataList.getRows().get(dataList.getRows().size()-1).get("col1").getObjectValue());
  }

  /**
   * Test of sort data list of String
   * with null values at LAST and DESC sort
   *
   * @throws Exception Test error
   */
  @Test
  public void testSortStringDescNullsLast() throws Exception {
    // Prepare datalist
    DataList dataList = new DataListBuilder().addColumn("col1", Arrays.asList("Hola", null, "Holaaa", "Adios", null, "Ok"), CellDataType.STRING.toValue()).build();
    // Prepare sort column list
    List<SortColumn> sortColumnList = Collections.singletonList(new SortColumn("col1", "desc"));
    // Run
    DataListUtil.sort(dataList, sortColumnList, false);
    // Assert
    assertEquals("Ok", dataList.getRows().get(0).get("col1").getObjectValue());
    assertNull(dataList.getRows().get(dataList.getRows().size()-1).get("col1").getObjectValue());
  }

  /**
   * Test of sort data list of Date
   * with null values at LAST and DESC sort
   *
   * @throws Exception Test error
   */
  @Test
  public void testSortDateDescNullsLast() throws Exception {
    // Prepare datalist
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    List<Date> dateList = new ArrayList<>();
    dateList.add(formatter.parse("10/08/1985"));
    dateList.add(null);
    dateList.add(formatter.parse("21/11/2019"));
    dateList.add(null);
    dateList.add(formatter.parse("01/01/1999"));

    DataList dataList = new DataListBuilder().addColumn("col1", dateList, CellDataType.DATE.toValue()).build();
    // Prepare sort column list
    List<SortColumn> sortColumnList = Collections.singletonList(new SortColumn("col1", "desc"));
    // Run
    DataListUtil.sort(dataList, sortColumnList, false);
    // Assert
    assertEquals(formatter.parse("21/11/2019"), dataList.getRows().get(0).get("col1").getObjectValue());
    assertNull(dataList.getRows().get(dataList.getRows().size()-1).get("col1").getObjectValue());
  }

  /**
   * Test of check data list utilities
   *
   * @throws Exception Test error
   */
  @Test
  public void testDataListUtilFilterContainsDataList() throws Exception {
    // Prepare
    DataListBuilder builder = new DataListBuilder();
    builder.addColumn("test1", Arrays.asList("value1", "value2", "asdzz3", "val", "aaaaa5", "tutu"), "STRING");
    builder.addColumn("test2", Arrays.asList("asasa1", "value2", "asdzz3", "val", "value5"), "STRING");
    builder.addColumn("test3", Arrays.asList("asasa1", "value2", "value3", "val", "aaaaa5", "lere"), "STRING");

    // Run
    DataList output = builder.build();
    DataListUtil.filterContains(output, new FilterColumn("test2", "val"));

    // Assert
    assertEquals(3, output.getRows().size());
    assertEquals(3L, output.getRecords());
  }

  /**
   * Test datalist conversion to bean list
   *
   * @throws Exception Test error
   */
  @Test
  public void testDataListToBeanList() throws Exception {
    // Prepare
    DataListBuilder builder = new DataListBuilder();
    builder.addColumn("name", Arrays.asList("Earth", "Mars", "Jupiter"), "STRING");
    builder.addColumn("rotationPeriod", Arrays.asList("1d 0h 0m", "1d 0h 37m", "0d 9h 56m"), "STRING");
    builder.addColumn("orbitalPeriod", Arrays.asList("365d 6h 0m", "687d", "12y"), "STRING");
    builder.addColumn("diameter", Arrays.asList(10000, 8121, 123121), "INTEGER");
    builder.addColumn("climate", Arrays.asList("mixed", "dry", "stormy"), "STRING");
    builder.addColumn("gravity", Arrays.asList("9,807 m/s²", "3,711 m/s²", "24,79 m/s²"), "STRING");
    builder.addColumn("terrain", Arrays.asList("solid", "solid", "gas"), "STRING");
    builder.addColumn("population", Arrays.asList(101231012312L, 0L, 0L), "LONG");

    // Run
    DataList output = builder.build();

    // Generate bean list
    List<Planet> planetList = DataListUtil.asBeanList(output, Planet.class);

    // Assert
    assertEquals(3, planetList.size());
    assertEquals("3,711 m/s²", planetList.get(1).getGravity());
  }

  /**
   * Test null datalist conversion to bean list
   *
   * @throws Exception Test error
   */
  @Test(expected = NullPointerException.class)
  public void testNullDataListToBeanList() throws Exception {
    // Prepare
    DataList output = null;

    // Generate bean list
    DataListUtil.asBeanList(output, Planet.class);
  }

  /**
   * Test datalist conversion to bean list
   */
  @Test
  public void testDataListFromBeanList() {
    // Prepare
    List<Planet> planets = new ArrayList<>();
    planets.add(new Planet()
      .setName("Earth")
      .setRotationPeriod("1d 0h 0m")
      .setOrbitalPeriod("365d 6h 0m")
      .setDiameter("10000")
      .setClimate("mixed")
      .setGravity("9,807 m/s²")
      .setTerrain("solid")
      .setPopulation(101231012312L));
    planets.add(new Planet()
      .setName("Mars")
      .setRotationPeriod("1d 0h 37m")
      .setOrbitalPeriod("687d")
      .setDiameter("8121")
      .setClimate("dry")
      .setGravity("3,711 m/s²")
      .setTerrain("solid")
      .setPopulation(0L));
    planets.add(new Planet()
      .setName("Jupiter")
      .setRotationPeriod("0d 9h 56m")
      .setOrbitalPeriod("12y")
      .setDiameter("123121")
      .setClimate("stormy")
      .setGravity("24,79 m/s²")
      .setTerrain("gas")
      .setPopulation(0L));

    // Generate bean list
    DataList planetData = DataListUtil.fromBeanList(planets);

    // Assert
    assertEquals(3, planetData.getRows().size());
    assertEquals("3,711 m/s²", DataListUtil.getData(planetData,1, "gravity"));
  }

  /**
   * Test datalist conversion to bean list
   */
  @Test
  public void testDataListFromBeanListExtraCases() {
    // Prepare
    List<Planet> planets = new ArrayList<>();

    // Generate bean list
    DataList planetData = DataListUtil.fromBeanList(planets);

    // Assert
    assertEquals(0, planetData.getRows().size());

    // Prepare
    List<NoAttributes> nones = new ArrayList<>();
    nones.add(new NoAttributes());
    nones.add(new NoAttributes());
    nones.add(new NoAttributes());
    nones.add(new NoAttributes());

    // Generate bean list
    DataList nonesData = DataListUtil.fromBeanList(nones);

    // Assert
    assertEquals(4, nonesData.getRows().size());
  }

  /**
   * Export as bean list a null datalist
   */
  @Test(expected = NullPointerException.class)
  public void asBeanListNull() throws AWException {
    DataListUtil.asBeanList(null, null);
  }

}