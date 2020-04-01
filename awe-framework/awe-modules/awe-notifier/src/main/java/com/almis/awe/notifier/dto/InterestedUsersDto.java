package com.almis.awe.notifier.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InterestedUsersDto {
  private String user;
  private boolean byWeb;
  private boolean byEmail;
}
