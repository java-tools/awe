package com.almis.awe.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.FileService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Manage template request
 */
@RestController
@RequestMapping("/file")
public class UploadController extends ServiceConfig {

  // Autowired services
  private FileService fileService;
  private FileUtil fileUtil;
  private BroadcastService broadcastService;

  // Upload identifier
  @Value("${file.upload.identifier:u}")
  private String uploadIdentifierKey;

  // Connection identifier
  @Value("${application.parameter.comet.id:s}")
  private String connectionIdentifierKey;

  /**
   * Autowired constructor
   * @param fileService File service
   * @param fileUtil File util
   * @param broadcastService Broadcast service
   */
  @Autowired
  public UploadController(FileService fileService, FileUtil fileUtil, BroadcastService broadcastService) {
    this.fileService = fileService;
    this.fileUtil = fileUtil;
    this.broadcastService = broadcastService;
  }

  /**
   * Upload a file
   * @param file File to upload
   * @param request Request
   * @throws AWException Error uploading file
   */
  @RequestMapping(value="/upload", method=RequestMethod.POST, consumes = {"multipart/form-data"})
  @ResponseStatus(HttpStatus.OK)
  public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 HttpServletRequest request) throws AWException {
    // Initialize parameters
    AweRequest aweRequest = getRequest();
    aweRequest.init(request);
    String connectionKey = aweRequest.getParameterAsString(connectionIdentifierKey);
    ObjectNode address = (ObjectNode) aweRequest.getParameter(AweConstants.PARAMETER_ADDRESS);
    String folder = aweRequest.getParameterAsString(AweConstants.PARAMETER_DESTINATION);

    // Upload the file
    FileData fileData = fileService.uploadFile(file, folder);

    // Generate file uploaded
    ClientAction uploadedNotification = new ClientAction("file-uploaded");
    uploadedNotification.setAsync(true);
    uploadedNotification.setAddress(new ComponentAddress(address));
    uploadedNotification.addParameter("path", fileUtil.fileDataToString(fileData));
    uploadedNotification.addParameter("name", fileData.getFileName());
    uploadedNotification.addParameter("size", fileData.getFileSize());
    uploadedNotification.addParameter("type", fileData.getMimeType());

    // Broadcast file uploaded
    broadcastService.broadcastMessageToUID(connectionKey, uploadedNotification);
  }

  /**
   * Handle upload error
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler(AWException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public String handleAWException(AWException exc) {
    // Initialize parameters
    return broadcastError(exc.getTitle(), exc.getMessage(), exc);
  }

  /**
   * Handle upload error
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  public String handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return broadcastError(getElements().getLocale("ERROR_TITLE_SIZE_LIMIT"), exc.getLocalizedMessage(), exc);
  }

  /**
   * Handle upload error
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler({MultipartException.class, FileUploadException.class, Exception.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public String handleIOException(Exception exc) {
    return broadcastError(getElements().getLocale("ERROR_TITLE_FILE_UPLOAD"), exc.getLocalizedMessage(), exc);
  }

  /**
   * Broadcast error
   * @param title Title
   * @param message Message
   * @param exc Exception
   */
  private String broadcastError(String title, String message, Exception exc) {
    String connectionKey = getSession().getParameter(String.class, AweConstants.SESSION_TOKEN);

    // Log error
    getLogger().log(UploadController.class, Level.ERROR, title, exc);

    // Generate message action
    ClientAction messageAction = new ClientAction("message")
            .setAsync(true)
            .addParameter("type", "error")
            .addParameter("title", title)
            .addParameter("message", message);

    // Broadcast error
    broadcastService.broadcastMessageToUID(connectionKey, messageAction);
    return title;
  }
}
