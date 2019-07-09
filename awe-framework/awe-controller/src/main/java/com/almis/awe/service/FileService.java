package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage application initialization
 */
public class FileService extends ServiceConfig {

  // Autowired services
  private BroadcastService broadcastService;
  private FileUtil fileUtil;
  private LogUtil logger;
  private AweRequest request;

  // Upload identifier
  @Value("${file.upload.path:/}")
  private String uploadBaseFolder;

  private static final String ERROR_TITLE_FILE_READING_ERROR = "ERROR_TITLE_FILE_READING_ERROR";
  private static final String ERROR_MESSAGE_FILE_READING_ERROR = "ERROR_MESSAGE_FILE_READING_ERROR";

  /**
   * Autowired constructor
   * @param broadcastService Broadcaster
   * @param fileUtil File utilities
   * @param logger Logger
   * @param request Request
   */
  @Autowired
  public FileService(BroadcastService broadcastService, FileUtil fileUtil, LogUtil logger, AweRequest request) {
    this.broadcastService = broadcastService;
    this.fileUtil = fileUtil;
    this.logger = logger;
    this.request = request;
  }

  /**
   * Retrieve a text file content
   *
   * @param path        File path
   * @param contentType Content type
   * @return Text file
   * @throws AWException Error retrieving text file
   */
  public ResponseEntity<String> getTextFile(String path, String contentType) throws AWException {
    String fileContent = "";

    try {
      fileContent = new String(Files.readAllBytes(Paths.get(path)));
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_FILE_READING_ERROR), getLocale(ERROR_MESSAGE_FILE_READING_ERROR, path), exc);
    }

    // Generate text file headers
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.parseMediaType(contentType));

    // Retrieve entity
    return new ResponseEntity<>(fileContent, responseHeaders, HttpStatus.CREATED);
  }

  /**
   * Retrieve a log file content
   *
   * @return Service data with log value
   * @throws AWException Error retrieving log file
   */
  public ServiceData getLogFile() throws AWException {
    ServiceData serviceData = new ServiceData();
    List<String> content = new ArrayList<>();
    // Get path and offset
    String path = EncodeUtil.decodeSymmetric(getRequest().getParameterAsString("path"));
    Integer offset = getRequest().getParameter("offset").asInt();

    try (FileInputStream file = new FileInputStream(path)) {
      // Read file
      Integer line = 0;
      String lineString;
      InputStreamReader fileReader = new InputStreamReader(file, StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      while ((lineString = bufferedReader.readLine()) != null) {
        if (offset <= line) {
          content.add(lineString);
        }
        line++;
      }
      bufferedReader.close();

      // Generate output
      serviceData.addVariable("LOG_CONTENT", new CellData(content));
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_FILE_READING_ERROR), getLocale(ERROR_MESSAGE_FILE_READING_ERROR, path), exc);
    }

    // Retrieve entity
    return serviceData;
  }

  /**
   * Retrieve a text file content
   *
   * @param path        File path
   * @param contentType Content type
   * @return Text file
   * @throws AWException Error retrieving text file
   */
  public ResponseEntity<FileSystemResource> getFileStream(String path, String contentType) throws AWException {
    HttpHeaders headers = new HttpHeaders();

    try {
      // Generate text file headers
      FileSystemResource resource = new FileSystemResource(path);
      headers.setContentType(MediaType.parseMediaType(contentType));
      headers.setContentLength(resource.contentLength());
      StringBuilder builder = new StringBuilder("inline;filename=\"").append(resource.getFilename()).append("\"");
      headers.add(HttpHeaders.CONTENT_DISPOSITION, builder.toString());

      // Retrieve entity
      return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_FILE_READING_ERROR), getLocale(ERROR_MESSAGE_FILE_READING_ERROR, path), exc);
    }
  }

  /**
   * Retrieve a file content
   *
   * @param fileData File data
   * @return Text file
   * @throws AWException Error retrieving text file
   */
  public ResponseEntity<FileSystemResource> getFileStream(FileData fileData) throws AWException {
    String filePath = fileUtil.getFullPath(fileData, false) + fileData.getFileName();
    return getFileStream(filePath, fileData.getMimeType());
  }

  /**
   * Retrieve a text file content
   *
   * @param fileData           File data
   * @param downloadIdentifier Download identifier
   * @return File to download
   * @throws AWException Error retrieving text file
   */
  public ResponseEntity<byte[]> downloadFile(FileData fileData, Integer downloadIdentifier) throws AWException {
    // convert JSON to Employee
    HttpHeaders headers = new HttpHeaders();
    String filePath = fileUtil.getFullPath(fileData, false) + fileData.getFileName();

    try (InputStream fileStream = fileData.getFileStream() != null ? fileData.getFileStream() : new FileInputStream(new File(filePath));
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      // Generate text file headers
      headers.setContentType(MediaType.parseMediaType(fileData.getMimeType()));
      headers.setContentLength(fileData.getFileSize());
      StringBuilder builder = new StringBuilder("attachment;filename=\"").append(fileData.getFileName()).append("\"");
      headers.add(HttpHeaders.CONTENT_DISPOSITION, builder.toString());
      headers.add("Content-Transfer-Encoding", "binary");
      headers.add("Filename", fileData.getFileName());

      // Publish file downloaded
      ClientAction fileDownloadedAction = new ClientAction(new StringBuilder("file-downloaded/").append(downloadIdentifier).toString());
      fileDownloadedAction.setAsync(true);
      broadcastService.broadcastMessageToUID(request.getToken(), fileDownloadedAction);

      // Generate the file stream
      IOUtils.copy(fileStream, outputStream);

      // Return the file stream
      return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_FILE_READING_ERROR), getLocale(ERROR_MESSAGE_FILE_READING_ERROR, filePath), exc);
    }
  }

  /**
   * Retrieve a text file content
   *
   * @param file   Uploaded file
   * @param folder Destination folder
   * @return File path
   * @throws AWException Error retrieving text file
   */
  public FileData uploadFile(MultipartFile file, String folder) throws AWException {
    FileData fileData = null;
    try {
      if (!file.isEmpty()) {
        // Store file
        fileData = new FileData(FileUtil.sanitizeFileName(file.getOriginalFilename()), file.getSize(), FileUtil.extractContentType(file), folder);
        fileData.setBasePath(uploadBaseFolder);

        // Generate file path
        String fullPath = fileUtil.getFullPath(fileData, true);

        // Save file on upload path
        File saveTo = new File(fullPath + fileData.getFileName());

        // Log saving file
        logger.log(FileService.class, Level.DEBUG, "Saving file on {0}", saveTo.getCanonicalPath());

        // Store file
        file.transferTo(saveTo);
      }
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_SAVING_ITEM"),
        getLocale("ERROR_MESSAGE_SAVING_ITEM"), exc);
    }

    return fileData;
  }

  /**
   * Deletes an uploaded file
   *
   * @return Service data
   * @throws AWException Error deleting file
   */
  public ServiceData deleteFile() throws AWException {
    String fileName = getRequest().getParameterAsString("filename");
    FileData fileData = FileUtil.stringToFileData(fileName);
    return deleteFile(fileData);
  }

  /**
   * Deletes an uploaded file
   *
   * @param fileData File data
   * @return Service data
   * @throws AWException Error deleting file
   */
  public ServiceData deleteFile(FileData fileData) throws AWException {
    ServiceData serviceData = new ServiceData();
    try {
      // Get file data
      String fullPath = fileUtil.getFullPath(fileData, false);

      // Check if file exists
      File dest = new File(fullPath + fileData.getFileName());
      if (!dest.exists()) {
        throw new AWException(getLocale("ERROR_TITLE_NONEXISTENT_FILE"),
          getLocale("ERROR_MESSAGE_NONEXISTENT_FILE", fileData.getFileName()));
      }

      // Remove file from path
      Files.delete(dest.toPath());
    } catch (AWException exc) {
      throw exc;
    } catch (IOException exc) {
      throw new AWException(getLocale("ERROR_TITLE_FILE_DELETE"), getLocale("ERROR_MESSAGE_FILE_DELETE"), exc);
    }
    return serviceData;
  }

  /**
   * Given a file identifier, retrieve file information
   *
   * @return File information
   * @throws AWException Error generating file info
   */
  public ServiceData getFileInfo() throws AWException {
    ServiceData serviceData = new ServiceData();
    String fileName = getRequest().getParameterAsString("filename");
    FileData fileData = FileUtil.stringToFileData(fileName);

    // Set variables
    serviceData
      .addVariable(AweConstants.ACTION_FILE_NAME, fileData.getFileName())
      .addVariable(AweConstants.ACTION_FILE_SIZE, fileData.getFileSize().toString())
      .addVariable(AweConstants.ACTION_FILE_TYPE, fileData.getMimeType())
      .addVariable(AweConstants.ACTION_FILE_PATH, fileName);
    return serviceData;
  }
}
