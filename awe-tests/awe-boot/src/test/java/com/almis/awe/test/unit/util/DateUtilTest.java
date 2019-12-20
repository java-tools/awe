package com.almis.awe.test.unit.util;

import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author pgarcia
 */
public class DateUtilTest extends TestUtil {

  /**
   * Test of check public addresses
   * @throws Exception Test error
   */
  @Test
  public void testRdb2JavaDate() throws Exception {
    // Prepare
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date expect = simpleDateFormat.parse("23/10/1978");
    Date expect2 = simpleDateFormat.parse("02/01/2018");
    Date expect3 = simpleDateFormat.parse("05/08/2011");
    Date expect4 = simpleDateFormat.parse("31/03/2004");

    // Run
    Date date = DateUtil.rdb2Date("23-OCT-1978");
    Date date2 = DateUtil.rdb2Date("02-JAN-2018");
    Date date3 = DateUtil.rdb2Date("05-AUG-2011");
    Date date4 = DateUtil.rdb2Date("31-MAR-2004");

    // Assert
    assertEquals(expect, date);
    assertEquals(expect2, date2);
    assertEquals(expect3, date3);
    assertEquals(expect4, date4);
  }

  /**
   * Test of check public addresses
   * @throws Exception Test error
   */
  @Test
  public void testDateAndTime() throws Exception {
    // Prepare
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date expect = simpleDateFormat.parse("23/10/1978");
    Date expect2 = simpleDateFormat.parse("02/01/2018");
    Date expect5 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("23/10/1978 20:11:23");

    // Run
    Date date5 = DateUtil.getDateWithTimeFromCriteria("23/10/1978", "20:11:23");
    Date date6 = DateUtil.addTimeToDate(expect, "20:11:23");
    Date date7 = DateUtil.addTimeToDate(expect2, "");
    Date date8 = DateUtil.addTimeToDate(expect2, null);

    // Assert
    assertEquals(expect5, date5);
    assertEquals(expect5, date6);
    assertEquals(expect2, date7);
    assertEquals(expect2, date8);
  }
}