package com.almis.awe.test.unit.sqlserver;

import com.almis.awe.test.unit.categories.CIDatabaseTest;
import com.almis.awe.test.unit.categories.NotCIDatabaseTest;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@SuiteClasses({
  MaintainSQLServerTest.class,
  QuerySQLServerTest.class
})
@ExcludeCategory(NotCIDatabaseTest.class)
@Category(CIDatabaseTest.class)
public class SpringSqlserverTestsSuite {
}

