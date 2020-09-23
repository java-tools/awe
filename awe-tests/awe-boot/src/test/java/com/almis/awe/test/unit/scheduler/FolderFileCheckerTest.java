package com.almis.awe.test.unit.scheduler;

import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.file.Server;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.FileDAO;
import com.almis.awe.scheduler.filechecker.FileClient;
import com.almis.awe.scheduler.filechecker.FolderFileChecker;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.quartz.TriggerBuilder;

import javax.naming.NamingException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
@RunWith(PowerMockRunner.class)
public class FolderFileCheckerTest extends TestUtil {

  @InjectMocks
  private FolderFileChecker fileChecker;

  @Mock
  private FileDAO fileDAO;

  @Mock
  private FileClient fileClient;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(fileChecker).isNotNull();
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkForChangesNoFiles() throws Exception {
    // Mock
    given(fileClient.listFiles(anyString(), anyString())).willReturn(new ArrayList<>());
    Task task = generateTask();

    // Call
    String changedFile = fileChecker.checkForChanges(task);

    // Verify
    assertNull(changedFile);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkForChangesWithFiles() throws Exception {
    // Mock
    java.io.File file = java.io.File.createTempFile("test", ".txt");
    file.deleteOnExit();
    List<java.io.File> files = new ArrayList<>();
    files.add(file);
    given(fileClient.listFiles(anyString(), anyString())).willReturn(files);
    Task task = generateTask();

    // Call
    String changedFile = fileChecker.checkForChanges(task);

    // Verify
    assertEquals(file.getName(), changedFile);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkForChangesWithFilesNoMatch() throws Exception {
    // Mock
    java.io.File file = java.io.File.createTempFile("test", ".pom");
    file.deleteOnExit();
    List<java.io.File> files = new ArrayList<>();
    files.add(file);
    given(fileClient.listFiles(anyString(), anyString())).willReturn(files);
    Task task = generateTask();

    // Call
    String changedFile = fileChecker.checkForChanges(task);

    // Verify
    assertNull(changedFile);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkForChangesWithFilesAndFileModifications() throws Exception {
    // Mock
    java.io.File file = java.io.File.createTempFile("test", ".txt");
    file.deleteOnExit();
    List<java.io.File> files = new ArrayList<>();
    files.add(file);
    given(fileClient.listFiles(anyString(), anyString())).willReturn(files);
    Task task = generateTask();
    addFileModifications(task);

    // Call
    String changedFile = fileChecker.checkForChanges(task);

    // Verify
    assertEquals(file.getName(), changedFile);
  }

  private Task generateTask() {
    Server server = new Server();
    server.setName("tutu");
    server.setHost("127.0.0.1");
    server.setActive(true);
    server.setTypeOfConnection("FTP");

    Task task = new Task();
    task.setTrigger(TriggerBuilder.newTrigger().build());
    task.setFile(new File()
      .setFilePattern(".*\\.txt")
      .setFilePath("test/")
      .setServer(server));
    return task;
  }

  private void addFileModifications(Task task) {
    // Define old calendar modifications
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, -1);

    Map<String, Date> fileModifications = new HashMap<>();
    fileModifications.put("test/test.txt", calendar.getTime());

    task.getFile().setFileModifications(fileModifications);
  }
}
