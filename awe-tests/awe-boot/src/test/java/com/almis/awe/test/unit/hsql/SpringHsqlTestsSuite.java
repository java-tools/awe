package com.almis.awe.test.unit.hsql;

import com.almis.awe.test.unit.database.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MaintainTest.class,
  QueryTest.class,
  DirectServiceCallTest.class
})
public class SpringHsqlTestsSuite {
}