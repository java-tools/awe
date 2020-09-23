package com.almis.awe.test.unit.scheduler;

import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.file.Server;
import com.almis.awe.scheduler.dao.ServerDAO;
import com.almis.awe.service.QueryService;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.NamingException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class ServerDAOTest extends TestUtil {

  @InjectMocks
  private ServerDAO serverDAO;

  @Mock
  private QueryService queryService;

  @Mock
  private QueryUtil queryUtil;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(serverDAO).isNotNull();
  }

  /**
   * Test not finding server
   *
   * @throws Exception
   */
  @Test
  public void findServerEmpty() throws Exception {
    given(queryUtil.getParameters(null, "1", "0")).willReturn(JsonNodeFactory.instance.objectNode());
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(new DataList()));

    // Assert called
    assertEquals(null, serverDAO.findServer(121, null));
  }

  /**
   * Test finding server
   *
   * @throws Exception
   */
  @Test
  public void findServer() throws Exception {
    Server server = new Server()
      .setServerId(121)
      .setActive(true)
      .setName("Server Guay")
      .setHost("192.168.1.21")
      .setTypeOfConnection("FTP")
      .setPort(8080);

    given(queryUtil.getParameters(null, "1", "0")).willReturn(JsonNodeFactory.instance.objectNode());
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(DataListUtil.fromBeanList(Arrays.asList(server))));

    // Assert called
    assertEquals(server, serverDAO.findServer(121, null));
  }

  /**
   * Test finding server without port
   *
   * @throws Exception
   */
  @Test
  public void findServerWithoutPort() throws Exception {
    Server server = new Server()
      .setServerId(122)
      .setActive(false)
      .setName("Server Guay2")
      .setHost("192.168.1.22")
      .setTypeOfConnection("SSH");

    given(queryUtil.getParameters(null, "1", "0")).willReturn(JsonNodeFactory.instance.objectNode());
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(DataListUtil.fromBeanList(Arrays.asList(server))));

    // Assert called
    assertEquals(server, serverDAO.findServer(122, null));
  }
}
