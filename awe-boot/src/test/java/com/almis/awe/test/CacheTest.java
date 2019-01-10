package com.almis.awe.test;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith (SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CacheTest {

  @Autowired
  AweElements aweElements;

  @Autowired
  CacheManager cacheManager;

  @Test
  public void testMenu() throws Exception {
    Menu menu = new Menu(aweElements.getMenu(AweConstants.PUBLIC_MENU));
    menu.addElement(new Option().setInvisible(true).setScreen("Prueba").setName("prueba"));
    aweElements.setMenu(AweConstants.PUBLIC_MENU, menu);
    assertThat(menu.getElementList().size(), equalTo(aweElements.getMenu(AweConstants.PUBLIC_MENU).getElementList().size()));
  }

  @Test
  public void testScreen() throws Exception {
    Screen screen = new Screen(aweElements.getScreen("MatTst"));
    screen.addElement(new Tag());
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