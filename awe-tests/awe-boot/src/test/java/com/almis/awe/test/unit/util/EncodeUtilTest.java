package com.almis.awe.test.unit.util;

import com.almis.awe.model.util.security.EncodeUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author pgarcia
 */
@RunWith(MockitoJUnitRunner.class)
public class EncodeUtilTest {

  @Before
  public void setUp() {
    EncodeUtil.init(null);
  }

  /**
   * Test of hash
   *
   * @throws Exception Test error
   */
  @Test
  public void testHash() throws Exception {
    // Prepare
    assertEquals("655e786674d9d3e77bc05ed1de37b4b6bc89f788829f9f3c679e7687b410c89b", EncodeUtil.hash(EncodeUtil.HashingAlgorithms.SHA_256, "prueba"));
  }
}