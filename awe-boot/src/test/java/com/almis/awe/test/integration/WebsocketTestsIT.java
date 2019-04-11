package com.almis.awe.test.integration;

import com.almis.awe.testing.utilities.SeleniumUtilities;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.JavascriptExecutor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebsocketTestsIT extends SeleniumUtilities {

  /**
   * Log into the application
   * @throws Exception
   */
  @Test
  public void t000_loginTest() throws Exception {
    checkLogin("test", "test", "#ButUsrAct span.info-text", "Manager (test)");
  }

  /**
   * Log out from the application
   * @throws Exception
   */
  @Test
  public void t999_logoutTest() throws Exception {
    checkLogout(".slogan", "Almis Web Engine");
  }

  /**
   * Websocket message send test
   * @throws Exception Error on test
   */
  @Test
  public void t001_checkWebsocketMessageSend() throws Exception {
    // Title
    setTestTitle("Websocket message send test");

    // Do broadcast test
    broadcastMessageToUser("test", "This is a broadcast message test");

    String a = "var winNew = window.open('" + getBaseUrl() + "session/invalidate','_blank', 'width=1, height=1');setTimeout(function(){ winNew.close();}, 1000);";
    ((JavascriptExecutor) getDriver()).executeScript(a);

    // Pause 5 seconds
    pause(5000);

    // Go to broadcast screen
    gotoScreen("tools", "sites");

    // Accept danger message
    checkAndCloseMessage("danger");

    // Do login
    checkLogin("test", "test", "span.info-text", "Manager (test)");

    // Do broadcast test
    broadcastMessageToUser("test", "This is a broadcast message test");

    // Assert there are no info messages
    checkMessageMissing("info");
  }

  /**
   * Send websocket message to all users
   * @throws Exception Error on test
   */
  @Test
  public void t002_sendWebsocketMessageToAllUsers() throws Exception {
    // Title
    setTestTitle("Send websocket message to all users");

    // Go to broadcast screen
    gotoScreen("tools", "broadcast-messages");

    // Write on criterion
    writeText("MsgDes", "This is a broadcast message test");

    // Search and wait
    clickButton("ButSnd");

    // Accept message
    checkAndCloseMessage("success");

    // Accept message
    checkAndCloseMessage("info");

    // Check message has been deleted
    checkCriterionContents("MsgDes", "");
  }
}
