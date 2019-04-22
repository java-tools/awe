package com.almis.awe.tools.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.tools.filemanager.enums.FileModeEnum;
import com.almis.awe.tools.service.FileManagerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controller for File Manager
 *
 * @author jbellon
 *
 */
@Controller
@RequestMapping("/fm")
public class FileManagerController {

  private static final Logger LOGGER = LogManager.getLogger(FileManagerController.class);

  // Autowired services
  FileManagerService service;
  ServletContext context;

  /**
   * Autowired constructor
   * @param service File manager service
   * @param context Servlet context
   */
  @Autowired
  public FileManagerController(FileManagerService service, ServletContext context) {
    this.service = service;
    this.context = context;
  }

  /**
   * Handler for home page
   * @param response Response
   * @param request Request
   * @param session Session
   * @return Index page
   */
  @GetMapping("/home")
  public String home(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    return "fileManager";
  }

  /**
   * Get method
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws IOException
   * @throws AWException
   */
  @GetMapping("/{actionId}")
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, AWException {

    File file = null;

    // File action
    FileModeEnum mode = FileModeEnum.valueOf(request.getParameter("action").toUpperCase());
    switch (mode) {
      case DOWNLOAD:
        String preview = request.getParameter("preview");
        String path = request.getParameter("path");
        file = service.downloadFile(path, preview);
        break;
      case DOWNLOADMULTIPLE:
        String[] toFilename = request.getParameterValues("toFilename");
        String[] items = request.getParameterValues("items[]");
        file = service.downloadAsZipFile(toFilename, items);
        break;
      default:
        throw new AWException("Not implemented");
    }

    if (!file.isFile()) {
      // if not a file, it is a folder, show this error.
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource Not Found");
      return;
    }

    response.setHeader("Content-Type", context.getMimeType(file.getName()));
    response.setHeader("Content-Length", String.valueOf(file.length()));
    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

    try (FileInputStream input = new FileInputStream(file);
         BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())){

      byte[] buffer = new byte[8192];
      for (int length = 0; (length = input.read(buffer)) > 0;) {
        output.write(buffer, 0, length);
      }
    } catch (Exception ex) {
      LOGGER.error("Error opening resources", ex);
    } finally {
      // Clean temp zip files
      if (mode.equals(FileModeEnum.DOWNLOADMULTIPLE)) {
        try {
          Files.deleteIfExists(Paths.get(file.toString()));
        } catch (IOException ex) {
          LOGGER.error("Error deleting temporal zip", ex);
        }
      }
    }
  }

  /**
   * POST method
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws IOException
   *
   */
  @PostMapping("/{actionId}")
  @ResponseBody
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LOGGER.debug("doPost");
    JsonNode responseNode = null;
    try {
      ObjectNode params = requestParamsToJSON(request);
      responseNode = service.fileOperation(params);
    } catch (Exception exc) {
      LOGGER.error("Error handling post request", exc);
      try {
        responseNode = service.error(exc);
      } catch (Exception ex) {
        response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
    }

    writeResponse(response, responseNode);
  }

  /**
   * POST upload method
   *
   * @param files Upload files
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws IOException Error uploading
   *
   */
  @PostMapping("/uploadUrl")
  @ResponseBody
  public void doUploadPost(@RequestParam("files") List<MultipartFile> files, HttpServletRequest request, HttpServletResponse response) throws IOException {
    LOGGER.debug("doUploadPost");
    JsonNode responseNode = null;
    try {
      // if request contains multipart-form-data
      if (ServletFileUpload.isMultipartContent(request)) {
        String destination = request.getParameter("destination");
        responseNode = service.uploadFile(destination, files);
      }
    } catch (Exception exc) {
      LOGGER.error("Error handling post request", exc);
      try {
        responseNode = service.error(exc);
      } catch (Exception ex) {
        response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
    }

    writeResponse(response, responseNode);
  }

  /**
   * Get parameters of request body as Json
   * @param request Request
   * @return Parameters
   */
  private ObjectNode requestParamsToJSON(ServletRequest request) {
    ObjectNode paramJson = JsonNodeFactory.instance.objectNode();
    ObjectMapper mapper = new ObjectMapper();

    StringBuilder jb = new StringBuilder();
    String line = null;
    try {
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null) {
        jb.append(line);
      }
      paramJson = (ObjectNode) mapper.readTree(jb.toString());

    } catch (Exception ex) {
      LOGGER.error("Error getting request parameters", ex);
    }
    return paramJson;
  }

  /**
   * Writes a JsonNode in the output
   *
   * @param response Response
   * @param node Node
   * @throws IOException Error writing output
   */
  private void writeResponse(HttpServletResponse response, JsonNode node) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    out.print(node);
    out.flush();
  }
}
