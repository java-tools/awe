package com.almis.awe.test.unit.services;

import com.almis.awe.dao.TemplateDao;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.screen.component.TagList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.service.MenuService;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.TemplateService;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stringtemplate.v4.STGroup;

import javax.naming.NamingException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class TemplateServiceTest extends TestUtil {

  @InjectMocks
  private TemplateService templateService;

  @Mock
  private MenuService menuService;

  @Mock
  private STGroup elementsTemplateGroup;

  @Mock
  private STGroup helpTemplateGroup;

  @Mock
  private STGroup screensTemplateGroup;

  @Mock
  private QueryService queryService;

  @Mock
  private TemplateDao templateDao;

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
    assertThat(templateService).isNotNull();
  }

  /**
   * Check generate template list empty
   */
  @Test
  public void generateTemplateListEmpty() throws Exception {
    // Mock
    when(templateDao.generateTaglistXml(anyList())).thenReturn("Template [value] tutu lala [otherValue]");

    // Run
    List<String> templates = templateService.generateTaglistTemplate(new TagList(), new DataList());

    // Assert
    assertEquals(0, templates.size());
  }

  /**
   * Check generate template list empty
   */
  @Test
  public void generateTemplateListWithValues() throws Exception {
    // Mock
    when(templateDao.generateTaglistXml(anyList())).thenReturn("Template [value] tutu lala [otherValue]");
    DataList dataList = new DataList();
    DataListUtil.addColumn(dataList, "value", Arrays.asList("lalala", "lerele", null));
    DataListUtil.addColumn(dataList, "otherValue", Arrays.asList("tututu", null, "tititi"));
    dataList.getRows().get(2).put("value", null);

    // Run
    List<String> templates = templateService.generateTaglistTemplate(new TagList(), dataList);

    // Assert
    assertEquals(3, templates.size());
    assertEquals("Template lalala tutu lala tututu", templates.get(0));
    assertEquals("Template lerele tutu lala ", templates.get(1));
    assertEquals("Template [value] tutu lala tititi", templates.get(2));
  }
}
