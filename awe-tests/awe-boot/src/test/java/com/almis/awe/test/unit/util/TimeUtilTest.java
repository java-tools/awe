package com.almis.awe.test.unit.util;

import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.TimeUtil;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author pgarcia
 */
public class TimeUtilTest extends TestUtil {

  /**
   * Test hours
   * @throws Exception Test error
   */
  @Test
  public void testExactHoursWithMs() throws Exception {
    // Test and assert
    assertEquals("10h", TimeUtil.formatTime(36000000));
  }

  /**
   * Test hours
   * @throws Exception Test error
   */
  @Test
  public void testExactHoursWithoutMs() throws Exception {
    // Test and assert
    assertEquals("6h 40m", TimeUtil.formatTime(24000000, false));
  }

  /**
   * Test hours
   * @throws Exception Test error
   */
  @Test
  public void testNotExactHoursWithMs() throws Exception {
    // Test and assert
    assertEquals("3h 7m 11s 231ms", TimeUtil.formatTime(11231231));
  }

  /**
   * Test hours
   * @throws Exception Test error
   */
  @Test
  public void testNotExactHoursWithoutMs() throws Exception {
    // Test and assert
    assertEquals("6h 30m 23s", TimeUtil.formatTime(23423423, false));
    assertEquals("1h 5s", TimeUtil.formatTime(3605000, false));
  }

  /**
   * Test exact minutes with ms
   * @throws Exception Test error
   */
  @Test
  public void testExactMinutesWithMs() throws Exception {
    // Test and assert
    assertEquals("6m", TimeUtil.formatTime(360000));
  }

  /**
   * Test minutes
   * @throws Exception Test error
   */
  @Test
  public void testExactMinutesWithoutMs() throws Exception {
    // Test and assert
    assertEquals("4m", TimeUtil.formatTime(240000, false));
  }

  /**
   * Test minutes
   * @throws Exception Test error
   */
  @Test
  public void testMinutesWithMs() throws Exception {
    // Test and assert
    assertEquals("6m 1s 231ms", TimeUtil.formatTime(361231, true));
  }

  /**
   * Test minutes
   * @throws Exception Test error
   */
  @Test
  public void testMinutesWithoutMs() throws Exception {
    // Test and assert
    assertEquals("7m 32s", TimeUtil.formatTime(452324, false));
  }
}