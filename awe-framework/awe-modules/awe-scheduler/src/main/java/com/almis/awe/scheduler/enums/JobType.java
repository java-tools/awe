package com.almis.awe.scheduler.enums;

/**
 * Enum Job types
 *
 * @author pvidal
 */
public enum JobType {
  JOB_COMMAND(0),
  JOB_MAINTAIN(1),
  JOB_TIMEOUT(2),
  JOB_PROGRESS(3);

  private final Integer value;

  JobType(Integer value) {
    this.value = value;
  }

  /**
   * Get integer value
   * @return
   */
  public Integer getValue() {
    return this.value;
  }

  /**
   * Get value of integer
   *
   * @param value Value
   * @return Task launch type
   */
  public static JobType valueOf(Integer value) {
    for (JobType enumerated : values()) {
      if (enumerated.value.equals(value)) {
        return enumerated;
      }
    }
    throw new IllegalArgumentException();
  }
}
