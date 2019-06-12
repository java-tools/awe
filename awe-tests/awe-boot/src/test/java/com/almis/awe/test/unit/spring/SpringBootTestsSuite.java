package com.almis.awe.test.unit.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  CacheTest.class,
  AnnotationTest.class,
  EncryptTest.class,
  FileServiceTest.class,
  JavaServiceTest.class,
  MenuServiceTest.class,
  TemplateControllerTest.class,
  UserServiceTest.class,
  UploadControllerTest.class
})
@RunWith(Suite.class)
public class SpringBootTestsSuite {
}

