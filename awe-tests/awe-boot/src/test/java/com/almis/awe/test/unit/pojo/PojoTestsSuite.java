package com.almis.awe.test.unit.pojo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  DataListTest.class,
  CellDataTest.class,
  WidgetTest.class,
  DependencyTest.class,
  TagListTest.class,
  TableTest.class,
  CriteriaTest.class
})
@RunWith(Suite.class)
public class PojoTestsSuite {
}

