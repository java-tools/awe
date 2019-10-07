package com.almis.awe.test.unit.hsql;

import com.almis.awe.test.unit.categories.NotHSQLDatabaseTest;
import com.almis.awe.test.unit.database.DirectServiceCallTest;
import com.almis.awe.test.unit.database.MaintainTest;
import com.almis.awe.test.unit.database.QueryTest;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MaintainTest.class,
  QueryTest.class,
  DirectServiceCallTest.class
})
@ExcludeCategory(NotHSQLDatabaseTest.class)
public class SpringHsqlTestsSuite {
}