package com.almis.awe.test.unit.developer;

import com.almis.awe.developer.service.LiteralsService;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Class used for testing translation service
 *
 * @author pvidal
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class LiteralsServiceTest extends TestUtil {

  @InjectMocks
  private LiteralsService literalsService;

  @Mock
  private ApplicationContext context;

  @Mock
  private AweElements aweElements;

  /**
   * Initializes beans for tests
   */
  @Before
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
    doReturn(aweElements).when(context).getBean(any(Class.class));
    ReflectionTestUtils.setField(literalsService, "translationApiUrl", "http://api.mymemory.translated.net/get");
    ReflectionTestUtils.setField(literalsService, "keyParameter", "key");
    ReflectionTestUtils.setField(literalsService, "translationApiKey", "7fddcdd2be4f4fe4f632");
    ReflectionTestUtils.setField(literalsService, "languageParameter", "langpair");
    ReflectionTestUtils.setField(literalsService, "textParameter", "q");
    ReflectionTestUtils.setField(literalsService, "localeFile", "Locale-");

  }

  /**
   * Test translate a text with the same language from/to
   * Skip call api request
   *
   * @throws AWException AWE exception
   */
  @Test
  public void translateSameLanOriginDestination() throws AWException {
    // Mockito actions
    when(aweElements.getLocale(anyString())).thenReturn("LOCALE");

    // Launch
    ServiceData serviceData = literalsService.translate("This is a test", "en", "en");

    // Asserts and verifications
    assertNotNull(serviceData.getData());
  }

  /**
   * Test translate a text
   *
   * @throws AWException AWE exception
   */
  @Test
  public void translate() throws AWException {
    // Mockito actions
    when(aweElements.getLocale(anyString())).thenReturn("LOCALE");

    // Launch
    ServiceData serviceData = literalsService.translate("This is a test", "en", "es");

    // Asserts and verifications
    assertNotNull(serviceData.getData());
    assertArrayEquals(new String[]{"Esto es una prueba"}, (Object[]) serviceData.getData());
  }

  /**
   * Test switch Languages
   */
  @Test
  public void switchLanguages() {
    ServiceData serviceData = literalsService.switchLanguages("EN", "ES", "en", "es");
    assertEquals(2, serviceData.getClientActionList().size());
  }
}