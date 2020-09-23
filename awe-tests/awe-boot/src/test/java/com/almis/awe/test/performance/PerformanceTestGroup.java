package com.almis.awe.test.performance;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@LoadWith("performance_load.properties")
@TestMapping(testClass = PerformanceTest.class, testMethod = "testEvalBigPerformance")
@ExtendWith({ParallelLoadExtension.class})
public class PerformanceTestGroup {
}
