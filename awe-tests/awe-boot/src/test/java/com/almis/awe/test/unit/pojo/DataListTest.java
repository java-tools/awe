package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
}