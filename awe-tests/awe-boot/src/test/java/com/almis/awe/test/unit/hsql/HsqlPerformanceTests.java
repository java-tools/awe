package com.almis.awe.test.unit.hsql;

import com.almis.awe.test.unit.database.QueryTest;
import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ParallelLoadExtension.class})
public class HsqlPerformanceTests {

  @LoadWith("performance.properties")
  @TestMapping(testClass = QueryTest.class, testMethod = "testBigDataEvalPerformance")
  @Test
  public void testLoad() {
    // This space remains empty
  }
}
