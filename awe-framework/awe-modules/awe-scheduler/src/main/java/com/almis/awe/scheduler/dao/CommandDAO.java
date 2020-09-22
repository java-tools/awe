package com.almis.awe.scheduler.dao;

import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.SystemUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
public class CommandDAO {

  // Private services
  Runtime runtime;

  /**
   * Autowired constructor
   *
   * @param runtime Runtime service
   */
  public CommandDAO(Runtime runtime) {
    this.runtime = runtime;
  }

  /**
   * Run a command task
   *
   * @param commandTask
   * @param envp
   * @param timeout
   * @return
   */
  public Integer runCommand(Task commandTask, String[] envp, final long timeout) {
    int exit = 1;
    Process proc = null;

    String finalCommand = constructCommand(commandTask);
    Path path = Paths.get(commandTask.getCommandPath());
    Path commandPath = Paths.get(commandTask.getCommandPath(), commandTask.getAction());
    log.info("[Batch] Batch {} launch started on path {}", finalCommand, path);

    try {
      if (!commandPath.toFile().exists()) {
        proc = runtime.exec(finalCommand, envp);
      } else {
        proc = runtime.exec(finalCommand, envp, path.toFile());
      }

      // Wait for process
      proc.waitFor(timeout, TimeUnit.SECONDS);

      // Log output and error messages
      logHandler(commandTask, proc.getErrorStream(), "ERROR");
      logHandler(commandTask, proc.getInputStream(), "OUTPUT");

      // Retrieve exit value
      exit = proc.exitValue();
    } catch (IOException exc) {
      log.error("[{}] Error executing command {}", commandTask.getTrigger().getKey(), commandTask.getAction(), exc);
      exit = 1;
    } catch (InterruptedException exc) {
      Thread.currentThread().interrupt();
      log.error("[{}] Error, command interrupted {}", commandTask.getTrigger().getKey().toString(), commandTask.getAction(), exc);
      exit = 1;
    } finally {
      if (proc != null) {
        proc.destroy();
      }
    }

    return exit;
  }

  /**
   * Construct the batch to execute
   *
   * @param commandTask
   * @return
   */
  private String constructCommand(Task commandTask) {
    String finalCommand = commandTask.getAction() + generateParameterList(commandTask.getParameterList());

    if (SystemUtils.IS_OS_WINDOWS && !commandTask.getAction().matches("(.*).exe")) {
      if (commandTask.getAction().matches("(.*).bat")) {
        finalCommand = "start " + finalCommand;
      }
      finalCommand = "cmd /c " + finalCommand;
    } else {
      finalCommand = "./" + finalCommand;
    }
    return finalCommand;
  }

  /**
   * Generate parameter list
   *
   * @param parameters
   * @return
   */
  private String generateParameterList(List<TaskParameter> parameters) {
    String parameterList = parameters.stream().map(TaskParameter::getValue).collect(Collectors.joining(" "));
    return parameterList.isEmpty() ? "" : " " + parameterList;
  }

  /**
   * Log a handler
   *
   * @param task        Task
   * @param inputStream Input stream
   * @param type        Handler
   *                    Private class to handler and trace output command
   */
  private void logHandler(Task task, InputStream inputStream, String type) {
    String line;
    try {
      try (InputStreamReader isr = new InputStreamReader(inputStream);
           BufferedReader br = new BufferedReader(isr)) {
        while ((line = br.readLine()) != null) {
          log.info("[{}] [{}] {}", task.getTrigger().getKey(), type, line);
        }
      }
    } catch (Exception exc) {
      log.error("[Command] Failed to collect the command output: {}", task.getTrigger().getKey(), exc);
    }
  }
}
