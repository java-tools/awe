package com.almis.awe.test.unit.sqlserver;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.runner.parallel.ZeroCodeMultiLoadRunner;
import org.junit.runner.RunWith;

@LoadWith("performance.properties")
@TestMapping(testClass = QuerySQLServerTest.class, testMethod = "testBigDataEvalPerformance")
@RunWith(ZeroCodeMultiLoadRunner.class)
public class SQLServerPerformanceTestsSuite {
}
