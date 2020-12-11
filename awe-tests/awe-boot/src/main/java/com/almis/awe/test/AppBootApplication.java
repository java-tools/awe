package com.almis.awe.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class that launches the application.
 */
@SpringBootApplication
public class AppBootApplication extends SpringBootServletInitializer {

  /**
   * The goal of this method is only for running the application as a standalone application, setting up an embedded server.
   *
   * @param args Application arguments
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(AppBootApplication.class);
    app.setApplicationStartup(new BufferingApplicationStartup(2048));
    app.run(args);
  }
}