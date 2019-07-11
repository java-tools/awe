package com.almis.awe.model.util.file;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.util.data.CompressionUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
  private static ObjectMapper objectMapper = new ObjectMapper();

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
   * @throws AWException AWE exception
   */
  public static String fileDataToString(FileData fileData) throws AWException {
    try {
      return EncodeUtil.encodeSymmetric(CompressionUtil.compress(StringUtil.compressJson(objectMapper.writeValueAsString(fileData))));
    } catch (IOException exc) {
      throw new AWException("Error encoding file into string", "There was an error trying to encode file data into string:\n" + fileData.getFileName(),  exc);
    }
  }

  /**
   * Transform fileData into a string
   * @param fileStringEncoded File String encoded
   * @return FileData
   * @throws AWException AWE exception
   */
  public static FileData stringToFileData(String fileStringEncoded) throws AWException {
    try {
      String fileString = StringUtil.decompressJson(CompressionUtil.decompress(EncodeUtil.decodeSymmetricAsByteArray(fileStringEncoded)));
      return objectMapper.treeToValue(objectMapper.readTree(fileString), FileData.class);
    } catch (IOException exc) {
      throw new AWException("Error decoding file from string", "There was an error trying to decode file data from string:\n" + fileStringEncoded,  exc);
    }
  }

  /**
   * Extract safely content type
   * @param file Multipart file
   * @return Sanitized filename
   */
  public static String extractContentType(MultipartFile file) {
    return MimeType.valueOf(file.getContentType()).toString();
  }

  /**
   * Sanitize filename
   * @param filename Filename
   * @return Sanitized filename
   */
  public static String sanitizeFileName(String filename) {
    return filename == null ? "" : filename.replaceAll(".*(\\\\|\\/)(.*)", "$2");
  }

  /**
   * Fix an untrusted path
   * @param paths Untrusted paths
   * @return Normalized path
   */
  public static String fixUntrustedPath(String... paths) {
    List<String> fixedPaths = new ArrayList();
    for (String path : paths) {
      fixedPaths.add(path.replaceAll("\\.\\.(\\\\|\\/)", ""));
    }
    return Paths.get(".", fixedPaths.toArray(new String[0])).normalize().toString();
  }
}
