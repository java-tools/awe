package com.almis.awe.test.unit.hsql;

import com.almis.awe.test.unit.database.DirectServiceCallTest;
import com.almis.awe.test.unit.database.MaintainTest;
import com.almis.awe.test.unit.database.QueryTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Hsql Database Performance Suite")
@SelectClasses({
  MaintainTest.class,
  QueryTest.class,
  HsqlPerformanceTests.class,
  DirectServiceCallTest.class
})
public class SpringHsqlTestsSuite {
}