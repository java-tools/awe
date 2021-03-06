package com.almis.awe.test.unit.rest;

import com.almis.awe.model.dto.ServiceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class MaintainRestControllerTest extends AweSpringRestTests {
  @LocalServerPort
  private int port;

  TestRestTemplate restTemplate = new TestRestTemplate();
  HttpHeaders headers = new HttpHeaders();

  //inserts
  private String insert = "testInsert";
  //updates
  private String update = "testUpdate";
  private String updateParameters = "testUpdateParameters";
  //delete
  private String delete = "testDelete";

  /**
   * Initializes json mapper for tests
   *
   * @throws Exception error updating user
   */
  @Before
  public void initHeaders() throws Exception {
    headers.put("Authorization", Arrays.asList("Basic dGVzdDp0ZXN0"));
  }

  /**
   * Creates an url with the local port
   *
   * @param uri
   *
   * @return
   */
  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }

  /*
   * Maintains
   *
   * */

  @Test
  public void maintainAInsert() throws Exception {
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"clientActionList\":[],\"variableMap\":{\"MESSAGE_TITLE\":\"Operation successful\",\"MESSAGE_TYPE\":\"ok\",\"MESSAGE_DESCRIPTION\":\"The selected maintain operation has been successfully performed\",\"MESSAGE_RESULT_DETAILS\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTarget\",\"Act\":1,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":1,\"Nam\":\"testIncludeTarget\",\"Date\":null}}]},\"resultDetails\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTarget\",\"Act\":1,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":1,\"Nam\":\"testIncludeTarget\",\"Date\":null}}]}", ServiceData.class);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/maintain/" + insert),
            HttpMethod.POST, entity, ServiceData.class);


    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }

  @Test
  public void maintainBUpdate() throws Exception {
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"clientActionList\":[],\"variableMap\":{\"MESSAGE_TITLE\":\"Operation successful\",\"MESSAGE_TYPE\":\"ok\",\"MESSAGE_DESCRIPTION\":\"The selected maintain operation has been successfully performed\",\"MESSAGE_RESULT_DETAILS\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTargetUpd\",\"Act\":2,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":2,\"Nam\":\"testIncludeTargetUpd\"}}]},\"resultDetails\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTargetUpd\",\"Act\":2,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":2,\"Nam\":\"testIncludeTargetUpd\"}}]}>", ServiceData.class);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/maintain/" + update),
            HttpMethod.POST, entity, ServiceData.class);


    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }

  @Test
  public void maintainCUpdateWithParameters() throws Exception {
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"clientActionList\":[],\"variableMap\":{\"MESSAGE_TITLE\":\"Operation successful\",\"MESSAGE_TYPE\":\"ok\",\"MESSAGE_DESCRIPTION\":\"The selected maintain operation has been successfully performed\",\"MESSAGE_RESULT_DETAILS\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTargetUpd\",\"Act\":2,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":2,\"Nam\":\"testIncludeTargetUpd\"}}]},\"resultDetails\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTargetUpd\",\"Act\":2,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":2,\"Nam\":\"testIncludeTargetUpd\"}}]}>", ServiceData.class);

    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.set("nam", JsonNodeFactory.instance.textNode("testIncludeTargetUpd"));

    HttpEntity<ObjectNode> entity = new HttpEntity<>(node, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/maintain/" + updateParameters),
            HttpMethod.POST, entity, ServiceData.class);

    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }

  @Test
  public void maintainDelete() throws Exception {
    ServiceData expected = new ObjectMapper().readValue("{\"valid\":true,\"type\":\"OK\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"clientActionList\":[],\"variableMap\":{\"MESSAGE_TITLE\":\"Operation successful\",\"MESSAGE_TYPE\":\"ok\",\"MESSAGE_DESCRIPTION\":\"The selected maintain operation has been successfully performed\",\"MESSAGE_RESULT_DETAILS\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30}}]},\"resultDetails\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30}}]}", ServiceData.class);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<ServiceData> response = restTemplate.exchange(
            createURLWithPort("/api/maintain/" + delete),
            HttpMethod.POST, entity, ServiceData.class);


    Assert.assertEquals(new ObjectMapper().writeValueAsString(expected), new ObjectMapper().writeValueAsString(response.getBody()));
  }
}
