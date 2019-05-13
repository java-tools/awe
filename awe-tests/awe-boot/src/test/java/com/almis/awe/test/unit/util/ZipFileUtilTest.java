package com.almis.awe.test.unit.util;

import com.almis.awe.test.unit.TestUtil;
import com.almis.awe.tools.filemanager.utils.ZipFileUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author pgarcia
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZipFileUtilTest extends TestUtil {
  private static final String TEMP_PATH = "target/tests";
  private static final String FULL_PATH = Paths.get("target", "tests").toAbsolutePath().toString();

  /**
   * Zip file test
   * @throws Exception Test error
   */
  @Test
  public void test1ZipFile() throws Exception {
    // Prepare
    zipFileTest(TEMP_PATH + "/testZip.zip");

    // Run
    File f = new File(TEMP_PATH + "/testZip.zip");

    // Assert
    assertTrue(f.exists());
  }

  /**
   * Create a zip file
   * @param path Zip path
   */
  private void zipFileTest(String path) throws IOException {
    List<String> files = new ArrayList<>();
    files.add(TEMP_PATH + "/db/awe-test.log");
    files.add(TEMP_PATH + "/upload");
    ZipFileUtil.create(path, files);
  }

  /**
   * Zip file test
   * @throws Exception Test error
   */
  @Test
  public void test2UnzipFile() throws Exception {
    // Run
    ZipFileUtil.unzip(TEMP_PATH + "/testZip.zip", TEMP_PATH + "/unzip");
    File f = new File(TEMP_PATH + "/unzip");

    // Assert
    assertTrue(f.exists());
  }
}