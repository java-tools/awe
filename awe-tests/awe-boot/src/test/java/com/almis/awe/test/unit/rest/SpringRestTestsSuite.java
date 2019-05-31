package com.almis.awe.test.unit.rest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  RestServiceTest.class,
  MicroserviceTest.class,
  DataRestControllerTest.class,
  MaintainRestControllerTest.class,
  ScreenConfigurationTest.class,
  ScreenControllerTest.class,
  ScreenRestrictionTest.class,
  TagListTest.class
})
public class SpringRestTestsSuite {
}

