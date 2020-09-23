package com.almis.awe.test.unit.scheduler;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.scheduler.builder.task.FileTaskBuilder;
import com.almis.awe.scheduler.builder.task.ScheduledTaskBuilder;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class TaskBuilderTest extends TestUtil {

  /**
   * Generate task test with a null datalist
   */
  @Test
  public void generateTaskWithNullDataList() {
    assertThrows(AWException.class, () -> new FileTaskBuilder(null));
  }

  /**
   * Generate task test with an empty datalist
   */
  @Test
  public void generateTaskWithEmptyDataList() {
    assertThrows(AWException.class, () -> new ScheduledTaskBuilder(new DataList()));
  }
}
