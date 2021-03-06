package com.almis.awe.test;

import com.almis.awe.test.categories.CIDatabaseTest;
import com.almis.awe.test.categories.NotHSQLDatabaseTest;
import com.almis.awe.test.unit.builder.BuildersTestsSuite;
import com.almis.awe.test.unit.developer.DeveloperTestSuite;
import com.almis.awe.test.unit.hsql.SpringHsqlTestsSuite;
import com.almis.awe.test.unit.notifier.NotifierTestsSuite;
import com.almis.awe.test.unit.pojo.PojoTestsSuite;
import com.almis.awe.test.unit.print.PrintTestSuite;
import com.almis.awe.test.unit.rest.SpringRestTestsSuite;
import com.almis.awe.test.unit.scheduler.SchedulerTestSuite;
import com.almis.awe.test.unit.services.ServicesTestSuite;
import com.almis.awe.test.unit.spring.SpringBootTestsSuite;
import com.almis.awe.test.unit.util.UtilitiesTestsSuite;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@ExcludeCategory({CIDatabaseTest.class, NotHSQLDatabaseTest.class})
@SuiteClasses({
        SpringHsqlTestsSuite.class,
        SpringBootTestsSuite.class,
        SpringRestTestsSuite.class,
        ServicesTestSuite.class,
        UtilitiesTestsSuite.class,
        PojoTestsSuite.class,
        BuildersTestsSuite.class,
        SchedulerTestSuite.class,
        DeveloperTestSuite.class,
        NotifierTestsSuite.class,
        PrintTestSuite.class
})
public class AllTestsSuite {
}

