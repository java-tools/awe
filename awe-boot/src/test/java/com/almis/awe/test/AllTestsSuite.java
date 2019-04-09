package com.almis.awe.test;

import com.almis.awe.test.unit.builder.ScreenBuilderTest;
import com.almis.awe.test.unit.rest.DataRestControllerTest;
import com.almis.awe.test.unit.rest.MaintainRestControllerTest;
import com.almis.awe.test.unit.*;
import com.almis.awe.test.unit.util.DateUtilTest;
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
  DataRestControllerTest.class,
  MaintainRestControllerTest.class,
  ScreenBuilderTest.class,
  MenuServiceTest.class,
  FileServiceTest.class,
  UploadControllerTest.class,
  TagListTest.class,
  DateUtilTest.class
})
@RunWith(Suite.class)
public class AllTestsSuite {
}

