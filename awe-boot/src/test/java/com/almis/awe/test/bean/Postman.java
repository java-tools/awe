package com.almis.awe.test.bean;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.data.builder.DataListBuilder;

import java.util.Arrays;

public class Postman implements ResponseWrapper {

  private Boolean gzipped;
  private PostmanHeaders headers;
  private String method;

  @Override
  public ServiceData toServiceData() throws AWException {
    DataList dataList = new DataListBuilder()
            .addColumn("gzipped", Arrays.asList(new Boolean[]{getGzipped()}), "BOOLEAN")
            .addColumn("accept", Arrays.asList(new String[]{getHeaders().getAccept()}), "STRING")
            .addColumn("acceptEncoding", Arrays.asList(new String[]{getHeaders().getAcceptEncoding()}), "STRING")
            .addColumn("acceptLanguage", Arrays.asList(new String[]{getHeaders().getAcceptLanguage()}), "STRING")
            .addColumn("cacheControl", Arrays.asList(new String[]{getHeaders().getCacheControl()}), "STRING")
            .addColumn("cookie", Arrays.asList(new String[]{getHeaders().getCookie()}), "STRING")
            .addColumn("postmanToken", Arrays.asList(new String[]{getHeaders().getPostmanToken()}), "STRING")
            .addColumn("userAgent", Arrays.asList(new String[]{getHeaders().getUserAgent()}), "STRING")
            .addColumn("method", Arrays.asList(new String[]{getMethod()}), "STRING")
            .build();
    return new ServiceData().setDataList(dataList);
  }

  public Boolean getGzipped() {
    return gzipped;
  }

  public Postman setGzipped(Boolean gzipped) {
    this.gzipped = gzipped;
    return this;
  }

  public PostmanHeaders getHeaders() {
    return headers;
  }

  public Postman setHeaders(PostmanHeaders headers) {
    this.headers = headers;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public Postman setMethod(String method) {
    this.method = method;
    return this;
  }
}
