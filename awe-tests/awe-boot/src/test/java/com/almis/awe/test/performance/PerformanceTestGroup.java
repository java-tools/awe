package com.almis.awe.test.performance;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.runner.parallel.ZeroCodeMultiLoadRunner;
import org.junit.runner.RunWith;

@LoadWith("performance_load.properties")
@TestMapping(testClass = PerformanceTest.class, testMethod = "testEvalBigPerformance")
@RunWith(ZeroCodeMultiLoadRunner.class)
public class PerformanceTestGroup {
}
