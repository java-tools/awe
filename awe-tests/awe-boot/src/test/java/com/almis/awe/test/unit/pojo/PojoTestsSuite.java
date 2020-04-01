package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.tracker.AweConnectionTracker;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  DataListTest.class,
  CellDataTest.class,
  WidgetTest.class,
  DependencyTest.class,
  TableTest.class,
  CriteriaTest.class,
  QueryTest.class,
  ServiceDataTest.class,
  ChartParameterTest.class,
  AweRequestTest.class,
  AweConnectionTrackerTest.class
})
@RunWith(Suite.class)
public class PojoTestsSuite {
}

