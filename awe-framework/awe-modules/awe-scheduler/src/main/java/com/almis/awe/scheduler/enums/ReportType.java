package com.almis.awe.scheduler.enums;

public enum ReportType {
  NONE(0),
  EMAIL(1),
  BROADCAST(2),
  MAINTAIN(3),
  NOTIFICATION(4);

  private Integer value;

  ReportType(Integer value) {
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
  public static ReportType valueOf(Integer value) {
    for (ReportType enumerated : values()) {
      if (enumerated.value.equals(value)) {
        return enumerated;
      }
    }
    throw new IllegalArgumentException();
  }
}
