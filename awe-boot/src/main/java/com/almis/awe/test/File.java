package com.almis.awe.test;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.file.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * File test class
 *
 * @author pgarcia
 */
@Service
public class File extends ServiceConfig {

  @Autowired
  private FileUtil fileUtil;

  /**
   * Given a file identifier, download a file
   * @param filedata File data
   * @return Service data
   * @throws AWException error retrieving file
   */
  public ServiceData downloadFile(String filedata) throws AWException {
    ServiceData serviceData = new ServiceData();
    String fullPath = null;
    FileData fileData = fileUtil.stringToFileData(filedata);

    try {
      fullPath = fileUtil.getFullPath(fileData, false);

      FileInputStream file = new FileInputStream(fullPath + fileData.getFileName());
      fileData.setFileStream(file);
    } catch (FileNotFoundException exc) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_READING_FILE"),
              getElements().getLocale("Error reading file {0} from {1}", new Object[]{fileData.getFileName(), fullPath}), exc);
    }

    // Set variables
    serviceData.setData(fileData);
    return serviceData;
  }

  /**
   * Given a file identifier, retrieve file information
   * @param filedata File data
   * @return File information
   * @throws AWException Error generating file info
   */
  public ServiceData getFileInfo(String filedata) throws AWException {
    ServiceData serviceData = new ServiceData();
    FileData fileData = fileUtil.stringToFileData(filedata);

    // Set variables
    String[] out = { filedata, fileData.getFileName() };

    // Set variables
    return serviceData.setData(out);
  }
}
