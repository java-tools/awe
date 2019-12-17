package com.almis.awe.test.unit.scheduler;

import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.FileDAO;
import com.almis.awe.service.MaintainService;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.NamingException;
import java.util.Date;

import static com.almis.awe.scheduler.constant.MaintainConstants.FILE_INSERT_MODIFICATION_QUERY;
import static com.almis.awe.scheduler.constant.MaintainConstants.FILE_UPDATE_MODIFICATION_QUERY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class FileDAOTest extends TestUtil {

  @InjectMocks
  private FileDAO fileDAO;

  @Mock
  private MaintainService maintainService;

  @Mock
  private QueryUtil queryUtil;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
    given(queryUtil.getParameters((String) null)).willReturn(JsonNodeFactory.instance.objectNode());
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(fileDAO).isNotNull();
  }

  /**
   * Test add modification (insert)
   *
   * @throws NamingException Test error
   */
  @Test
  public void addModificationInsert() throws Exception {
    // Mock
    Task task = new Task().setFile(new File());

    // Call method
    fileDAO.addModification(task, "", new Date(), false);

    // Assert called
    verify(maintainService, times(1)).launchPrivateMaintain(eq(FILE_INSERT_MODIFICATION_QUERY), any(ObjectNode.class));
  }

  /**
   * Test add modification (update)
   *
   * @throws NamingException Test error
   */
  @Test
  public void addModificationUpdate() throws Exception {
    // Mock
    Task task = new Task().setFile(new File());

    // Call method
    fileDAO.addModification(task, "", new Date(), true);

    // Assert called
    verify(maintainService, times(1)).launchPrivateMaintain(eq(FILE_UPDATE_MODIFICATION_QUERY), any(ObjectNode.class));
  }
}
