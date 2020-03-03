package com.almis.awe.test.unit.sqlserver;

import com.almis.awe.test.categories.CIDatabaseTest;
import com.almis.awe.test.categories.NotCIDatabaseTest;
import com.almis.awe.test.categories.NotSQLServerDatabaseTest;
import com.almis.awe.test.unit.database.DirectServiceCallTest;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@SuiteClasses({
  MaintainSQLServerTest.class,
  QuerySQLServerTest.class,
  SQLServerPerformanceTestsSuite.class,
  DirectServiceCallTest.class
})
@ExcludeCategory({NotCIDatabaseTest.class, NotSQLServerDatabaseTest.class})
@Category(CIDatabaseTest.class)
public class SpringSQLServerTestsSuite {
}

