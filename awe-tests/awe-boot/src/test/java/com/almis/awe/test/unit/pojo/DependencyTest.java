package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class DependencyTest extends TestUtil {

  /**
   * Test of dependency element
   * @throws Exception Test error
   */
  @Test
  public void testDependencyElement() throws Exception {
    // Prepare
    DependencyElement dependencyElement = new DependencyElement()
      .setOptional(true)
      .setCancel(true)
      .setCheckChanges(true);
    DependencyElement dependencyElementAllFalse = dependencyElement.copy()
      .setOptional(false)
      .setCancel(false)
      .setCheckChanges(false);
    DependencyElement dependencyElementAllNull =new DependencyElement();

    // Run
    assertTrue(dependencyElement.isOptional());
    assertTrue(dependencyElement.isCancel());
    assertTrue(dependencyElement.isCheckChanges());

    assertFalse(dependencyElementAllFalse.isOptional());
    assertFalse(dependencyElementAllFalse.isCancel());
    assertFalse(dependencyElementAllFalse.isCheckChanges());

    assertFalse(dependencyElementAllNull.isOptional());
    assertFalse(dependencyElementAllNull.isCancel());
    assertTrue(dependencyElementAllNull.isCheckChanges());
  }

  /**
   * Test of dependency element
   * @throws Exception Test error
   */
  @Test
  public void testDependency() throws Exception {
    // Prepare
    Dependency dependency = new Dependency()
      .setInitial(true)
      .setInvert(true)
      .setAsync(true)
      .setSilent(true);
    Dependency dependencyAllFalse = dependency.copy()
      .setInitial(false)
      .setInvert(false)
      .setAsync(false)
      .setSilent(false);
    Dependency dependencyAllNull = new Dependency();

    // Run
    assertTrue(dependency.isInitial());
    assertTrue(dependency.isInvert());
    assertTrue(dependency.isAsync());
    assertTrue(dependency.isSilent());

    assertFalse(dependencyAllFalse.isInitial());
    assertFalse(dependencyAllFalse.isInvert());
    assertFalse(dependencyAllFalse.isAsync());
    assertFalse(dependencyAllFalse.isSilent());

    assertFalse(dependencyAllNull.isInitial());
    assertFalse(dependencyAllNull.isInvert());
    assertFalse(dependencyAllNull.isAsync());
    assertFalse(dependencyAllNull.isSilent());
  }
}