package com.almis.awe.tools.service;

import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.tools.filemanager.enums.FileModeEnum;
import com.almis.awe.tools.filemanager.utils.ZipFileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * File Manager service
 */
@Log4j2
public class FileManagerService implements InitializingBean {
  private static final String SYSTEM_UNIX = "unix";
  private static final String SYSTEM_DOS = "dos";
  private static final String RESULT = "result";
  private static final String ITEMS = "items";
  private static final String NEW_PATH = "newPath";
  private static final String ERROR_PATH = "Error getting fileManager path: {}";

  // Defaut base path
  @Value("${filemanager.base.path:/temp}")
  private String repositoryBasePath;
  private Path basePath;

  // Temp path
  @Value("${filemanager.temp.path:/temp}")
  private String tempPath;

  // Default date format (2016-07-04 12:08:56)
  @Value("${filemanager.date.format:yyyy-MM-dd hh:mm:ss}")
  private String dateFormat;

  // Filesystem provider
  private boolean isUnix = false;
  private boolean isDOS = false;

  /**
   * Class initialization
   */
  public void afterPropertiesSet() {
    log.info("Initializing FILE MANAGER SERVLET... ");
    // load from properties file REPOSITORY_BASE_PATH and DATE_FORMAT, use default if missing
    basePath = Paths.get(repositoryBasePath).toAbsolutePath();

    // Check path
    assertThat(!"".equals(repositoryBasePath) && basePath.toFile().isDirectory(), "Invalid base path (" + basePath + ") , Check " + repositoryBasePath);

    // Check date format
    String dateFormatError = "Invalid date format: " + dateFormat;
    try {
      assertThat(!new SimpleDateFormat(dateFormat).format(new Date()).isEmpty(), dateFormatError);
    } catch (Exception exc) {
      log.error(dateFormatError);
    }

    // Check system
    FileSystem defFS = FileSystems.getDefault();
    for (String fileAttrView : defFS.supportedFileAttributeViews()) {
      log.debug("Default file system supports: {}", fileAttrView);
      if (fileAttrView.equals(SYSTEM_UNIX)) {
        isUnix = true;
      }
      if (fileAttrView.equals(SYSTEM_DOS)) {
        isDOS = true;
      }
    }

    log.info("FILE MANAGER SERVLER initialized ");
  }

  /**
   * Check condition. If not valid, log message as error
   * @param condition Condition
   * @param message Error message
   */
  private void assertThat(boolean condition, String message) {
    try {
      if (!condition) {
        log.error(message);
      }
    } catch (Exception exc) {
      log.error(message);
    }
  }

  /**
   * Download preview file
   *
   * @param path File path
   * @param preview Preview
   * @return File
   */
  public File downloadFile(String path, String preview) {
    log.debug("doGet: download file: {} preview: {}", path, BooleanUtils.toBoolean(preview));
    return resolvePath(basePath, path).toFile();
  }

  /**
   * Download multiple files and zip them
   *
   * @param toFilename File names
   * @param items Items
   * @return File
   * @throws IOException Error downloading as zip file
   */
  public File downloadAsZipFile(String[] toFilename, String[] items) throws IOException {

    // Build empty zip file in tmp path
    Path zipFileName = Paths.get(tempPath, FileUtil.fixUntrustedPath(toFilename));

    // Add repository path to files
    List<String> fileList = new ArrayList<>();
    for (String file : items) {
      // Check path
      fileList.add(resolvePath(basePath, file).toString());
    }

    // Build zip
    ZipFileUtil.create(zipFileName.toString(), fileList);

    return zipFileName.toFile();
  }

