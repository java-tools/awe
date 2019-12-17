package com.almis.awe.scheduler.filechecker;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class FileClient {
  public List<File> listFiles(String host, String path) {
    List<File> files = new ArrayList<>();
    Path folder;

    if (host.equalsIgnoreCase("localhost") || host.equals("127.0.0.1")) {
      folder = Paths.get(path);
    } else {
      folder = Paths.get("\\\\" + host, path);
    }

    if (folder.toFile().exists()) {
      try (Stream<Path> pathList = Files.list(folder)) {
        files.addAll(pathList.map(Path::toFile).collect(Collectors.toList()));
      } catch (IOException exc) {
        log.error("[Folder checker] Error retrieving folder files: {}", folder);
      }
    } else {
      log.error("[Folder checker] The folder {} does not exist", folder);
    }
    return files;
  }
}
