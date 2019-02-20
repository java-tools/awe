package com.almis.awe.test;

import com.almis.awe.test.integration.IntegrationTestsIT;
import com.almis.awe.test.integration.RegressionTestsIT;
import com.almis.awe.test.integration.CRUDTestsIT;
import com.almis.awe.test.integration.WebsocketTestsIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  CRUDTestsIT.class,
  RegressionTestsIT.class,
  WebsocketTestsIT.class,
  IntegrationTestsIT.class
})
@RunWith(Suite.class)
public class AllTestsSuiteIT {
}
