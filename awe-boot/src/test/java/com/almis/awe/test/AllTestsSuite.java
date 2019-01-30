package com.almis.awe.test;

import com.almis.awe.test.builder.ScreenBuilderTest;
import com.almis.awe.test.rest.DataRestControllerTest;
import com.almis.awe.test.rest.MaintainRestControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  AnnotationTest.class,
  CacheTest.class,
  EncryptTest.class,
  MaintainHSQLTest.class,
  QueryHSQLTest.class,
  RestServiceTest.class,
  ScreenConfigurationTest.class,
  ScreenControllerTest.class,
  ScreenRestrictionTest.class,
  TemplateControllerTest.class,
  DataRestControllerTest.class,
  MaintainRestControllerTest.class,
  ScreenBuilderTest.class
})
@RunWith(Suite.class)
public class AllTestsSuite {
}

