package com.almis.awe.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
public class AllTestsSuiteIT {
  // Logger
  private static Logger logger = LogManager.getLogger(AllTestsSuiteIT.class);

  @Value("${basedir}")
  String basedir;

  @Value("${java.class.path}")
  String mavenTestClasspath;

  @Test
  public void launchSeleniumTests() throws Exception {
    /*for (String suite : suiteList) {
      launchTestSuite(suite);
    }*/
    File buildFile = Paths.get(basedir, "build.xml").toFile();
    Project p = new Project();

    MyLogger consoleLogger = new MyLogger();
    consoleLogger.setErrorPrintStream(System.err);
    consoleLogger.setOutputPrintStream(System.out);
    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);

    p.addBuildListener(consoleLogger);

    p.setUserProperty("ant.file", buildFile.getAbsolutePath());
    p.setProperty("classpath", mavenTestClasspath);
    p.init();
    ProjectHelper helper = ProjectHelper.getProjectHelper();
    p.addReference("ant.projectHelper", helper);
    helper.parse(p, buildFile);
    p.executeTarget("test-selenium");
  }

  class MyLogger extends DefaultLogger {
    @Override
    public void messageLogged(BuildEvent event)
    {
      int priority = event.getPriority();

      // Filter out messages based on priority
      if (priority <= msgOutputLevel)
      {
        System.out.println("ANT: " + event.getMessage());
      }
    }
  }
}

