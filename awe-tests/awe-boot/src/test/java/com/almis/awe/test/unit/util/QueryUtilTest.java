package com.almis.awe.test.unit.util;

import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.test.unit.TestUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author pgarcia
 */
public class QueryUtilTest extends TestUtil {

  @InjectMocks
  private QueryUtil queryUtil;

  /**
   * Test null get parameters
   */
  @Test
  public void testNullGetParameters() {
    assertThrows(NullPointerException.class,() -> queryUtil.getParameters(null, null, null, null));
  }

  /**
   * Test null get parameters
   */
  @Test
  public void testNullVariableIsList() {
    assertThrows(NullPointerException.class,() ->     queryUtil.variableIsList(null, null));
  }
}