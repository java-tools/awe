package com.almis.awe.test.unit.util;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * @author pgarcia
 */
public class QueryUtilTest extends TestUtil {

  @InjectMocks
  private QueryUtil queryUtil;

  /**
   * Test null get parameters
   */
  @Test(expected = NullPointerException.class)
  public void testNullGetParameters() {
    queryUtil.getParameters(null, null, null, null);
  }

  /**
   * Test null get parameters
   */
  @Test(expected = NullPointerException.class)
  public void testNullVariableIsList() throws AWException {
    queryUtil.variableIsList(null, null);
  }
}