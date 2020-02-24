package com.almis.awe.test.performance;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@SuiteClasses({
  InitializationTest.class,
  PerformanceTestGroup.class
})
public class PerformanceTestSuite {
}