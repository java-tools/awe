package com.almis.awe.test.unit.rest;

import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@Log4j2
public class MicroserviceTest extends AweSpringRestTests {

  /**
   * Initializes json mapper for tests
   *
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception {
    super.setup();

    // Update user
    loginUser();

    // Clean up
    cleanUp("CleanUpScreenConfiguration");
  }

  /**
   * Close and logout
   *
   * @throws Exception error updating user
   */
  @After
  public void clean() throws Exception {
    // Clean up
    cleanUp("CleanUpScreenConfiguration");

    // Update user
    logoutUser();
  }

  /**
   * Test a REST POST
   *
   * @param name       Target action
   * @param action     Server action
   * @param parameters Extra parameters
   * @param expected   Expected result
   * @throws Exception Exception
   */
  private void doRestTest(String name, String action, String parameters, String expected) throws Exception {

    MvcResult mvcResult = mockMvc.perform(post("/action/" + action + "/" + name)
      .with(csrf())
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{" + parameters + "\"max\":30}")
      .accept(MediaType.APPLICATION_JSON)
      .session(session))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);

    // Check expected
    if (expected != null) {
      JSONAssert.assertEquals(expected, result, false);
    }
  }

  /**
   * Simple microservice call
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleMicroservice() throws Exception {
    setParameter("database", "awedb01");
    setParameter("currentDate", "22/02/2019");
    doRestTest("CallAluMicroservice", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Simple microservice call to another microservice
   *
   * @throws Exception Test error
   */
  @Test
  public void testAnotherMicroservice() throws Exception {
    doRestTest("CallAnotherMicroservice", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"null\":null,\"double\":22.0,\"text\":\"test\",\"integer\":22,\"id\":1,\"float\":22.0,\"long\":22}]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Simple microservice call without parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleMicroserviceWithoutParameters() throws Exception {
    setParameter("database", "awedb01");
    setParameter("currentDate", "22/02/2019");
    doRestTest("CallAluMicroserviceWithoutParameters", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Simple microservice call to another microservice
   *
   * @throws Exception Test error
   */
  @Test
  public void testAnotherMicroserviceWithParameters() throws Exception {
    doRestTest("CallAnotherMicroserviceWithParameters", "data", "\"tutu\":\"23/10/1978\", \"lala\":[1,2,4], \"erre\": \"\", \"queErre\": null, \"yQueErre\": null,", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"double\":22.0,\"floatFormatted\":\"22\",\"integer\":22,\"dateFormatted\":\"23/10/1978\",\"longFormatted\":\"22\",\"float\":22.0,\"long\":22,\"doubleFormatted\":\"22\",\"null\":null,\"text\":\"test\",\"id\":1,\"integerFormatted\":\"22\"}]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Simple microservice call to another microservice
   *
   * @throws Exception Test error
   */
  @Test
  public void testAnotherMoreMicroservice() throws Exception {
    doRestTest("CallAnotherMoreMicroservice", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Simple call with overwrite microservice name
   *
   * @throws Exception Test error
   */
  @Test
  public void overwriteMicroserviceNameTest() throws Exception {
    doRestTest("CallOverWriteMicroserviceName", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]");
  }
}