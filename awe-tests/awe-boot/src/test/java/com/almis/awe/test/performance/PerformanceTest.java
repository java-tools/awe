package com.almis.awe.test.performance;

import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

@TargetEnv("performance_connection.properties")
@RunWith(ZeroCodeUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerformanceTest {
  @Test
  @Scenario("performance/test-eval-big-performance.yml")
  public void testEvalBigPerformance() throws Exception {
    assertTrue(true);
  }
}
