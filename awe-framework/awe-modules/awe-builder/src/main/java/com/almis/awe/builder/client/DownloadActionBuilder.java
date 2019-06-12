package com.almis.awe.builder.client;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.util.file.FileUtil;
import lombok.extern.log4j.Log4j2;

/**
 * Get file action builder
 *
 * @author pgarcia
 */
@Log4j2
public class DownloadActionBuilder extends ClientActionBuilder<DownloadActionBuilder> {

  private static final String TYPE = "get-file";

  /**
   * Empty constructor
   */
  public DownloadActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and value list
   *
   * @param fileData File data
   */
  public DownloadActionBuilder(FileData fileData) {
    setType(TYPE);
    try {
      addParameter("filename", FileUtil.fileDataToString(fileData));
    } catch (AWException exc) {
      log.error(exc.getTitle() + " " + exc.getMessage(), exc);
    }
  }
}

