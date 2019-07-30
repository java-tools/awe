package com.almis.awe.test.unit.util;

import com.almis.awe.model.util.file.FileUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest {

  @Test
  public void sanitizeFileName() {
    assertEquals("tutu.zip", FileUtil.sanitizeFileName("allala/../tutu.zip"));
    assertEquals("", FileUtil.sanitizeFileName(null));
  }

  @Test
  public void fixUntrustedPath() {
    assertEquals("allala/tutu.zip", FileUtil.fixUntrustedPath("allala/../tutu.zip"));
    assertEquals("allala/tutu/alalal/asdaas/epa.txt", FileUtil.fixUntrustedPath("allala/../tutu/../../", "alalal/../asdaas/../epa.txt"));
    assertEquals("", FileUtil.fixUntrustedPath());
  }
}