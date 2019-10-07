package com.almis.awe.test.unit.mysql;

import com.almis.awe.test.unit.categories.CIDatabaseTest;
import com.almis.awe.test.unit.categories.NotMySQLDatabaseTest;
import com.almis.awe.test.unit.categories.NotCIDatabaseTest;
import com.almis.awe.test.unit.database.DirectServiceCallTest;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@SuiteClasses({
  MaintainMySQLTest.class,
  QueryMySQLTest.class,
  DirectServiceCallTest.class
})
@ExcludeCategory({NotCIDatabaseTest.class, NotMySQLDatabaseTest.class})
@Category(CIDatabaseTest.class)
public class SpringMysqlTestsSuite {
}

