package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.entities.queries.Table;
import com.almis.awe.test.unit.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class TableTest extends TestUtil {

  /**
   * Test of cell data null
   * @throws Exception Test error
   */
  @Test
  public void testTable() throws Exception {
    // Prepare
    Table table = new Table()
      .setId("tabla")
      .setAlias("alias")
      .setSchema("schema");

    Table queryTable = Table.builder().build()
      .setQuery("query")
      .setAlias("alias");

     Table copiedQueryTable = queryTable.copy()
       .setAlias(null);

    Table copiedTable = table.copy()
      .setAlias(null);

    Table copiedTableWithoutSchema = copiedTable.copy()
      .setSchema(null);

    // Run
    assertEquals("schema.tabla alias", table.toString());
    assertEquals("([SUBQUERY] query) as alias", queryTable.toString());
    assertEquals("([SUBQUERY] query)", copiedQueryTable.toString());
    assertEquals("schema.tabla", copiedTable.toString());
    assertEquals("tabla", copiedTableWithoutSchema.toString());
  }
}