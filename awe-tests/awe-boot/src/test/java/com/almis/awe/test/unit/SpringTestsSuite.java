package com.almis.awe.test.unit;

import com.almis.awe.test.unit.builder.ScreenBuilderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  AnnotationTest.class,
  CacheTest.class,
  EncryptTest.class,
  MaintainHSQLTest.class,
  QueryHSQLTest.class,
  ScreenConfigurationTest.class,
  ScreenControllerTest.class,
  ScreenRestrictionTest.class,
  TemplateControllerTest.class,
  ScreenBuilderTest.class,
  MenuServiceTest.class,
  FileServiceTest.class,
  JavaServiceTest.class,
  UploadControllerTest.class,
  UserServiceTest.class,
  RestServiceTest.class,
  MicroserviceTest.class
})
@RunWith(Suite.class)
public class SpringTestsSuite {
}

