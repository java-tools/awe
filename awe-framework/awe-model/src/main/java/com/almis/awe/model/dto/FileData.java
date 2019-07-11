package com.almis.awe.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Stores the data to retrieve an uploaded file
 *
 * @author pgarcia
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class FileData implements Serializable {

  private static final long serialVersionUID = 7037606445027020703L;

  private String fileName;
  private Long fileSize;
  private String mimeType;
  private String basePath;
  private String relativePath;
  private transient InputStream fileStream;

  /**
   * Empty constructor
   */
  public FileData() {
  }

  /**
   * Copy constructor
   *
   * @param other fileData object
   */
  public FileData(FileData other) {
    this.fileName = other.fileName;
    this.fileSize = other.fileSize;
    this.mimeType = other.mimeType;
    this.basePath = other.basePath;
    this.relativePath = other.relativePath;
    this.fileStream = other.fileStream;
  }

  /**
   * Generate a file upload data with the file token
   *
   * @param fileName File name
   * @param fileSize File size
   * @param mimeType Mime type
   */
  public FileData(String fileName, Long fileSize, String mimeType) {
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.mimeType = mimeType;
  }

  /**
   * Generate a file upload data with the file token
   *
   * @param fileName     File name
   * @param fileSize     File size
   * @param mimeType     Mime type
   * @param relativePath File destination folder
   */
  public FileData(String fileName, Long fileSize, String mimeType, String relativePath) {
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.mimeType = mimeType;
    this.relativePath = relativePath == null || relativePath.isEmpty() ? null : relativePath;
  }

  /**
   * Retrieve the file name
   *
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Store the file name
   *
   * @param fileName the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Retrieve the file size
   *
   * @return the fileSize
   */
  public Long getFileSize() {
    return fileSize;
  }

  /**
   * Store the file size
   *
   * @param fileSize the fileSize to set
   */
  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  /**
   * Retrieve the file mime type
   *
   * @return the contentType
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Store the file mime type
   *
   * @param contentType the contentType to set
   */
  public void setMimeType(String contentType) {
    this.mimeType = contentType;
  }

  /**
   * Retrieve the file stream if defined
   *
   * @return the fileStream
   */
  public InputStream getFileStream() {
    return fileStream;
  }

  /**
   * Store the file stream
   *
   * @param fileStream the fileStream to set
   */
  public void setFileStream(InputStream fileStream) {
    this.fileStream = fileStream;
  }

  /**
   * Get base path
   *
   * @return Base path
   */
  public String getBasePath() {
    return basePath;
  }

  /**
   * Set base path
   *
   * @param basePath base path
   * @return file data
   */
  public FileData setBasePath(String basePath) {
    this.basePath = basePath;
    return this;
  }

  /**
   * Get relative path
   *
   * @return Relative path
   */
  public String getRelativePath() {
    return relativePath;
  }

  /**
   * Set relative path
   *
   * @param relativePath Relative path
   * @return file data
   */
  public FileData setRelativePath(String relativePath) {
    this.relativePath = relativePath;
    return this;
  }
}
