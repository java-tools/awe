package com.almis.awe.notifier.dto;

import com.almis.awe.notifier.type.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NotificationDto {
  private String subscription;
  private String title;
  private String description;
  private String icon;
  private String screen;
  private NotificationType type;
  private String code;
}
