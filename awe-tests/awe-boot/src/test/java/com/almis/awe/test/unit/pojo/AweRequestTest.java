package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.test.bean.Planet;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * DataList, DataListUtil and DataListBuilder tests
 *
 * @author pgarcia
 */
@RunWith(MockitoJUnitRunner.class)
public class AweRequestTest extends TestUtil {

  @InjectMocks
  private AweRequest aweRequest;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  /**
   * Test of set parameter
   *
   * @throws Exception Test error
   */
  @Test
  public void testSetParameter() throws Exception {
    // Prepare
    ObjectMapper mapper = new ObjectMapper();
    JsonNodeFactory factory = JsonNodeFactory.instance;
    BigDecimal bigDecimal = new BigDecimal(1213123);
    Date date = new Date();
    aweRequest.setParameter("test", "test");
    aweRequest.setParameter("testList", "test1", "test2");
    aweRequest.setParameter("testList2", Arrays.asList("test1", "test2"));
    aweRequest.setParameter("testInt", 1);
    aweRequest.setParameter("testLong", 1L);
    aweRequest.setParameter("testDouble", 1D);
    aweRequest.setParameter("testFloat", 1F);
    aweRequest.setParameter("testBigDecimal", bigDecimal);
    aweRequest.setParameter("testDate", date);
    aweRequest.setParameter("testNull", null);
    aweRequest.setParameter("testBoolean", true);
    aweRequest.setParameter("testCellData", new CellData("121"));
    aweRequest.setParameter("testPOJO", new Planet().setClimate("rainy"));

    // Assert
    assertEquals("test", aweRequest.getParameterAsString("test"));
    assertEquals(factory.arrayNode().add("test1").add("test2"), aweRequest.getParameter("testList"));
    assertEquals(factory.arrayNode().add("test1").add("test2"), aweRequest.getParameter("testList2"));
    assertEquals(factory.numberNode(1), aweRequest.getParameter("testInt"));
    assertEquals(factory.numberNode(1L), aweRequest.getParameter("testLong"));
    assertEquals(factory.numberNode(1D), aweRequest.getParameter("testDouble"));
    assertEquals(factory.numberNode(1F), aweRequest.getParameter("testFloat"));
    assertEquals(factory.numberNode(bigDecimal), aweRequest.getParameter("testBigDecimal"));

    // Test retrieve value as string
    assertEquals("1", aweRequest.getParameterAsString("testInt"));
    assertEquals("1", aweRequest.getParameterAsString("testLong"));
    assertEquals("1.0", aweRequest.getParameterAsString("testDouble"));
    assertEquals("1.0", aweRequest.getParameterAsString("testFloat"));
    assertEquals("1213123", aweRequest.getParameterAsString("testBigDecimal"));

    assertEquals(DateUtil.dat2WebTimestamp(date), aweRequest.getParameterAsString("testDate"));
    assertEquals(null, aweRequest.getParameterAsString("testNull"));
    assertEquals(true, aweRequest.getParameter("testBoolean").asBoolean());
    assertEquals("true", aweRequest.getParameterAsString("testBoolean"));
    assertEquals(factory.textNode("121"), aweRequest.getParameter("testCellData"));
    assertEquals("121", aweRequest.getParameterAsString("testCellData"));
    assertEquals(mapper.convertValue(new Planet().setClimate("rainy"), ObjectNode.class), aweRequest.getParameter("testPOJO"));
  }
}