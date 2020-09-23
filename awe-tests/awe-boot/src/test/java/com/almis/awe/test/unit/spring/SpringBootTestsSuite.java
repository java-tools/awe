package com.almis.awe.test.unit.spring;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@SelectClasses({
  CacheTest.class,
  AnnotationTest.class,
  EncryptTest.class,
  FileServiceTest.class,
  JavaServiceTest.class,
  MenuServiceTest.class,
  TemplateControllerTest.class,
  UserServiceTest.class,
  UploadControllerTest.class,
  BroadcastServiceTest.class,
  EmailServiceTest.class,
  ScreenModelGeneratorTest.class,
  PropertiesTest.class
})
@RunWith(JUnitPlatform.class)
public class SpringBootTestsSuite {
}

