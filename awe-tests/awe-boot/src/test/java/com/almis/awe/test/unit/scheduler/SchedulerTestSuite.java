package com.almis.awe.test.unit.scheduler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
  SchedulerQueriesTest.class,
  TaskDAOTest.class,
  SchedulerDAOTest.class,
  CalendarDAOTest.class,
  CommandDAOTest.class,
  FTPFileCheckerTest.class,
  FolderFileCheckerTest.class,
  FileDAOTest.class,
  BroadcastReportJobTest.class,
  EmailReportJobTest.class,
  CronPatternBuilderTest.class,
  ExecutionServiceTest.class
})
@RunWith(Suite.class)
public class SchedulerTestSuite {
}