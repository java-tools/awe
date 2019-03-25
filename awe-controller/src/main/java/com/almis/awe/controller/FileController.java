package com.almis.awe.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.service.FileService;
import com.almis.awe.service.MaintainService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
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
  private FileUtil fileUtil;
  private MaintainService maintainService;

  /**
   * Autowired constructor
   * @param fileService File service
   * @param fileUtil File util
   * @param maintainService Maintain service
   */
  @Autowired
  public FileController(FileService fileService, FileUtil fileUtil, MaintainService maintainService) {
    this.fileService = fileService;
    this.fileUtil = fileUtil;
    this.maintainService = maintainService;
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
   * @param fileData File data
   * @param downloadId Download identifier
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/download")
  public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("token") String token,
                                                          @RequestParam("filename") String fileData,
                                                          @RequestParam("d") Integer downloadId) throws AWException {
    // Initialize parameters
    getRequest().init(JsonNodeFactory.instance.objectNode(), token);

    // Retrieve file
    return fileService.downloadFile(fileUtil.stringToFileData(fileData), downloadId);
  }

  /**
   * Retrieve file for download
   * @param token Connection token
   * @param fileData File data
   * @param downloadId Download identifier
   * @param targetId Maintain target
   * @return File content
   * @throws AWException Error retrieving file
   */
  @GetMapping("/download/maintain/{targetId}")
  @PostMapping("/download/maintain/{targetId}")
  public ResponseEntity<InputStreamResource> downloadFileMaintainGet(@RequestParam("token") String token,
                                                                     @RequestParam("filename") String fileData,
                                                                     @RequestParam("d") Integer downloadId,
                                                                     @PathVariable("targetId") String targetId) throws AWException {
    // Initialize parameters
    getRequest().init(targetId, JsonNodeFactory.instance.objectNode(), token);
    getRequest().setParameter("filename", fileData);

    // Launch maintain
    ServiceData serviceData = maintainService.launchMaintain(targetId);

    // Retrieve file
    return fileService.downloadFile((FileData) serviceData.getData(), downloadId);
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
    return fileService.deleteFile(fileUtil.stringToFileData(filedata));
  }
}
