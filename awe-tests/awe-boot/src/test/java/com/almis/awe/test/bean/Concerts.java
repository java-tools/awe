package com.almis.awe.test.bean;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Concerts implements ResponseWrapper {

  private List<Concert> results;

  @Override
  public ServiceData toServiceData() throws AWException {
    DataList dataList = new DataList();
    for (Concert concert : getResults()) {
      Map<String, CellData> row = new HashMap<>();
      row.put("name", new CellData(concert.getName()));
      row.put("event", new CellData(concert.getEventDateName()));
      row.put("date", new CellData(concert.getDateOfShow()));
      row.put("userGroup", new CellData(concert.getUserGroupName()));
      row.put("eventHall", new CellData(concert.getEventHallName()));
      row.put("image", new CellData(concert.getImageSource()));
      dataList.addRow(row);
    }
    dataList.setRecords(dataList.getRows().size());
    return new ServiceData().setDataList(dataList);
  }

  public List<Concert> getResults() {
    return results;
  }

  public Concerts setResults(List<Concert> results) {
    this.results = results;
    return this;
  }
}
