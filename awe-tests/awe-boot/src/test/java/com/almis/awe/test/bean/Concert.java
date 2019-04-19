package com.almis.awe.test.bean;

import java.util.Date;

public class Concert {

  private String eventDateName;
  private String name;
  private Date dateOfShow;
  private String userGroupName;
  private String eventHallName;
  private String imageSource;

  public String getEventDateName() {
    return eventDateName;
  }

  public Concert setEventDateName(String eventDateName) {
    this.eventDateName = eventDateName;
    return this;
  }

  public String getName() {
    return name;
  }

  public Concert setName(String name) {
    this.name = name;
    return this;
  }

  public Date getDateOfShow() {
    return dateOfShow;
  }

  public Concert setDateOfShow(Date dateOfShow) {
    this.dateOfShow = dateOfShow;
    return this;
  }

  public String getUserGroupName() {
    return userGroupName;
  }

  public Concert setUserGroupName(String userGroupName) {
    this.userGroupName = userGroupName;
    return this;
  }

  public String getEventHallName() {
    return eventHallName;
  }

  public Concert setEventHallName(String eventHallName) {
    this.eventHallName = eventHallName;
    return this;
  }

  public String getImageSource() {
    return imageSource;
  }

  public Concert setImageSource(String imageSource) {
    this.imageSource = imageSource;
    return this;
  }
}
