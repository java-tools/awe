package com.almis.awe.test;

import com.almis.awe.test.integration.RegressionTestsIT;
import com.almis.awe.test.integration.StaticTestsIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  StaticTestsIT.class,
  RegressionTestsIT.class
})
@RunWith(Suite.class)
public class AllTestsSuiteIT {
}