  /**
   * Upload file
   * @param destination Destination
   * @param files Files
   * @return  Json node
   */
  public JsonNode uploadFile(String destination, List<MultipartFile> files) {
    // URL: $config.uploadUrl, Method: POST, Content-Type: multipart/form-data
    // Unlimited file upload, each item will be enumerated as file-1, file-2,
    // etc.
    // [$config.uploadUrl]?destination=/public_html/image.jpg&file-1=...&file-2=...
    log.debug("Uploading");
    JsonNode responseJsonObject;

    try {
      if (files.isEmpty()) {
        log.debug("file size = 0");
        throw new IOException("file size = 0");
      } else {
        for (MultipartFile file : files) {
          File f = Paths.get(repositoryBasePath, FileUtil.fixUntrustedPath(destination), FileUtil.sanitizeFileName(file.getOriginalFilename())).toFile();
          if (!write(file, f)) {
            log.error("Error uploading file");
            throw new IOException("write error");
          }
        }
        responseJsonObject = this.success();
      }
    } catch (Exception e) {
      log.error("Cannot write file");
      responseJsonObject = error(e);
    }

    return responseJsonObject;
  }

  /**
   * Manage file manager operation
   * @param params Parameters
   * @return Response
   */
  public JsonNode fileOperation(ObjectNode params) {
    JsonNode responseJsonObject;
    try {
      FileModeEnum mode = FileModeEnum.valueOf(params.get("action").asText().toUpperCase());
      switch (mode) {
        case CREATEFOLDER:
          responseJsonObject = addFolder(params);
          break;
        case CHANGEPERMISSIONS:
          responseJsonObject = changePermissions(params);
          break;
        case COMPRESS:
          responseJsonObject = compress(params);
          break;
        case COPY:
          responseJsonObject = copy(params);
          break;
        case REMOVE:
          responseJsonObject = delete(params);
          break;
        case EDIT:
          // save content
          responseJsonObject = saveFile(params);
          break;
        case EXTRACT:
          responseJsonObject = extract(params);
          break;
        case LIST:
          responseJsonObject = list(params);
          break;
        case RENAME:
          responseJsonObject = rename(params);
          break;
        case MOVE:
          responseJsonObject = move(params);
          break;
        case GETCONTENT:
          responseJsonObject = getContent(params);
          break;
        default:
          throw new ServletException("Not implemented");
      }
      if (responseJsonObject == null) {
        responseJsonObject = error("Generic error : responseJsonObject is null");
      }
    } catch (Exception ex) {
      log.error("Error manage file manager", ex);
      responseJsonObject = error(ex);
    }
    return responseJsonObject;
  }

  /**
   * Write file output stream. Used for downloading files
   *
   * @param input Input stream
   * @param file File
   * @return Is ok
   */
  private boolean write(MultipartFile input, File file) {
    boolean ret = false;

    try (InputStream inputStream = input.getInputStream();
         OutputStream outputStream = new FileOutputStream(file)) {
      int read;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      ret = true;

    } catch (IOException ex) {
      log.error("Error writing to FileOutput", ex);
    }
    return ret;
  }

