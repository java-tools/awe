package com.almis.awe.test.unit.spring;

import com.almis.awe.dao.InitialLoadDao;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.screen.data.ComponentModel;
import com.almis.awe.model.entities.screen.data.ScreenComponent;
import com.almis.awe.service.screen.ScreenModelGenerator;
import com.almis.awe.service.screen.ScreenRestrictionGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ScreenModelGeneratorTest {
  @InjectMocks
  private ScreenModelGenerator screenModelGenerator;

  @Mock
  private ScreenRestrictionGenerator screenRestrictionGenerator;

  @Mock
  private InitialLoadDao initialLoadDao;

  /**
   * Initialize beans
   */
  @Before
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void generateComponentModelFromDataList() {
    ScreenComponent screenComponent = new ScreenComponent().setModel(new ComponentModel());
    screenModelGenerator.generateComponentModelFromDataList(new DataList(), screenComponent);
    assertEquals(Long.valueOf(0), screenComponent.getModel().getRecords());
  }
}
