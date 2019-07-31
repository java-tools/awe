package com.almis.awe.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.service.FileService;
import com.almis.awe.service.MaintainService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Manage template request
 */
@RestController
@RequestMapping("/file")
public class FileController extends ServiceConfig {

  // Autowired services
  private FileService fileService;
  private LogUtil logger;
  private MaintainService maintainService;

  /**
   * Autowired constructor
   * @param fileService File service
   * @param maintainService Maintain service
   * @param logger Logger service
   */
  @Autowired
  public FileController(FileService fileService, MaintainService maintainService, LogUtil logger) {
    this.fileService = fileService;
    this.maintainService = maintainService;
    this.logger = logger;
  }

  /**
   * Retrieve text file
   * @param token Connection token
   * @param parameters Parameters
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/text")
  public ResponseEntity<String> getFileAsText(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                              @RequestBody ObjectNode parameters) throws AWException {
    // Initialize parameters
    getRequest().init(parameters, token);

    // Get path and content-type
    String path = EncodeUtil.decodeSymmetric(getRequest().getParameterAsString("path"));
    String contentType = getRequest().getParameterAsString("content-type");

    // Retrieve file
    return fileService.getTextFile(path, contentType);
  }

  /**
   * Retrieve generic file stream
   * @param token Connection parameters
   * @param parameters Parameters
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/stream")
  public ResponseEntity<FileSystemResource> getFileAsStream(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                                            @RequestBody ObjectNode parameters) throws AWException {
    // Initialize parameters
    getRequest().init(parameters, token);

    // Get path and content-type
    String path = EncodeUtil.decodeSymmetric(getRequest().getParameterAsString("path"));
    String contentType = getRequest().getParameterAsString("content-type");

    // Retrieve file
    return fileService.getFileStream(path, contentType);
  }

  /**
   * Retrieve generic file stream
   *
   * @param parameters Parameters
   * @param targetId Maintain target id
   * @return File content
   * @throws AWException Error retrieving file
   */
  @GetMapping("/stream/maintain/{targetId}")
  @PostMapping("/stream/maintain/{targetId}")
  public ResponseEntity<FileSystemResource> getFileAsStream(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                                            @RequestBody ObjectNode parameters,
                                                            @PathVariable("targetId") String targetId) throws AWException {
    // Initialize parameters
    getRequest().init(parameters, token);

    // Launch maintain
    ServiceData serviceData = maintainService.launchMaintain(targetId);

    // Retrieve file
    return fileService.getFileStream((FileData) serviceData.getData());
  }

  /**
   * Retrieve file for download
   * @param token Connection token
   * @param parameters Parameters
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                                          @RequestBody ObjectNode parameters) throws AWException {
    // Initialize parameters
    getRequest().init(parameters, token);

    // Retrieve file
    return fileService.downloadFile(FileUtil.stringToFileData(getRequest().getParameterAsString("filename")), getRequest().getParameter("d").asInt());
  }

  /**
   * Retrieve file for download
   * @param token Connection token
   * @param parameters Parameters
   * @param targetId Maintain target
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/download/maintain/{targetId}")
  public ResponseEntity<byte[]> downloadFileMaintain(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                                                  @RequestBody ObjectNode parameters,
                                                                  @PathVariable("targetId") String targetId) throws AWException {
    // Initialize parameters
    getRequest().init(targetId, parameters, token);

    // Launch maintain
    ServiceData serviceData = maintainService.launchMaintain(targetId);

    // Retrieve file
    return fileService.downloadFile((FileData) serviceData.getData(), getRequest().getParameter("d").asInt());
  }

  /**
   * Retrieve file for download
   *
   * @param parameters Parameters
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/delete")
  public ServiceData deleteFile(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                @RequestBody ObjectNode parameters) throws AWException {
    // Initialize parameters
    getRequest().init(parameters, token);

    // Get path and content-type
    String filedata = getRequest().getParameterAsString("filename");

    // Retrieve file
    return fileService.deleteFile(FileUtil.stringToFileData(filedata));
  }

  /**
   * Handle error
   * @param exc Exception to handle
   */
  @ExceptionHandler(AWException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public void handleAWException(AWException exc) {
    logger.log(FileController.class, Level.ERROR, exc.getTitle() + "\n" + exc.getMessage(), exc);
  }
}
