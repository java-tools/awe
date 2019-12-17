package com.almis.awe.scheduler.enums;

/**
 * @author dfuentes
 */
public enum TaskLaunchType {
  MANUAL(0),
  SCHEDULED(1),
  FILE_TRACKING(2),
  DEPENDENCY(3);

  private Integer value;

  TaskLaunchType(Integer value) {
    this.value = value;
  }

  /**
   * Get value
   * @return
   */
  public Integer getValue() {
    return value;
  }

  /**
   * Get value of integer
   *
   * @param value Value
   * @return Task launch type
   */
  public static TaskLaunchType valueOf(Integer value) {
    for (TaskLaunchType enumerated : values()) {
      if (enumerated.value.equals(value)) {
        return enumerated;
      }
    }
    throw new IllegalArgumentException();
  }
}
