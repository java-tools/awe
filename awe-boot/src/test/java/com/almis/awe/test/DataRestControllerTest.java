
package com.almis.awe.test;


import com.almis.awe.model.dto.ServiceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

//@RunWith (SpringRunner.class)
//@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataRestControllerTest {
  @LocalServerPort
  private int port;

  TestRestTemplate restTemplate = new TestRestTemplate();
  HttpHeaders headers = new HttpHeaders();


  @Autowired
  private WebApplicationContext wac;
  private MockMvc mockMvc;
  private String queryIdNoAuth = "SimpleEnumPub";
  private String queryIdAuth = "SimpleEnum";
  private String queryWithVariable = "QueryVariableInField";

  /**
   * Initializes json mapper for tests
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    this.headers.put("Authorization", Arrays.asList("Basic bWdyOnJhaQ=="));
  }

  /**
   * Creates an url with the local port
   * @param uri
   * @return
   */
  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }

  /*
   * Queries
   * */

  //@Test
  public void queryWithoutAuthentication() throws Exception {
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"\",\"message\":\"\",\"dataList\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]},\"clientActionList\":[],\"variableMap\":{\"DATA\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]},\"ROWS\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]},\"resultDetails\":[]}",  ServiceData.class);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/data/" + queryIdNoAuth),
            HttpMethod.POST, entity, ServiceData.class);


    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }

  //@Test
  public void queryWithAuthentication() throws Exception{
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"\",\"message\":\"\",\"dataList\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]},\"clientActionList\":[],\"variableMap\":{\"DATA\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]},\"ROWS\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]},\"resultDetails\":[]}",  ServiceData.class);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/data/" + queryIdAuth),
            HttpMethod.POST, entity, ServiceData.class);


    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }

  //@Test
  public void queryWithVariable() throws Exception {
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"\",\"message\":\"\",\"dataList\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"1\":1,\"IdeModPro\":62,\"id\":1},{\"1\":1,\"IdeModPro\":65,\"id\":2},{\"1\":1,\"IdeModPro\":74,\"id\":3},{\"1\":1,\"IdeModPro\":937,\"id\":4}]},\"clientActionList\":[],\"variableMap\":{\"DATA\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"1\":1,\"IdeModPro\":62,\"id\":1},{\"1\":1,\"IdeModPro\":65,\"id\":2},{\"1\":1,\"IdeModPro\":74,\"id\":3},{\"1\":1,\"IdeModPro\":937,\"id\":4}]},\"ROWS\":[{\"1\":1,\"IdeModPro\":62,\"id\":1},{\"1\":1,\"IdeModPro\":65,\"id\":2},{\"1\":1,\"IdeModPro\":74,\"id\":3},{\"1\":1,\"IdeModPro\":937,\"id\":4}]},\"resultDetails\":[]}",  ServiceData.class);

    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.set("variable", JsonNodeFactory.instance.textNode("1"));

    HttpEntity<ObjectNode> entity = new HttpEntity<>(node, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/data/" + queryWithVariable),
            HttpMethod.POST, entity, ServiceData.class);


    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }
}
