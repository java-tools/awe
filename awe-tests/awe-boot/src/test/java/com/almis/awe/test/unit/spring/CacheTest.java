package com.almis.awe.test.unit.spring;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.cache.CacheManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(Alphanumeric.class)
public class CacheTest extends AweSpringBootTests {

  private AweElements aweElements;
  private CacheManager cacheManager;

  @BeforeEach
  public void loadBeans() {
    aweElements = getElements();
    cacheManager = getBean(CacheManager.class);
  }

  @Test
  public void testMenu() throws Exception {
    Menu originalMenu = aweElements.getMenu(AweConstants.PUBLIC_MENU);
    Menu menu = originalMenu.copy();
    menu.addElement(Option.builder()
      .invisible(true)
      .screen("Prueba")
      .name("prueba")
      .build());
    aweElements.setMenu(AweConstants.PUBLIC_MENU, menu);
    assertThat(menu.getElementList().size(), equalTo(aweElements.getMenu(AweConstants.PUBLIC_MENU).getElementList().size()));
    aweElements.setMenu(AweConstants.PUBLIC_MENU, originalMenu);
  }

  @Test
  public void testScreen() throws Exception {
    Screen screen = aweElements.getScreen("MatTst").copy();
    screen.addElement(Tag.builder().build());
    aweElements.setScreen(screen);
    assertThat(screen.getElementList().size(), equalTo(aweElements.getScreen("MatTst").getElementList().size()));
  }

  @Test
  public void testEnumerated() throws Exception {
    // Skip the first result: For some reason is not the same object as cached one
    aweElements.getEnumerated("Es1Es0");

    // First invocation returns object returned by the method
    EnumeratedGroup first = aweElements.getEnumerated("Es1Es0");

    // Second invocation should return cached value
    EnumeratedGroup result = aweElements.getEnumerated("Es1Es0");
    assertThat(result, is(first));

    // Verify repository method was invoked once
    assertThat(cacheManager.getCache("enumerated").get("Es1Es0"), is(notNullValue()));

    // Skip the first result: For some reason is not the same object as cached one
    aweElements.getEnumerated("EsyEnn");

    // First invocation returns object returned by the method
    EnumeratedGroup second = aweElements.getEnumerated("EsyEnn");
    assertThat(second, not(is(first)));

    // Second invocation should return cached value
    result = aweElements.getEnumerated("EsyEnn");
    assertThat(result, is(second));

    // Verify repository method was invoked once
    assertThat(cacheManager.getCache("enumerated").get("EsyEnn"), is(notNullValue()));
  }
}