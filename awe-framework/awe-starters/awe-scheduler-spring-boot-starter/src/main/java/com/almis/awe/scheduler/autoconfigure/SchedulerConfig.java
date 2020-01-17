package com.almis.awe.scheduler.autoconfigure;

import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.dao.*;
import com.almis.awe.scheduler.filechecker.FTPFileChecker;
import com.almis.awe.scheduler.filechecker.FileChecker;
import com.almis.awe.scheduler.filechecker.FileClient;
import com.almis.awe.scheduler.filechecker.FolderFileChecker;
import com.almis.awe.scheduler.listener.SchedulerEventListener;
import com.almis.awe.scheduler.listener.SchedulerJobListener;
import com.almis.awe.scheduler.listener.SchedulerTriggerListener;
import com.almis.awe.scheduler.service.*;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import org.apache.commons.net.ftp.FTPClient;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Scheduler module configuration
 */
@Configuration
@PropertySource("classpath:config/scheduler.properties")
public class SchedulerConfig {

  /**
   * Define Scheduler
   *
   * @return Scheduler
   */
  @Bean
  public Scheduler scheduler(SchedulerFactoryBean factory) {
    return factory.getScheduler();
  }

  /**
   * Define Runtime
   *
   * @return Runtime
   */
  @Bean
  public Runtime runtime() {
    return Runtime.getRuntime();
  }

  /**
   * Define FTPClient
   *
   * @return FTPCLient
   */
  @Bean
  @Scope("prototype")
  public FTPClient ftpClient() {
    return new FTPClient();
  }

  /**
   * Define FileClient
   *
   * @return FileClient
   */
  @Bean
  @Scope("prototype")
  public FileClient fileClient() {
    return new FileClient();
  }

  /**
   * Define Scheduler service
   *
   * @return Scheduler service
   */
  @Bean
  public SchedulerService schedulerService(TaskDAO taskDAO, SchedulerDAO schedulerDAO, CalendarDAO calendarDAO) {
    return new SchedulerService(taskDAO, schedulerDAO, calendarDAO);
  }

  /**
   * Define Task service
   *
   * @return Task service
   */
  @Bean
  public TaskService taskService(QueryService queryService, QueryUtil queryUtil, TaskDAO taskDAO) {
    return new TaskService(queryService, queryUtil, taskDAO);
  }

  /**
   * Define Scheduler service
   *
   * @return Scheduler service
   */
  @Bean
  public ExecutionService timeoutService(Scheduler scheduler) {
    return new ExecutionService(scheduler);
  }

  /*********************************************************************************************************************
   JOB TYPES
   ********************************************************************************************************************/

  /**
   * Define Maintain job service
   *
   * @return Scheduler service
   */
  @Bean
  public MaintainJobService maintainJobService(ExecutionService executionService, MaintainService maintainService, QueryUtil queryUtil, TaskDAO taskDAO, ApplicationEventPublisher eventPublisher) {
    return new MaintainJobService(executionService, maintainService, queryUtil, taskDAO, eventPublisher);
  }

  /**
   * Define Command job service
   *
   * @return Scheduler service
   */
  @Bean
  public CommandJobService commandJobService(ExecutionService executionService, MaintainService maintainService, QueryUtil queryUtil, TaskDAO taskDAO, ApplicationEventPublisher eventPublisher, CommandDAO commandDAO) {
    return new CommandJobService(executionService, maintainService, queryUtil, taskDAO, eventPublisher, commandDAO);
  }

  /*********************************************************************************************************************
   DAO
   ********************************************************************************************************************/

  /**
   * Database Data Object Access
   *
   * @return Database DAO
   */
  @Bean
  public DatabaseDAO databaseDAO(QueryService queryService) {
    return new DatabaseDAO(queryService);
  }

  /**
   * Calendar Data Object Access
   *
   * @return Calendar DAO
   */
  @Bean
  public CalendarDAO calendarDAO(Scheduler scheduler, QueryService queryService, QueryUtil queryUtil) {
    return new CalendarDAO(scheduler, queryService, queryUtil);
  }

  /**
   * Scheduler Data Object Access
   *
   * @return Scheduler DAO
   */
  @Bean
  public SchedulerDAO schedulerDAO(Scheduler scheduler, CalendarDAO calendarDAO, TaskService taskService,
                                   SchedulerTriggerListener triggerListener, SchedulerJobListener jobListener) {
    return new SchedulerDAO(scheduler, calendarDAO, taskService, triggerListener, jobListener);
  }

  /**
   * Task Data Object Access
   *
   * @return Task DAO
   */
  @Bean
  public TaskDAO taskDAO(Scheduler scheduler, QueryService queryService, MaintainService maintainService,
                         QueryUtil queryUtil, CalendarDAO calendarDAO, ServerDAO serverDAO, FileChecker fileChecker) {
    return new TaskDAO(scheduler, queryService, maintainService, queryUtil, calendarDAO, serverDAO, fileChecker);
  }

  /**
   * File Data Object Access
   *
   * @return File DAO
   */
  @Bean
  public FileDAO fileDAO(MaintainService maintainService, QueryUtil queryUtil) {
    return new FileDAO(maintainService, queryUtil);
  }

  /**
   * Server Data Object Access
   *
   * @return File DAO
   */
  @Bean
  public ServerDAO serverDAO(QueryService queryService, QueryUtil queryUtil) {
    return new ServerDAO(queryService, queryUtil);
  }

  /**
   * Server Data Object Access
   *
   * @return File DAO
   */
  @Bean
  public CommandDAO commandDAO(Runtime runtime) {
    return new CommandDAO(runtime);
  }

  /*********************************************************************************************************************
   Checkers
   ********************************************************************************************************************/

  /**
   * Define file checker
   *
   * @return File checker
   */
  @Bean
  public FileChecker fileChecker(FTPFileChecker ftpFileChecker, FolderFileChecker folderFileChecker) {
    return new FileChecker(ftpFileChecker, folderFileChecker);
  }

  /**
   * Define ftp file checker
   *
   * @return FTP File checker
   */
  @Bean
  public FTPFileChecker ftpFileChecker(FileDAO fileDAO, FTPClient ftpClient) {
    return new FTPFileChecker(fileDAO, ftpClient);
  }

  /**
   * Define folder file checker
   *
   * @return Folder File checker
   */
  @Bean
  public FolderFileChecker folderFileChecker(FileDAO fileDAO, FileClient fileClient) {
    return new FolderFileChecker(fileDAO, fileClient);
  }

  /*********************************************************************************************************************
   LISTENERS
   ********************************************************************************************************************/
  @Bean
  public SchedulerTriggerListener schedulerTriggerListener(TaskDAO taskDAO) {
    return new SchedulerTriggerListener(taskDAO);
  }

  @Bean
  public SchedulerJobListener schedulerJobListener(ApplicationEventPublisher eventPublisher) {
    return new SchedulerJobListener(eventPublisher);
  }

  @Bean
  public SchedulerEventListener schedulerEventListener(BroadcastService broadcastService, AweConnectionTracker connectionTracker,
                                                       TaskDAO taskDAO) {
    return new SchedulerEventListener(broadcastService, connectionTracker, taskDAO);
  }
}
