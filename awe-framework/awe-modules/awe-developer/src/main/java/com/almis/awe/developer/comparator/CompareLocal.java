/*
 * Package definition
 */
package com.almis.awe.developer.comparator;

import java.util.Comparator;

import com.almis.awe.model.entities.Global;

/**
 * CompareRow Class
 *
 * Used to compare two rows in sort service data
 *
 * @author Pablo VIDAL - 09/may/2013
 */
public class CompareLocal implements Comparator<Global> {

  /**
   * Compare two DataList rows
   *
   * @param local1 Fist row
   * @param local2 Row to compare with
   * @return Compare diff
   */
  @Override
  public int compare(Global local1, Global local2) {
    return local1.getName().compareToIgnoreCase(local2.getName());
  }
}
