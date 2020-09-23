package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.almis.awe.test.unit.TestUtil;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class CriteriaTest extends TestUtil {

  /**
   * Test of Criteria element
   * @throws Exception Test error
   */
  @Test
  public void testCriteria() throws Exception {
    // Prepare
    Criteria criteria = (Criteria) new Criteria()
      .setChecked(true)
      .setShowSlider(true)
      .setReadonly(true)
      .setCapitalize(true)
      .setCheckEmpty(true)
      .setCheckInitial(true)
      .setOptional(true)
      .setShowFutureDates(true)
      .setShowTodayButton(true)
      .setShowWeekends(true)
      .setStrict(true)
      .setPrintable("excel");

    Criteria criteriaAllFalse = (Criteria) new Criteria()
      .setChecked(false)
      .setShowSlider(false)
      .setReadonly(false)
      .setCapitalize(false)
      .setCheckEmpty(false)
      .setCheckInitial(false)
      .setOptional(false)
      .setShowFutureDates(false)
      .setShowTodayButton(false)
      .setShowWeekends(false)
      .setStrict(false)
      .setPrintable("false");

    Criteria criteriaAllNull = new Criteria();

    // Run
    assertTrue(criteria.isChecked());
    assertTrue(criteria.isShowSlider());
    assertTrue(criteria.isReadonly());
    assertTrue(criteria.isCapitalize());
    assertTrue(criteria.isCheckEmpty());
    assertTrue(criteria.isCheckInitial());
    assertTrue(criteria.isOptional());
    assertTrue(criteria.isShowFutureDates());
    assertTrue(criteria.isShowTodayButton());
    assertTrue(criteria.isShowWeekends());
    assertTrue(criteria.isStrict());
    assertTrue(criteria.isPrintable());

    assertFalse(criteriaAllFalse.isChecked());
    assertFalse(criteriaAllFalse.isShowSlider());
    assertFalse(criteriaAllFalse.isReadonly());
    assertFalse(criteriaAllFalse.isCapitalize());
    assertFalse(criteriaAllFalse.isCheckEmpty());
    assertFalse(criteriaAllFalse.isCheckInitial());
    assertFalse(criteriaAllFalse.isOptional());
    assertFalse(criteriaAllFalse.isShowFutureDates());
    assertFalse(criteriaAllFalse.isShowTodayButton());
    assertFalse(criteriaAllFalse.isShowWeekends());
    assertFalse(criteriaAllFalse.isStrict());
    assertFalse(criteriaAllFalse.isPrintable());

    assertFalse(criteriaAllNull.isChecked());
    assertFalse(criteriaAllNull.isShowSlider());
    assertFalse(criteriaAllNull.isReadonly());
    assertFalse(criteriaAllNull.isCapitalize());
    assertFalse(criteriaAllNull.isCheckEmpty());
    assertTrue(criteriaAllNull.isCheckInitial());
    assertFalse(criteriaAllNull.isOptional());
    assertFalse(criteriaAllNull.isShowFutureDates());
    assertFalse(criteriaAllNull.isShowTodayButton());
    assertFalse(criteriaAllNull.isShowWeekends());
    assertTrue(criteriaAllNull.isStrict());
    assertTrue(criteriaAllNull.isPrintable());
  }
}