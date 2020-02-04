package com.almis.awe.test.unit.hsql;

import com.almis.awe.test.unit.database.DirectServiceCallTest;
import com.almis.awe.test.unit.database.MaintainTest;
import com.almis.awe.test.unit.database.QueryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MaintainTest.class,
  QueryTest.class,
  HsqlPerformanceTestsSuite.class,
  DirectServiceCallTest.class

})
public class SpringHsqlTestsSuite {
}