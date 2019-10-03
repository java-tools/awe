package com.almis.awe.test.bean;

import lombok.Data;

import java.util.List;

@Data
public class Denominations {
  private List<String> id;
  private List<Integer> valor;
  private List<Integer> numero;
  private List<Integer> cantidad;
}
