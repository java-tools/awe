package com.almis.awe.test.bean;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.data.builder.DataListBuilder;
import lombok.Data;

import java.util.Collections;

@Data
public class PostmanAuth implements ResponseWrapper {

  private boolean authenticated;

  @Override
  public ServiceData toServiceData() throws AWException {
    return new ServiceData().setDataList(new DataListBuilder()
      .addColumn("authenticated", Collections.singletonList(authenticated), "BOOLEAN")
      .build());
  }
}
