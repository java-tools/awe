package com.almis.awe.test.wrappers;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.RequestWrapper;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Test wrapper for JMS sending
 * 
 * @author pgarcia and pvidal
 */
@XStreamAlias("Casa")
public class Casa implements RequestWrapper, ResponseWrapper {

  @XStreamAlias("Ventanas")
  Integer ventanas;

  @XStreamAlias("Puertas")
  Integer puertas;

  @Override
  public void setParameters(Map<String, Object> parameterMap) {
    ventanas = (Integer) parameterMap.get("CrtVen");
    puertas = (Integer) parameterMap.get("CrtPue");
  }

  @Override
  public ServiceData toServiceData() throws AWException {
    List<Integer> listaVentanas = Arrays.asList(new Integer[]{ventanas});
    List<Integer> listaPuertas = Arrays.asList(new Integer[]{puertas});
    DataList datalist = new DataListBuilder()
        .addColumn("OutFld1", listaVentanas, "INTEGER")
        .addColumn("OutFld2", listaPuertas, "INTEGER")
        .build();
    return new ServiceData()
            .setDataList(datalist);
  }
}
