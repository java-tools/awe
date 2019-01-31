package com.almis.awe.test;

import com.almis.awe.test.integration.StaticTestsIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  StaticTestsIT.class
})
@RunWith(Suite.class)
public class AllTestsSuiteIT {
}
