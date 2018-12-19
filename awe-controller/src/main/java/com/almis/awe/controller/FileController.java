package com.almis.awe.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.service.FileService;
import com.almis.awe.service.MaintainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

  // Download identifier
  @Value("${file.download.identifier:d}")
  private String downloadIdentifier;

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
   *
   * @param request Request
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/text")
  public ResponseEntity<String> getFileAsText(HttpServletRequest request) throws AWException {
    // Initialize parameters
    getRequest().init(request);

    // Get path and content-type
    String path = EncodeUtil.decodeSymmetric(getRequest().getParameterAsString("path"));
    String contentType = getRequest().getParameterAsString("content-type");

    // Retrieve file
    return fileService.getTextFile(path, contentType);
  }

  /**
   * Retrieve generic file stream
   *
   * @param request Request
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/stream")
  public ResponseEntity<FileSystemResource> getFileAsStream(HttpServletRequest request) throws AWException {
    // Initialize parameters
    getRequest().init(request);

    // Get path and content-type
    String path = EncodeUtil.decodeSymmetric(getRequest().getParameterAsString("path"));
    String contentType = getRequest().getParameterAsString("content-type");

    // Retrieve file
    return fileService.getFileStream(path, contentType);
  }

  /**
   * Retrieve generic file stream
   *
   * @param request Request
   * @param targetId Maintain target id
   * @return File content
   * @throws AWException Error retrieving file
   */
  @RequestMapping("/stream/maintain/{targetId}")
  public ResponseEntity<FileSystemResource> getFileAsStream(HttpServletRequest request, @PathVariable("targetId") String targetId) throws AWException {
    // Initialize parameters
    getRequest().init(request);

    // Launch maintain
    ServiceData serviceData = maintainService.launchMaintain(targetId);

    // Retrieve file
    return fileService.getFileStream((FileData) serviceData.getData());
  }

  /**
   * Retrieve file for download
   *
   * @param request Request
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/download")
  public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request) throws AWException {
    // Initialize parameters
    getRequest().init(request);

    // Get path and content-type
    String filedata = getRequest().getParameterAsString("filename");
    Integer downloadIde = getRequest().getParameter(downloadIdentifier).asInt();

    // Retrieve file
    return fileService.downloadFile(fileUtil.stringToFileData(filedata), downloadIde);
  }

  /**
   * Retrieve file for download
   * @param request Request
   * @param targetId Maintain target identifier
   * @return File content
   * @throws AWException Error retrieving file
   */
  @RequestMapping("/download/maintain/{targetId}")
  public ResponseEntity<InputStreamResource> downloadFileMaintainGet(HttpServletRequest request, @PathVariable("targetId") String targetId) throws AWException {
    // Initialize parameters
    getRequest().init(targetId, request);

    // Launch maintain
    ServiceData serviceData = maintainService.launchMaintain(targetId);

    // Get path and content-type
    Integer downloadIde = getRequest().getParameter(downloadIdentifier).asInt();

    // Retrieve file
    return fileService.downloadFile((FileData) serviceData.getData(), downloadIde);
  }

  /**
   * Retrieve file for download
   *
   * @param request Request
   * @return File content
   * @throws AWException Error retrieving file
   */
  @PostMapping("/delete")
  public ServiceData deleteFile(HttpServletRequest request) throws AWException {
    // Initialize parameters
    getRequest().init(request);

    // Get path and content-type
    String filedata = getRequest().getParameterAsString("filename");

    // Retrieve file
    return fileService.deleteFile(fileUtil.stringToFileData(filedata));
  }
}
