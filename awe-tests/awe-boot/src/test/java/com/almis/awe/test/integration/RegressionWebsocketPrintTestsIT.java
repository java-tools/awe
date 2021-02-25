package com.almis.awe.test.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  RegressionTestsIT.class,
  WebsocketTestsIT.class,
  PrintTestsIT.class
})
public class RegressionWebsocketPrintTestsIT {
}
