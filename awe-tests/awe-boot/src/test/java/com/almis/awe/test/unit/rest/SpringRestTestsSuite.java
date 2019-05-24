package com.almis.awe.test.unit.rest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  RestServiceTest.class,
  MicroserviceTest.class,
  DataRestControllerTest.class,
  MaintainRestControllerTest.class,
  MaintainHSQLTest.class,
  QueryHSQLTest.class,
  ScreenConfigurationTest.class,
  ScreenControllerTest.class,
  ScreenRestrictionTest.class,
  TagListTest.class
})
@RunWith(Suite.class)
public class SpringRestTestsSuite {
}

