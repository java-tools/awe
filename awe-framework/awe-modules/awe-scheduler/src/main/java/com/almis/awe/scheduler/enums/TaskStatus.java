/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.scheduler.enums;

import java.io.Serializable;

/**
 * @author dfuentes
 */
public enum TaskStatus implements Serializable {
  JOB_OK(0),
  JOB_ERROR(1),
  JOB_WARNING(2),
  JOB_STOPPED(3),
  JOB_RUNNING(4),
  JOB_QUEUED(5),
  JOB_INTERRUPTED(6),
  JOB_INFO(7);

  private final Integer value;

  /**
   * constructor
   *
   * @param value
   */
  TaskStatus(int value) {
    this.value = value;
  }

  /**
   * Get task status as integer
   *
   * @return
   */
  public int getValue() {
    return value;
  }

  /**
   * Get value of integer
   *
   * @param value Value
   * @return Task launch type
   */
  public static TaskStatus valueOf(Integer value) {
    for (TaskStatus enumerated : values()) {
      if (enumerated.value.equals(value)) {
        return enumerated;
      }
    }
    throw new IllegalArgumentException();
  }
}
