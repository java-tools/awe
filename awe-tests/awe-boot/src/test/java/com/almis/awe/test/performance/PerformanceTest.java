package com.almis.awe.test.performance;

import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtension;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.assertTrue;

@TargetEnv("performance_connection.properties")
@ExtendWith({ParallelLoadExtension.class})
@TestMethodOrder(Alphanumeric.class)
public class PerformanceTest {
  @Test
  @Scenario("performance/test-eval-big-performance.yml")
  public void testEvalBigPerformance() throws Exception {
    assertTrue(true);
  }
}
