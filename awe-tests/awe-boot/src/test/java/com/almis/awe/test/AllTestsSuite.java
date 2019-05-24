package com.almis.awe.test;

import com.almis.awe.test.unit.SpringTestsSuite;
import com.almis.awe.test.unit.pojo.PojoTestsSuite;
import com.almis.awe.test.unit.rest.RestTestsSuite;
import com.almis.awe.test.unit.util.UtilitiesTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite.SuiteClasses({
  SpringTestsSuite.class,
  UtilitiesTestsSuite.class,
  RestTestsSuite.class,
  PojoTestsSuite.class
})
@RunWith(Suite.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllTestsSuite {
}

