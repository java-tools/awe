package com.almis.awe.test.unit.oracle;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.runner.parallel.ZeroCodeMultiLoadRunner;
import org.junit.runner.RunWith;

@LoadWith("performance.properties")
@TestMapping(testClass = QueryOracleTest.class, testMethod = "testBigDataEvalPerformance")
@RunWith(ZeroCodeMultiLoadRunner.class)
public class OraclePerformanceTestsSuite {
}
