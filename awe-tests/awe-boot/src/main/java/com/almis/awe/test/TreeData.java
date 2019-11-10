package com.almis.awe.test;

import lombok.Data;

/**
 * Tree data bean
 */
@Data
public class TreeData {
  private String type;
  private String id;
  private String parent;
  private Integer level;
  private Integer isLeaf;
  private String hour;
  private String user;
}
