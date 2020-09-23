package com.almis.awe.test.performance;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
  InitializationTest.class,
  PerformanceTestGroup.class
})
public class PerformanceTestSuite {
}