package com.almis.awe.test;

import com.almis.awe.test.unit.*;
import com.almis.awe.test.unit.builder.ScreenBuilderTest;
import com.almis.awe.test.unit.pojo.PojoTestsSuite;
import com.almis.awe.test.unit.rest.RestTestsSuite;
import com.almis.awe.test.unit.util.UtilitiesTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  AnnotationTest.class,
  CacheTest.class,
  EncryptTest.class,
  MaintainHSQLTest.class,
  QueryHSQLTest.class,
  RestServiceTest.class,
  MicroserviceTest.class,
  ScreenConfigurationTest.class,
  ScreenControllerTest.class,
  ScreenRestrictionTest.class,
  TemplateControllerTest.class,
  ScreenBuilderTest.class,
  MenuServiceTest.class,
  FileServiceTest.class,
  UploadControllerTest.class,
  UserServiceTest.class,
  UtilitiesTestsSuite.class,
  RestTestsSuite.class,
  PojoTestsSuite.class
})
@RunWith(Suite.class)
public class AllTestsSuite {
}

