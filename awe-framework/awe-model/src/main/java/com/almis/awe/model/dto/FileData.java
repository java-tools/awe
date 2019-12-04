package com.almis.awe.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Stores the data to retrieve an uploaded file
 *
 * @author pgarcia
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class FileData implements Serializable {

  private String fileName;
  private Long fileSize;
  private String mimeType;
  private String basePath;
  private String relativePath;
  private transient InputStream fileStream;

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
}
