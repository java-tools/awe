package com.almis.awe.test;

import com.almis.awe.test.unit.categories.CIDatabaseTest;
import com.almis.awe.test.unit.hsql.SpringHsqlTestsSuite;
import com.almis.awe.test.unit.pojo.PojoTestsSuite;
import com.almis.awe.test.unit.rest.SpringRestTestsSuite;
import com.almis.awe.test.unit.spring.SpringBootTestsSuite;
import com.almis.awe.test.unit.util.UtilitiesTestsSuite;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@ExcludeCategory(CIDatabaseTest.class)
@SuiteClasses({
  SpringHsqlTestsSuite.class,
  SpringBootTestsSuite.class,
  SpringRestTestsSuite.class,
  UtilitiesTestsSuite.class,
  PojoTestsSuite.class
})
public class AllTestsSuite {
}

