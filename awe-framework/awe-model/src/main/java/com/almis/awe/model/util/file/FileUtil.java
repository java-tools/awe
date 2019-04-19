/*
 * Package definition
 */
package com.almis.awe.model.util.file;

/*
 * File Imports
 */

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.util.data.CompressionUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

/**
 * FileUtil Class
 * File Utilities for AWE
 *
 * @author Pablo GARCIA - 19/JUL/2017
 */
public class FileUtil extends ServiceConfig {

  // Upload identifier
  @Value("${application.base.path:/}")
  private String applicationBasePath;

  // Upload identifier
  @Value("${file.upload.max.files.folder:1000}")
  private Integer maxFilesPerFolder;

  // Object mapper
  private ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Retrieves a previously uploaded file from upl path
   * @param fileData File data
   * @param create Create path
   * @return Uploaded file path
   */
  public String getFullPath(FileData fileData, boolean create) {
    // Variable definition
    String relativePath = fileData.getRelativePath();
    Long size = fileData.getFileSize();

    // Calculate max elements per folder
    if (relativePath == null && create) {
      relativePath = "tmp" + (size % maxFilesPerFolder);
    } else if (relativePath == null) {
      relativePath = "";
    }

    // Calculate upload path
    String absolutePath = StringUtil.getAbsolutePath(fileData.getBasePath() + relativePath + AweConstants.FILE_SEPARATOR, applicationBasePath);

    // Generate folder (if not null)
    if (create) {
      fileData.setRelativePath(relativePath);
      new File(absolutePath).mkdirs();
    }

    return absolutePath;
  }

  /**
   * Transform fileData into a string
   * @param fileData File Data
   * @return Stringified filedata
   * @throws com.almis.awe.exception.AWException
   */
  public String fileDataToString(FileData fileData) throws AWException {
    try {
      return EncodeUtil.encodeSymmetric(CompressionUtil.compress(StringUtil.compressJson(objectMapper.writeValueAsString(fileData))));
    } catch (IOException exc) {
      throw new AWException(getLocale("ERROR_TITLE_READING_PARAMETERS"),
              getLocale("ERROR_TITLE_READING_PARAMETER", fileData.getFileName()),  exc);
    }
  }

  /**
   * Transform fileData into a string
   * @param fileStringEncoded File String encoded
   * @return FileData
   * @throws com.almis.awe.exception.AWException
   */
  public FileData stringToFileData(String fileStringEncoded) throws AWException {
    try {
      String fileString = StringUtil.decompressJson(CompressionUtil.decompress(EncodeUtil.decodeSymmetricAsByteArray(fileStringEncoded)));
      return objectMapper.treeToValue(objectMapper.readTree(fileString), FileData.class);
    } catch (IOException exc) {
      throw new AWException(getLocale("ERROR_TITLE_READING_PARAMETERS"),
              getLocale("ERROR_TITLE_READING_PARAMETER", fileStringEncoded),  exc);
    }
  }
}
