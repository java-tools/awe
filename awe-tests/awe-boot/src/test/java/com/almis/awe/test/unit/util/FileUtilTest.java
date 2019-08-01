package com.almis.awe.test.unit.util;

import com.almis.awe.model.util.file.FileUtil;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FileUtilTest {

  @Test
  public void sanitizeFileName() {
    assertEquals("tutu.zip", FileUtil.sanitizeFileName("allala/../tutu.zip"));
    assertEquals("", FileUtil.sanitizeFileName(null));
  }

  @Test
  public void fixUntrustedPath() {
    assertEquals(Paths.get("allala", "tutu.zip").toString(), FileUtil.fixUntrustedPath("allala/..\\tutu.zip"));
    assertEquals(Paths.get("allala", "tutu", "alalal" , "asdaas" , "epa.txt").toString(), FileUtil.fixUntrustedPath("allala/../tutu\\../../", "/alalal\\../asdaas/../epa.txt"));
    assertEquals("", FileUtil.fixUntrustedPath());
  }
}