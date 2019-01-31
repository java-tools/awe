package com.almis.awe.test.unit;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 *
 */
@TestPropertySource("classpath:hsql.properties")
public class MaintainHSQLTest extends MaintainTest {

  // Logger
  private static Logger logger = LogManager.getLogger(MaintainHSQLTest.class);

  /**
   * Launch to update hsql database
   * @throws Exception Error in initialization
   */
  @Test
  public void initializeDatabase() throws Exception {
    String maintainName = "testDatabaseInitialization";

    String dt = "10/10/2008";  // Start date
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Calendar c = Calendar.getInstance();
    c.setTime(sdf.parse(dt));
    ArrayNode dates = JsonNodeFactory.instance.arrayNode();
    ArrayNode datesApp = JsonNodeFactory.instance.arrayNode();
    for (int i = 0; i < 2000; i ++) {
      c.add(Calendar.DATE, 2);
      String date = sdf.format(c.getTime());
      dates.add(date);
      if (i < 89) {
        datesApp.add(date);
      }
    }
    String variables = "\"dat\": " + dates.toString() + ",\"datApp\":" + datesApp.toString() + ",";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
  }
}