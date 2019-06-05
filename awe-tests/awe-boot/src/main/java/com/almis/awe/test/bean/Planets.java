package com.almis.awe.test.bean;

import lombok.Data;

import java.util.List;

@Data
public class Planets {
  private List<String> nameList;
  private List<Long> populationList;
}
