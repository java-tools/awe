package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.FilterColumn;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.test.bean.Planet;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class DataListTest extends TestUtil {

  /**
   * Test of check public addresses
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
   * @throws Exception Test error
   */
  @Test
  public void testBuilderFilterListDataList() throws Exception {
    // Prepare
    DataListBuilder builder = new DataListBuilder();
    builder.addColumn("test1", Arrays.asList("value1", "value2", "asdzz3", "val", "aaaaa5", "tutu"), "STRING");
    builder.addColumn("test2", Arrays.asList("asasa1", "value2", "asdzz3", "val", "value5"), "STRING");
    builder.addColumn("test3", Arrays.asList("asasa1", "value2", "value3", "val", "aaaaa5", "lere"), "STRING");
    builder.filter(Arrays.asList(new FilterColumn("test2", "as")));

    // Run
    DataList output = builder.build();

    // Assert
    assertEquals(2, output.getRows().size());
    assertEquals(2L, output.getRecords());
  }

  /**
   * Test of check data list utilities
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
   * Test of check data list utilities
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
}