package com.almis.awe.test.unit.scheduler;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.scheduler.builder.task.FileTaskBuilder;
import com.almis.awe.scheduler.builder.task.ScheduledTaskBuilder;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class TaskBuilderTest extends TestUtil {

  /**
   * Generate task test with a null datalist
   */
  @Test(expected = AWException.class)
  public void generateTaskWithNullDataList() throws AWException {
    new FileTaskBuilder(null);
  }

  /**
   * Generate task test with an empty datalist
   */
  @Test(expected = AWException.class)
  public void generateTaskWithEmptyDataList() throws AWException {
    new ScheduledTaskBuilder(new DataList());
  }
}
