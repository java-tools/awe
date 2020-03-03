package com.almis.awe.test.unit.oracle;

import com.almis.awe.test.categories.CIDatabaseTest;
import com.almis.awe.test.categories.NotCIDatabaseTest;
import com.almis.awe.test.categories.NotOracleDatabaseTest;
import com.almis.awe.test.unit.database.DirectServiceCallTest;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@SuiteClasses({
  MaintainOracleTest.class,
  QueryOracleTest.class,
  OraclePerformanceTestsSuite.class,
  DirectServiceCallTest.class
})
@ExcludeCategory({NotCIDatabaseTest.class, NotOracleDatabaseTest.class})
@Category(CIDatabaseTest.class)
public class SpringOracleTestsSuite {
}

