package com.almis.awe.test;

import com.almis.awe.test.unit.spring.SpringBootTestsSuite;
import com.almis.awe.test.unit.rest.SpringRestTestsSuite;
import com.almis.awe.test.unit.pojo.PojoTestsSuite;
import com.almis.awe.test.unit.util.UtilitiesTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite.SuiteClasses({
  SpringBootTestsSuite.class,
  SpringRestTestsSuite.class,
  UtilitiesTestsSuite.class,
  PojoTestsSuite.class
})
@RunWith(Suite.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllTestsSuite {
}

