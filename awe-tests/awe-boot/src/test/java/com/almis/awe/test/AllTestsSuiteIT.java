package com.almis.awe.test;

import com.almis.awe.test.integration.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  CRUDTestsIT.class,
  ApplicationTestsIT.class,
  RegressionTestsIT.class,
  WebsocketTestsIT.class,
  CriteriaAndMatrixTestsIT.class,
  IntegrationTestsIT.class,
  SchedulerTestsIT.class,
  PrintTestsIT.class,
})
public class AllTestsSuiteIT {
}
