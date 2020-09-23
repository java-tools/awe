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
public class InitializationTest {

  @Test
  @Scenario("performance/initialize.yml")
  public void initializePerformanceTests() throws Exception {
    assertTrue(true);
  }
}
