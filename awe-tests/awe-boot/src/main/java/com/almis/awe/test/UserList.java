package com.almis.awe.test;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 *
 * @author pvidal
 *
 */
@Service
public class UserList extends ServiceConfig {

  /**
   * Load users Json file
   * @return User list
   * @throws AWException Error retrieving user list
   */
  public ServiceData loadUsersJsonFile() throws AWException {

    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    final long simulateNumFile = 10;

    try {

      // Read json file
      Resource resource = new ClassPathResource("static/10000_complex.json");
      if (resource.exists()) {
        InputStream resourceInputStream = resource.getInputStream();
        // create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // convert json string to object
        List<User> userList = objectMapper.readValue(resourceInputStream, new TypeReference<List<User>>() {
        });

        // Build dataList
        for (int i = 0; i < simulateNumFile; i++) {
          for (User user : userList) {
            dataList.addRow(user.toDatalistRow());
          }
        }

        dataList.setRecords((long) userList.size() * simulateNumFile);
        serviceData.setDataList(dataList);
      }
    } catch (Exception ex) {
      throw new AWException("Error reading json file", ex);
    }
    return serviceData;
  }

}
