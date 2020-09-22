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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Manage template request
 */
@RestController
@RequestMapping("/file")
public class UploadController extends ServiceConfig {

  // Autowired services
  private final FileService fileService;
  private final BroadcastService broadcastService;

  // Upload identifier
  @Value("${file.upload.identifier:u}")
  private String uploadIdentifierKey;

  /**
   * Autowired constructor
   *
   * @param fileService      File service
   * @param broadcastService Broadcast service
   */
  @Autowired
  public UploadController(FileService fileService, BroadcastService broadcastService) {
    this.fileService = fileService;
    this.broadcastService = broadcastService;
  }

  /**
   * Upload a file
   *
   * @param token            Request token
   * @param file             File to upload
   * @param address          Component address
   * @param destination      Destination folder
   * @param uploadIdentifier Uploader identifier
   * @throws AWException Error uploading file
   */
  @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(HttpStatus.OK)
  public void handleFileUpload(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam(AweConstants.PARAMETER_ADDRESS) String address,
                               @RequestParam(AweConstants.PARAMETER_DESTINATION) String destination,
                               @RequestParam("u") String uploadIdentifier) throws AWException, IOException {
    // Retrieve parameters
    AweRequest aweRequest = getRequest();
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    parameters.set(AweConstants.PARAMETER_ADDRESS, mapper.readTree(address));
    parameters.put(AweConstants.PARAMETER_DESTINATION, destination);
    parameters.put(uploadIdentifierKey, uploadIdentifier);

    // Initialize parameters
    aweRequest.init(parameters, token);

    // Upload the file
    FileData fileData = fileService.uploadFile(file, parameters.get(AweConstants.PARAMETER_DESTINATION).asText());

    // Broadcast file uploaded
    broadcastService.broadcastMessageToUID(token, new ClientAction("file-uploaded")
      .setAsync(true)
      .setAddress(new ComponentAddress(parameters.get(AweConstants.PARAMETER_ADDRESS)))
      .addParameter("path", FileUtil.fileDataToString(fileData))
      .addParameter("name", fileData.getFileName())
      .addParameter("size", fileData.getFileSize())
      .addParameter("type", fileData.getMimeType()));
  }

  /**
   * Handle upload error
   *
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
   *
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  public String handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return broadcastError(getLocale("ERROR_TITLE_SIZE_LIMIT"), exc.getLocalizedMessage(), exc);
  }

  /**
   * Handle upload error
   *
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler({MultipartException.class, FileUploadException.class, Exception.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public String handleIOException(Exception exc) {
    return broadcastError(getLocale("ERROR_TITLE_FILE_UPLOAD"), exc.getLocalizedMessage(), exc);
  }

  /**
   * Broadcast error
   *
   * @param title   Title
   * @param message Message
   * @param exc     Exception
   */
  private String broadcastError(String title, String message, Exception exc) {
    String connectionKey = getSession().getParameter(String.class, AweConstants.SESSION_TOKEN);

    // Log error
    getLogger().log(UploadController.class, Level.ERROR, title, exc);

    // Broadcast error
    broadcastService.broadcastMessageToUID(connectionKey, new ClientAction("message")
      .setAsync(true)
      .addParameter("type", "error")
      .addParameter("title", title)
      .addParameter("message", message));
    return title;
  }
}
