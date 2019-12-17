package com.almis.awe.model.details;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConnectionDetails {
  String screen;
  String user;
  String connection;
  String session;
}
