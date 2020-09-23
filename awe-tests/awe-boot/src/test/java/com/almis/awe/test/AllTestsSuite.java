package com.almis.awe.test;

import com.almis.awe.test.unit.spring.SpringBootTestsSuite;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@ExcludeTags({"CIDatabaseTest", "NotHSQLDatabaseTest"})
@SelectClasses({
       /* SpringHsqlTestsSuite.class,*/
        SpringBootTestsSuite.class,
      /*  SpringRestTestsSuite.class,
        ServicesTestSuite.class,
        UtilitiesTestsSuite.class,
        PojoTestsSuite.class,
        BuildersTestsSuite.class,
        SchedulerTestSuite.class,
        DeveloperTestSuite.class,
        NotifierTestsSuite.class,
        PrintTestSuite.class*/
})
@RunWith(JUnitPlatform.class)
public class AllTestsSuite {
}