  /**
   * Get content of text file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode getContent(ObjectNode params) {

    try (FileInputStream stream = new FileInputStream(new File(repositoryBasePath, params.get("item").asText()))) {
      ObjectNode contentObject = JsonNodeFactory.instance.objectNode();
      Path filePath = Paths.get(repositoryBasePath + params.get("item").asText());
      log.debug("getContent of file path: {}", filePath);

      if (filePath.toFile().exists()) {

        FileChannel inChannel = stream.getChannel();
        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        return contentObject.put(RESULT, Charset.defaultCharset().decode(buffer).toString());

      } else {
        log.error("File not found {}", filePath);
        return error("File not found");
      }
    } catch (Exception ex) {
      log.error("getContent", ex);
      return error(ex);
    }
    // Close stream
  }

  /**
   * List files operation
   *
   * List all files or folders in the path
   *
   * @param params Parameters
   * @return Response
   * @throws ServletException Error in transmission
   */
  private JsonNode list(ObjectNode params) throws ServletException {
    String path = params.get("path").asText();
    ObjectNode result = JsonNodeFactory.instance.objectNode();
    ArrayNode resultList = JsonNodeFactory.instance.arrayNode();

    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(repositoryBasePath, path))) {
      SimpleDateFormat dt = new SimpleDateFormat(dateFormat);
      for (Path pathObj : directoryStream) {
        BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);

        ObjectNode element = JsonNodeFactory.instance.objectNode();
        element.put("name", pathObj.getFileName().toString());
        element.put("rights", getPermissions(pathObj));
        element.put("date", dt.format(new Date(attrs.lastModifiedTime().toMillis())));
        element.put("size", attrs.size());
        element.put("type", attrs.isDirectory() ? "dir" : "file");
        resultList.add(element);
      }

      return result.set(RESULT, resultList);
    } catch (AccessDeniedException ex) {
      log.error("[List files] - Access denied to read file", ex);
      return error("Access denied to read file " + ex.getMessage());
    } catch (IOException ex) {
      log.error("[List files] - Error list files of {}", path, ex);
      return error(ex);
    }
  }

  /**
   * Rename file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode rename(ObjectNode params) {
    try {
      String path = params.get("item").asText();
      String newpath = params.get("newItemPath").asText();
      log.debug("Rename from: {} to: {}", path, newpath);

      Path fromPath = Paths.get(repositoryBasePath, path);
      Path toPath = Paths.get(repositoryBasePath, newpath);

      // overwrite existing file, if exists
      CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE };

      // Move file or folder
      Files.move(fromPath, toPath, options);

      return success();
    } catch (Exception ex) {
      log.error("rename", ex);
      return error(ex);
    }
  }

  /**
   * Move file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode move(ObjectNode params) {
    try {

      // overwrite existing file, if exists
      CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE };

      // File items to move
      ArrayNode items = (ArrayNode) params.get(ITEMS);

      for (JsonNode item : items) {

        // Skip parameters 'mode' and 'newPath'
        Path filePath = Paths.get(repositoryBasePath, item.asText());

        // Destination path
        Path toPath = Paths.get(repositoryBasePath, params.get(NEW_PATH).asText(), filePath.getFileName().toString());

        log.debug("Move file: {} to: {}", filePath, toPath);

        // Move file or folder
        Files.move(filePath, toPath, options);
      }

      return success();
    } catch (Exception ex) {
      log.error("move", ex);
      return error(ex);
    }
  }

  /**
   * Copy file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode copy(ObjectNode params) {
    try {

      // Only present in single selection copy)
      JsonNode singleFileName = params.get("singleFilename");

      // File items to copy
      ArrayNode items = (ArrayNode) params.get(ITEMS);

      // overwrite existing file, if exists
      CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING };

      if (singleFileName != null) {

        // Source path
        Path sourcePath = Paths.get(repositoryBasePath, items.get(0).asText());

        // Target path
        Path targetPath = Paths.get(repositoryBasePath, params.get(NEW_PATH).asText(), singleFileName.textValue());

        log.debug("copy from: {} to: {}", sourcePath, targetPath);

        Files.copy(sourcePath, targetPath, options);

      } else {

        for (JsonNode item : items) {
          Path sourcePath = Paths.get(repositoryBasePath, item.asText());

          // Target path
          Path targetPath = Paths.get(repositoryBasePath, params.get(NEW_PATH).asText());

          log.debug("copy from: {} to: {}", sourcePath, targetPath);

          Files.copy(sourcePath, targetPath.resolve(sourcePath.getFileName()), options);
        }
      }

      return success();
    } catch (Exception ex) {
      log.error("copy", ex);
      return error(ex);
    }
  }

  /**
   * Delete file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode delete(ObjectNode params) {
    try {

      // File items to delete
      ArrayNode items = (ArrayNode) params.get(ITEMS);

      for (JsonNode item : items) {

        Path path = Paths.get(repositoryBasePath, item.asText());
        log.debug("delete {}", path);

        if (path.toFile().isDirectory()) {

          // Delete folder
          Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
              log.debug("delete file: {}", file.toString());
              Files.delete(file);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
              Files.delete(dir);
              log.debug("delete dir: {}", dir.toString());
              return FileVisitResult.CONTINUE;
            }
          });
        } else {
          // Remove regular file
          Files.delete(path);
        }

      }
      return success();

    } catch (Exception ex) {
      log.error("delete", ex);
      return error(ex);
    }
  }

  /**
   * Save file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode saveFile(ObjectNode params) {
    // save content
    try {
      String content = params.get("content").asText();
      Path path = Paths.get(repositoryBasePath + params.get("item").asText());

      if (content != null) {
        log.debug("Save file into path: {} content: isNotBlank {}, size {}", path, StringUtils.isNotBlank(content), content.length());
        Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      } else {
        log.debug("Content from path: {} file is empty", path);
      }
      return success();

    } catch (Exception ex) {
      log.error("saveFile", ex);
      return error(ex);
    }
  }

  /**
   * Create folder operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode addFolder(ObjectNode params) {
    try {
      Path path = Paths.get(repositoryBasePath + params.get(NEW_PATH).asText());
      return createFolder(path);
    } catch (Exception ex) {
      log.error("addFolder", ex);
      return error(ex);
    }
  }

  /**
   * Create a folder
   * @param path Folder path
   * @return Success function
   */
  private JsonNode createFolder(Path path) {
    log.debug("createFolder path: {}", path);
    if (!path.toFile().exists()) {
      try {
        Files.createDirectory(path);
      } catch (IOException ex) {
        log.error("createFolder", ex);
      }
    } else {
      return error("Can't create directory: " + path + " - Already exist.");
    }
    return success();
  }

  /**
   * Change permissions method. Only in unix enviroments
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode changePermissions(ObjectNode params) {
    try {

      if (isDOS) {
        return error("This feature is only valid in UNIX environment");
      }

      ArrayNode fileList = (ArrayNode) params.get(ITEMS);
      // "653"
      String perms = params.get("perms").asText();
      // "rw-r-x-wx"
      String permsCode = params.get("permsCode").asText();
      boolean recursive = params.get("recursive").asBoolean();

      log.debug("changepermissions path: {} perms: {} permsCode: {} recursive: {}", fileList, perms, permsCode, recursive);

      for (JsonNode file : fileList) {
        File f = new File(repositoryBasePath, file.asText());
        setPermissions(f, perms, recursive);
      }

      return success();
    } catch (Exception ex) {
      log.error("changepermissions", ex);
      return error(ex);
    }
  }

  /**
   * Compress folder operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode compress(ObjectNode params) {
    try {

      // Zip name
      String zipFileName = params.get("compressedFilename").asText();
      // Destination
      String destination = params.get("destination").asText();

      // File items to compress
      ArrayNode items = (ArrayNode) params.get(ITEMS);

      // Locate File on disk for creation
      Path pathZipFile = Paths.get(repositoryBasePath, destination, zipFileName);

      // Add files to zip
      List<String> fileNames = new ArrayList<>();

      for (JsonNode item : items) {
        // Path to source file
        Path fileToZip = Paths.get(repositoryBasePath, item.textValue());
        fileNames.add(fileToZip.toString());
      }

      // Compress
      ZipFileUtil.create(pathZipFile.toString(), fileNames);

      return success();

    } catch (Exception ex) {
      log.error("compress", ex);
      return error(ex);
    }
  }

  /**
   * Extract file operation
   *
   * @param params Parameters
   * @return Response
   */
  private JsonNode extract(ObjectNode params) {
    try {

      // Destination
      Path destination = Paths.get(repositoryBasePath, params.get("destination").asText());
      // Item
      Path zipFilePath = Paths.get(repositoryBasePath, params.get("item").asText());
      // Extract
      ZipFileUtil.unzip(zipFilePath.toString(), destination.toString());

      return success();
    } catch (Exception ex) {
      log.error("extract", ex);
      return error(ex);
    }
  }

  /**
   * Get permissions operation
   *
   * @param path Path
   * @return Permissions
   * @throws ServletException Error in transmission
   * @throws IOException Error in file
   */
  private String getPermissions(Path path) throws IOException, ServletException {

    String permissionsStr = "";

    if (isUnix) {
      // Unix environment
      PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
      PosixFileAttributes readAttributes = fileAttributeView.readAttributes();
      Set<PosixFilePermission> permissions = readAttributes.permissions();
      permissionsStr = PosixFilePermissions.toString(permissions);
    } else if (isDOS) {
      // Windows environment
      DosFileAttributeView fileAttributeView = Files.getFileAttributeView(path, DosFileAttributeView.class);
      if (fileAttributeView.readAttributes().isReadOnly()) {
        permissionsStr = "readonly";
      }
    } else {
      throw new ServletException("Error get permissions. Unknown filesystem");
    }
    return permissionsStr;
  }

  /**
   * Set permissions operation
   *
   * @param file File
   * @param permsCode Permissions
   * @param recursive Recursive
   * @return Permissions in string
   * @throws IOException Error retrieving file
   */
  private String setPermissions(File file, String permsCode, boolean recursive) throws IOException {
    // http://www.programcreek.com/java-api-examples/index.php?api=java.nio.file.attribute.PosixFileAttributes
    PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class);
    fileAttributeView.setPermissions(PosixFilePermissions.fromString(permsCode));
    if (file.isDirectory() && recursive && file.listFiles() != null) {
      for (File f : file.listFiles()) {
        setPermissions(f, permsCode, recursive);
      }
    }
    return permsCode;
  }

  /**
   * Build json error response
   *
   * @param ex Exception
   * @return Error message
   */
  public JsonNode error(Exception ex) {
    return error(ex.getClass().getCanonicalName() + "-" + ex.getMessage());
  }

  /**
   * Build json error response
   *
   * @param msg Message
   * @return Error message
   */
  private JsonNode error(String msg) {
    // Json error format --> RESULT: "success": false, "error": "msg"
    ObjectNode errorJson = JsonNodeFactory.instance.objectNode();
    ObjectNode result = JsonNodeFactory.instance.objectNode();
    result.set("success", JsonNodeFactory.instance.booleanNode(false));
    result.set("error", new TextNode(msg));
    return errorJson.set(RESULT, result);
  }

  /**
   * Build json success response
   *
   * @return Response
   */
  private JsonNode success() {
    // Json success format --> RESULT: "success": true, "error": null
    ObjectNode successJson = JsonNodeFactory.instance.objectNode();
    ObjectNode result = JsonNodeFactory.instance.objectNode();
    result.set("success", JsonNodeFactory.instance.booleanNode(true));
    result.set("error", null);
    return successJson.set(RESULT, result);
  }

  /**
   * Resolves an untrusted user-specified path against the API's base directory.
   * Paths that try to escape the base directory are rejected.
   *
   * @param baseDirPath
   *            the absolute path of the base directory that all user-specified
   *            paths should be within
   * @param strFileManagerPath
   *            the untrusted path provided by the API fileManager, expected to be
   *            relative to {@code baseDirPath}
   */
  private Path resolvePath(final Path baseDirPath, final String strFileManagerPath) {

    // Check basedir
    if (!baseDirPath.isAbsolute()) {
      log.error(ERROR_PATH, baseDirPath);
      throw new IllegalArgumentException("FileManager: base path must be absolute");
    }

    // Check file manager path
    Path fileManagerPath = Paths.get(FileUtil.fixUntrustedPath(strFileManagerPath));
    if (fileManagerPath.isAbsolute()) {
      log.error(ERROR_PATH, fileManagerPath);
      throw new IllegalArgumentException("FileManager: path must be relative");
    }

    // Join the two paths together, then normalize so that any ".." elements
    // in the userPath can remove parts of baseDirPath.
    // (e.g. "/foo/bar/baz" + "../attack" -> "/foo/bar/attack")
    final Path resolvedPath = baseDirPath.resolve(fileManagerPath).normalize();

    // Make sure the resulting path is still within the required directory.
    // (In the example above, "/foo/bar/attack" is not.)
    if (!resolvedPath.startsWith(baseDirPath)) {
      throw new IllegalArgumentException("Illegal access: the request was rejected because the URL was not normalized");
    }

    return resolvedPath;
  }
}
