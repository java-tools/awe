/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.grid.GroupHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfuentes
 */
public class GroupHeaderBuilder extends AweBuilder<GroupHeaderBuilder> {

  private List<ColumnBuilder> columnList;
  private String name;
  private String label;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public GroupHeaderBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    columnList = new ArrayList<>();
  }

  @Override
  public GroupHeaderBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    GroupHeader groupHeader = new GroupHeader();

    groupHeader.setId(getId());

    if (getName() != null) {
      groupHeader.setId(getName());
    }

    if (getLabel() != null) {
      groupHeader.setLabel(getLabel());
    }

    for (ColumnBuilder columnBuilder : getColumnList()) {
      addElement(groupHeader, columnBuilder.build(groupHeader));
    }

    return groupHeader;
  }

  /**
   * Get name
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set name
   *
   * @param name
   * @return
   */
  public GroupHeaderBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   * @return
   */
  public GroupHeaderBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get column list
   *
   * @return
   */
  public List<ColumnBuilder> getColumnList() {
    return columnList;
  }

  /**
   * Add column list
   *
   * @param columnList
   * @return
   */
  public GroupHeaderBuilder addColumnList(ColumnBuilder... columnList) {
    if (columnList != null) {
      if (this.columnList == null) {
        this.columnList = new ArrayList<>();
      }
      this.columnList.addAll(Arrays.asList(columnList));
    }
    return this;
  }

}
