package com.almis.awe.test;

import io.github.bonigarcia.seljup.DockerBrowser;
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import static io.github.bonigarcia.seljup.BrowserType.CHROME;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:test.properties")
@ExtendWith(SeleniumExtension.class)
public class AllTestsSuiteIT {
  // Logger
  private static Logger logger = LogManager.getLogger(AllTestsSuiteIT.class);

  @Test
  public void NGDemoTest(ChromeDriver driver) {
    driver.get("https://www.blazemeter.com/selenium");
    String homeUrl = driver.findElement(By.cssSelector("div#logo>   a#logo_image ")).getAttribute("href");
    assert homeUrl.equals("https://www.blazemeter.com/");
  }
}

