package com.almis.awe.test.unit.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  DateUtilTest.class,
  ZipFileUtilTest.class,
  FileUtilTest.class,
  TimeUtilTest.class,
  QueryUtilTest.class,
  EncodeUtilTest.class
})
@RunWith(Suite.class)
public class UtilitiesTestsSuite {
}

